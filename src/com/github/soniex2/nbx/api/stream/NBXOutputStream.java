package com.github.soniex2.nbx.api.stream;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.CRC32;

import com.github.soniex2.nbx.api.nbs.NBSHeader;
import com.github.soniex2.nbx.api.nbs.NBSSong;

public class NBXOutputStream extends FilterOutputStream {

	private static final byte[] FILE_HEADER = new byte[] { -0x7F, 'N', 'B',
			'X', 0x0D, 0x0A, 0x1A, 0x0A };

	public NBXOutputStream(OutputStream os) throws IOException {
		super(os);
		write(FILE_HEADER);
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

	/**
	 * Writes a NBX chunk to the file.
	 * 
	 * @param id
	 *            the ID - a 4-byte long ASCII string identifying this chunk
	 * @param data
	 *            the data
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public void writeChunk(String id, byte[] data) throws IOException {
		byte[] identifier = id.getBytes("US-ASCII");
		if (identifier.length != 4) {
			throw new IllegalArgumentException("ID length must match 4!");
		}
		writeInt(data.length);
		write(identifier);
		write(data);
		CRC32 crc = new CRC32();
		crc.update(identifier);
		crc.update(data);
		writeInt((int) (crc.getValue() & 0xFFFFFFFFL));
		crc.reset();
	}

	/**
	 * Writes a song to the stream, automagically adding a "SEND" chunk.
	 * 
	 * @param header
	 *            the {@link NBSHeader} for the song
	 * @param song
	 *            the {@link NBSSong} itself
	 * @see #writeSong(NBSHeader, NBSSong, boolean)
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public void writeSong(NBSHeader header, NBSSong song) throws IOException {
		writeSong(header, song, true);
	}

	/**
	 * Writes a song to the stream.
	 * 
	 * @param header
	 *            the {@link NBSHeader} for the song
	 * @param song
	 *            the {@link NBSSong} itself
	 * @param end
	 *            to add a "SEND" chunk after the data
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public void writeSong(NBSHeader header, NBSSong song, boolean end)
			throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		NBSOutputStream os = new NBSOutputStream(baos);
		os.writeHeader(header);
		os.writeSong(song);
		writeChunk("SDAT", baos.toByteArray());
		os.close();
		if (end)
			writeChunk("SEND", new byte[0]);
	}
}
