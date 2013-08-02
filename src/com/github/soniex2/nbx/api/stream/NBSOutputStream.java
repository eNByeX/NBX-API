package com.github.soniex2.nbx.api.stream;

import java.io.IOException;
import java.io.OutputStream;

import com.github.soniex2.nbx.api.nbs.NBSBlock;
import com.github.soniex2.nbx.api.nbs.NBSHeader;
import com.github.soniex2.nbx.api.nbs.NBSSong;
import com.github.soniex2.nbx.api.nbs.NBSTick;

public class NBSOutputStream extends LittleEndianDataOutputStream {

	public NBSOutputStream(OutputStream os) throws IOException {
		super(os);
	}
	
	public void writeHeader(NBSHeader header) throws IOException {
		writeShort(header.getTicks());
		writeShort(header.getLayers());
		writeASCII(header.getName());
		writeASCII(header.getAuthor());
		writeASCII(header.getOriginalAuthor());
		writeASCII(header.getDescription());
		writeShort(header.getTempo());
		write(header.shouldAutosave() ? 1 : 0);
		write(header.getAutosaveTime());
		write(header.getTimeSig());
		writeInt(header.getMinutes());
		writeInt(header.getLClicks());
		writeInt(header.getRClicks());
		writeInt(header.getBlockAdds());
		writeInt(header.getBlockBreaks());
		writeASCII(header.getImportName());
	}

	public void writeSong(NBSSong song) throws IOException {
		short songTicks = song.getTicks();
		short lastTick = -1;
		for (short x = 0; x < songTicks; x++) {
			NBSTick tick = song.getTick(x);
			if (tick.isEmpty())
				continue;
			writeShort(x - lastTick);
			lastTick = x;
			short layers = tick.getLayers();
			short lastLayer = -1;
			for (short y = 0; y < layers; y++) {
				NBSBlock block = tick.getNote(y);
				if (block == null)
					continue;
				writeShort(y - lastLayer);
				write(block.getInstrument());
				write(block.getNote());
				lastLayer = y;
			}
			writeShort(0);
		}
		writeShort(0);
		for (short x = 0; x < song.getLayers(); x++) {
			// TODO fix this
			writeASCII(String.valueOf(song.getLayerName(x)));
			write(song.getLayerVolume(x));
		}
		write(0);
	}
}
