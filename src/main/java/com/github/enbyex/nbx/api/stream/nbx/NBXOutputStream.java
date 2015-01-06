package com.github.enbyex.nbx.api.stream.nbx;

import com.github.enbyex.nbx.api.nbx.chunk.INBXChunk;
import com.github.enbyex.nbx.api.stream.LittleEndianDataOutputStream;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.CRC32;

public class NBXOutputStream extends LittleEndianDataOutputStream implements INBXWriter {

    private static final byte[] FILE_HEADER = new byte[]{-0x7F, 'N', 'B',
            'X', 0x0D, 0x0A, 0x1A, 0x0A};

    public NBXOutputStream(OutputStream os) throws IOException {
        super(os);
        write(FILE_HEADER);
    }

    /**
     * Writes an INBXChunk to the file
     *
     * @param chunk the INBXChunk
     * @throws IOException if an I/O error occurs.
     */
    public void writeChunk(INBXChunk chunk) throws IOException {
        writeChunk(chunk.getChunkId(), chunk.getChunkData());
    }

    /**
     * Writes a NBX chunk to the file.
     *
     * @param id   the ID - a 4-byte long ASCII string identifying this chunk
     * @param data the data
     * @throws IOException if an I/O error occurs.
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
}
