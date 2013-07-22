package com.github.soniex2.nbx.api;

import java.io.IOException;
import java.io.InputStream;

public class NBSInputStream extends InputStream {

	private InputStream is;
	private NBSHeader header;
	private NBSSong song;

	public NBSInputStream(InputStream is) throws IOException {
		this.is = is;
		header = new NBSHeader(readShort(), readShort(), readString(),
				readString(), readString(), readString(), readShort(),
				readByte() == 1, readByte(), readByte(), readInt(), readInt(),
				readInt(), readInt(), readInt(), readString());
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
		if (available() == 0)
			return;
		for (short i = 0; i < header.getLayers(); i++) {
			song.setLayerName(i, readString());
			song.setLayerVolume(i, readByte());
		}
		if (available() == 0)
			return;
		byte count = readByte();
		for (byte i = 0; i < count; i++) {
			// TODO add custom instrument support
			String name = readString();
			String file = readString();
			byte pitch = readByte();
			boolean play = readByte() == 1;
		}
	}

	@Override
	public int read() throws IOException {
		return readByte();
	}

	protected byte readByte() throws IOException {
		if (is == null) {
			throw new IOException();
		}
		int i = is.read();
		if (i == -1)
			throw new IOException();
		return (byte) i;
	}

	protected short readShort() throws IOException {
		return (short) (readByte() | readByte() * 256);
	}

	protected int readInt() throws IOException {
		return readByte() | readByte() * 256 | readByte() * 65536 | readByte()
				* 16777216;
	}

	protected String readString() throws IOException {
		byte[] b = new byte[readInt()];
		if (is.read(b) != b.length) {
			throw new IndexOutOfBoundsException("EOF reached");
		}
		return new String(b, "US-ASCII");
	}

	public NBSHeader getHeader() {
		return header.copy();
	}

	public NBSSong getSong() {
		return song.copy();
	}

	@Override
	public void close() {
		is = null;
	}

	@Override
	public int available() throws IOException {
		return is.available();
	}

}
