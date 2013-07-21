package com.github.soniex2.nbx.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class NBSOutputStream extends ByteArrayOutputStream {

	public NBSOutputStream(NBSHeader header) throws IOException {
		writeShort(header.getTicks());
		writeShort(header.getLayers());
		writeString(header.getName());
		writeString(header.getAuthor());
		writeString(header.getOriginalAuthor());
		writeString(header.getDescription());
		writeShort(header.getTempo());
		write(header.shouldAutosave() ? 1 : 0);
		write(header.getAutosaveTime());
		write(header.getTimeSig());
		writeInt(header.getMinutes());
		writeInt(header.getLClicks());
		writeInt(header.getRClicks());
		writeInt(header.getBlockAdds());
		writeInt(header.getBlockBreaks());
		writeString(header.getImportName());
	}

	protected void writeInt(int i) throws IOException {
		write(i & 0xFF);
		write((i >>> 8) & 0xFF);
		write((i >>> 16) & 0xFF);
		write((i >>> 24) & 0xFF);
	}

	protected void writeShort(int s) throws IOException {
		write(s & 0xFF);
		write((s >>> 8) & 0xFF);
	}

	protected void writeString(String s) throws IOException {
		byte[] data = s.getBytes("US-ASCII");
		writeInt(data.length);
		write(data);
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
			writeString(String.valueOf(song.getLayerName(x)));
			write(song.getLayerVolume(x));
		}
		write(0);
	}
}
