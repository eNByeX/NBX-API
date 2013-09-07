package com.github.soniex2.nbx.api.stream;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import com.github.soniex2.nbx.api.nbs.NBSBlock;
import com.github.soniex2.nbx.api.nbs.NBSHeader;
import com.github.soniex2.nbx.api.nbs.NBSInstrument;
import com.github.soniex2.nbx.api.nbs.NBSSong;
import com.github.soniex2.nbx.api.nbs.NBSTick;

public class NBSInputStream extends LittleEndianDataInputStream {

	private NBSHeader header;
	private NBSSong song;

	public NBSInputStream(InputStream is) {
		super(is);
	}

	/**
	 * Reads a header from the stream.
	 * @return The header.
	 * @throws EOFException if this stream reaches the end before reading all the bytes.
	 * @throws IOException if an I/O error occurs.
	 */
	public NBSHeader getHeader() throws IOException {
		if (header != null)
			return header.copy();
		return (header = new NBSHeader(readShort(), readShort(), readASCII(),
				readASCII(), readASCII(), readASCII(), readShort(),
				readBoolean(), readByte(), readByte(), readInt(), readInt(),
				readInt(), readInt(), readInt(), readASCII())).copy();
	}

	/**
	 * Reads a song from the stream.
	 * @return The song.
	 * @throws EOFException if this stream reaches the end before reading the whole song.
	 * @throws IOException if an I/O error occurs.
	 */
	public NBSSong getSong() throws IOException {
		if (song != null)
			return song.copy();
		song = new NBSSong(header.getTicks(), header.getLayers());
		short tick = -1;
		short jumps = 0;
		while (true) {
			jumps = readShort();
			if (jumps == 0) {
				break;
			}
			tick += jumps;
			short layer = -1;
			NBSTick t = new NBSTick(header.getLayers());
			song.addTick(tick, t);
			while (true) {
				jumps = readShort();
				if (jumps == 0) {
					break;
				}
				layer += jumps;
				byte inst = readByte();
				byte key = readByte();
				t.setNote(new NBSBlock(inst, key), layer);
			}
		}
		try {
			for (short i = 0; i < header.getLayers(); i++) {
				song.setLayerName(i, readASCII());
				song.setLayerVolume(i, readByte());
			}
		} catch (EOFException e) {
			return song.copy();
		}
		int a = read();
		if (a == -1) {
			return song.copy();
		}
		for (byte i = 0; i < a; i++) {
			// TODO add custom instrument support
			String name = readASCII();
			String file = readASCII();
			byte pitch = readByte();
			boolean play = readByte() == 1;
			NBSInstrument inst = new NBSInstrument(name, file, pitch, play);
			song.setCustomInstrument(i,inst);
		}
		if (song.getLayers() != header.getLayers()) {
			header.setLayers(song.getLayers());
		}
		return song.copy();
	}

}
