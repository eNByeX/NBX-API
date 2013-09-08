package com.github.soniex2.nbx.api.stream;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import com.github.soniex2.nbx.api.IInstrument;
import com.github.soniex2.nbx.api.nbs.NBSBlock;
import com.github.soniex2.nbx.api.nbs.NBSHeader;
import com.github.soniex2.nbx.api.nbs.NBSInstrument;
import com.github.soniex2.nbx.api.nbs.NBSSong;
import com.github.soniex2.nbx.api.nbs.NBSTick;

public class NBSOutputStream extends LittleEndianDataOutputStream {

	public NBSOutputStream(OutputStream os) throws IOException {
		super(os);
	}

	/**
	 * Writes a header to the stream.
	 * 
	 * @param header
	 *            the header.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public void writeHeader(NBSHeader header) throws IOException {
		writeShort(header.getTicks());
		writeShort(header.getLayers());
		writeASCII(header.getName());
		writeASCII(header.getAuthor());
		writeASCII(header.getOriginalAuthor());
		writeASCII(header.getDescription());
		writeShort(header.getTempo());
		writeBoolean(header.shouldAutosave());
		write(header.getAutosaveTime());
		write(header.getTimeSig());
		writeInt(header.getMinutes());
		writeInt(header.getLClicks());
		writeInt(header.getRClicks());
		writeInt(header.getBlockAdds());
		writeInt(header.getBlockBreaks());
		writeASCII(header.getImportName());
	}

	/**
	 * Writes a song to the stream.
	 * 
	 * @param song
	 *            the song.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
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
				write(block.inst);
				write(block.note);
				lastLayer = y;
			}
			writeShort(0);
		}
		writeShort(0);
		for (short x = 0; x < song.getLayers(); x++) {
			if (song.getLayerName(x) == null)
				writeASCII("");
			else
				writeASCII(String.valueOf(song.getLayerName(x)));
			write(song.getLayerVolume(x));
		}
		ArrayList<NBSInstrument> insts = new ArrayList<NBSInstrument>();
		for (byte x = 0; x < 9; x++) {
			IInstrument inst = song.getCustomInstrument(x);
			if (inst != null && inst instanceof NBSInstrument) {
				insts.add((NBSInstrument) inst);
			} else {
				insts.add(new NBSInstrument(inst.getName(), "", (byte) 45,
						false));
			}
		}
		write(insts.size());
		for (byte x = 0; x < insts.size(); x++) {
			NBSInstrument inst = insts.get(x);
			writeASCII(inst.getName());
			writeASCII(inst.getLocation());
			writeByte(inst.getPitch());
			writeBoolean(inst.getPress());
		}
	}
}
