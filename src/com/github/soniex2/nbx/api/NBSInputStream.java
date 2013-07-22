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
				read() == 1 ? true : false, (byte) read(), (byte) read(),
				readInt(), readInt(), readInt(), readInt(), readInt(),
				readString());
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
				byte inst = (byte) read();
				byte key = (byte) read();
				t.setNote(new NBSBlock(inst, key), layer);
			}
		}
	}

	@Override
	public int read() throws IOException {
		if (is == null) {
			throw new IOException();
		}
		return is.read();
	}

	protected short readShort() throws IOException {
		if (is == null) {
			throw new IOException();
		}
		return (short) (is.read() | is.read() * 256);
	}

	protected int readInt() throws IOException {
		if (is == null) {
			throw new IOException();
		}
		return is.read() | is.read() * 256 | is.read() * 65536 | is.read()
				* 16777216;
	}

	protected String readString() throws IOException {
		if (is == null) {
			throw new IOException();
		}
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

	public void close() {
		is = null;
	}

}
