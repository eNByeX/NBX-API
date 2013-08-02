package com.github.soniex2.nbx.api;

import java.io.DataInput;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UTFDataFormatException;

public class NBSInputStream extends FilterInputStream implements DataInput {

	private NBSHeader header;
	private NBSSong song;

	public NBSInputStream(InputStream is) throws IOException {
		super(is);
		header = new NBSHeader(readShort(), readShort(), readASCII(),
				readASCII(), readASCII(), readASCII(), readShort(),
				readBoolean(), readByte(), readByte(), readInt(), readInt(),
				readInt(), readInt(), readInt(), readASCII());
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
			song.setLayerName(i, readASCII());
			song.setLayerVolume(i, readByte());
		}
		if (available() == 0)
			return;
		byte count = readByte();
		for (byte i = 0; i < count; i++) {
			// TODO add custom instrument support
			String name = readASCII();
			String file = readASCII();
			byte pitch = readByte();
			boolean play = readByte() == 1;
		}
	}

	@Override
	public byte readByte() throws IOException {
		int i = read();
		if (i < 0)
			throw new EOFException();
		return (byte) i;
	}

	/**
	 * Reads two input bytes and returns a <code>short</code> value. Let
	 * <code>a</code> be the first byte read and <code>b</code> be the second
	 * byte. The value returned is:
	 * <p>
	 * 
	 * <pre>
	 * <code>(short) ((a &amp; 0xFF) | (b &amp; 0xFF) &lt;&lt; 8)</code>
	 * </pre>
	 * 
	 * </p>
	 * This method is suitable for reading the bytes written by the
	 * <code>writeShort</code> method of <code>NBSOutputStream</code>.
	 * 
	 * @return the 16-bit value read.
	 * @exception EOFException
	 *                if this stream reaches the end before reading all the
	 *                bytes.
	 * @exception IOException
	 *                if an I/O error occurs.
	 */
	@Override
	public short readShort() throws IOException {
		return (short) (readUnsignedByte() | readUnsignedByte() << 8);
	}

	/**
	 * Reads four input bytes and returns an <code>int</code> value. Let
	 * <code>a-d</code> be the first through fourth bytes read. The value
	 * returned is:
	 * <p>
	 * 
	 * <pre>
	 * <code>((a &amp; 0xFF) | (b &amp; 0xFF) &lt;&lt; 8 | (c &amp; 0xFF) &lt;&lt; 16 | (d &amp; 0xFF) &lt;&lt; 24)</code>
	 * </pre>
	 * 
	 * </p>
	 * This method is suitable for reading bytes written by the
	 * <code>writeInt</code> method of <code>NBSOutputStream</code>.
	 * 
	 * @return the <code>int</code> value read.
	 * @exception EOFException
	 *                if this stream reaches the end before reading all the
	 *                bytes.
	 * @exception IOException
	 *                if an I/O error occurs.
	 */
	@Override
	public int readInt() throws IOException {
		return readUnsignedShort() | readUnsignedShort() << 16;
	}

	/**
	 * Reads two input bytes and returns a <code>char</code> value. Let
	 * <code>a</code> be the first byte read and <code>b</code> be the second
	 * byte. The value returned is:
	 * <p>
	 * 
	 * <pre>
	 * <code>(char) ((a &amp; 0xFF) &lt;&lt; 8 | (b &amp; 0xFF))</code>
	 * </pre>
	 * 
	 * </p>
	 * This method is suitable for reading bytes written by the
	 * <code>writeChar</code> method of interface <code>DataOutput</code>.
	 * 
	 * @return the <code>char</code> value read.
	 * @exception EOFException
	 *                if this stream reaches the end before reading all the
	 *                bytes.
	 * @exception IOException
	 *                if an I/O error occurs.
	 */
	@Override
	public char readChar() throws IOException {
		return (char) (readUnsignedByte() << 8 | readUnsignedByte());
	}

	/**
	 * Reads eight input bytes and returns a <code>long</code> value. Let
	 * <code>a-h</code> be the first through eighth bytes read. The value
	 * returned is:
	 * <p>
	 * 
	 * <pre>
	 * <code>
	 * ((long) ((a &amp; 0xFF) | (b &amp; 0xFF) &lt;&lt; 8 | (c &amp; 0xFF) &lt;&lt; 16 | (d &amp; 0xFF) &lt;&lt; 24)) & 0xFFFFFFFFL |
	 * (((long) ((a &amp; 0xFF) | (b &amp; 0xFF) &lt;&lt; 8 | (c &amp; 0xFF) &lt;&lt; 16 | (d &amp; 0xFF) &lt;&lt; 24)) & 0xFFFFFFFFL) << 32
	 * </code>
	 * </pre>
	 * 
	 * </p>
	 * This method is suitable for reading bytes written by the
	 * <code>writeLong</code> method of <code>NBSOutputStream</code>.
	 * 
	 * @return the <code>long</code> value read.
	 * @exception EOFException
	 *                if this stream reaches the end before reading all the
	 *                bytes.
	 * @exception IOException
	 *                if an I/O error occurs.
	 */
	@Override
	public long readLong() throws IOException {
		return ((long) readInt() & 0xFFFFFFFFL)
				| ((long) readInt() & 0xFFFFFFFFL) << 32;
	}

	@Override
	public boolean readBoolean() throws IOException {
		return readByte() != 0;
	}

	@Override
	public int readUnsignedByte() throws IOException {
		return readByte() & 0xFF;
	}

	/**
	 * Reads two input bytes and returns an <code>int</code> value in the range
	 * <code>0</code> through <code>65535</code>. Let <code>a</code> be the
	 * first byte read and <code>b</code> be the second byte. The value returned
	 * is:
	 * <p>
	 * 
	 * <pre>
	 * <code>(a &amp; 0xFF) | (b &amp; 0xFF) &lt;&lt; 8
	 * </code>
	 * </pre>
	 * 
	 * </p>
	 * This method is suitable for reading the bytes written by the
	 * <code>writeShort</code> method of <code>NBSOutputStream</code> if the
	 * argument to <code>writeShort</code> was intended to be a value in the
	 * range <code>0</code> through <code>65535</code>.
	 * 
	 * @return the unsigned 16-bit value read.
	 * @exception EOFException
	 *                if this stream reaches the end before reading all the
	 *                bytes.
	 * @exception IOException
	 *                if an I/O error occurs.
	 */
	@Override
	public int readUnsignedShort() throws IOException {
		return readShort() & 0xFFFF;
	}

	public String readASCII() throws IOException {
		byte[] b = new byte[readInt()];
		if (read(b) != b.length) {
			throw new EOFException();
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
	public void readFully(byte[] b) throws IOException {
		readFully(b, 0, b.length);
	}

	@Override
	public void readFully(byte[] b, int off, int len) throws IOException {
		if (len < 0)
			throw new IndexOutOfBoundsException();
		int n = 0;
		while (n < len) {
			int count = in.read(b, off + n, len - n);
			if (count < 0)
				throw new EOFException();
			n += count;
		}
	}

	@Override
	public int skipBytes(int n) throws IOException {
		int total = 0;
		int cur = 0;
		while ((total < n) && ((cur = (int) in.skip(n - total)) > 0)) {
			total += cur;
		}
		return total;
	}

	@Override
	public float readFloat() throws IOException {
		return Float.intBitsToFloat(readInt());
	}

	@Override
	public double readDouble() throws IOException {
		return Double.longBitsToDouble(readLong());
	}

	/**
	 * This method throws an IOException, you shouldn't try to read a text line
	 * from a NBS file...
	 * 
	 * @return never returns.
	 * @exception IOException
	 *                if you call it.
	 */
	@Override
	public String readLine() throws IOException {
		throw new IOException();
	}

	private byte bytearr[] = new byte[80];
	private char chararr[] = new char[80];

	@Override
	public String readUTF() throws IOException {
		int utflen = readInt();
		byte[] bytearr = null;
		char[] chararr = null;
		if (this.bytearr.length < utflen) {
			this.bytearr = new byte[utflen * 2];
			this.chararr = new char[utflen * 2];
		}
		chararr = this.chararr;
		bytearr = this.bytearr;
		int c, char2, char3;
		int count = 0;
		int chararr_count = 0;
		readFully(bytearr, 0, utflen);
		while (count < utflen) {
			c = (int) bytearr[count] & 0xff;
			if (c > 127)
				break;
			count++;
			chararr[chararr_count++] = (char) c;
		}
		while (count < utflen) {
			c = (int) bytearr[count] & 0xff;
			switch (c >> 4) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
				/* 0xxxxxxx */
				count++;
				chararr[chararr_count++] = (char) c;
				break;
			case 12:
			case 13:
				/* 110x xxxx 10xx xxxx */
				count += 2;
				if (count > utflen)
					throw new UTFDataFormatException(
							"malformed input: partial character at end");
				char2 = (int) bytearr[count - 1];
				if ((char2 & 0xC0) != 0x80)
					throw new UTFDataFormatException(
							"malformed input around byte " + count);
				chararr[chararr_count++] = (char) (((c & 0x1F) << 6) | (char2 & 0x3F));
				break;
			case 14:
				/* 1110 xxxx 10xx xxxx 10xx xxxx */
				count += 3;
				if (count > utflen)
					throw new UTFDataFormatException(
							"malformed input: partial character at end");
				char2 = (int) bytearr[count - 2];
				char3 = (int) bytearr[count - 1];
				if (((char2 & 0xC0) != 0x80) || ((char3 & 0xC0) != 0x80))
					throw new UTFDataFormatException(
							"malformed input around byte " + (count - 1));
				chararr[chararr_count++] = (char) (((c & 0x0F) << 12)
						| ((char2 & 0x3F) << 6) | ((char3 & 0x3F) << 0));
				break;
			default:
				/* 10xx xxxx, 1111 xxxx */
				throw new UTFDataFormatException("malformed input around byte "
						+ count);
			}
		}
		// The number of chars produced may be less than utflen
		return new String(chararr, 0, chararr_count);
	}

}
