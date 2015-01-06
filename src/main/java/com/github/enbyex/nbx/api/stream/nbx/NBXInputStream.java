package com.github.enbyex.nbx.api.stream.nbx;

import com.github.enbyex.nbx.api.nbx.chunk.INBXChunk;
import com.github.enbyex.nbx.api.nbx.chunk.SimpleNBXChunk;
import com.github.enbyex.nbx.api.stream.LittleEndianDataInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;

/**
 * @author soniex2
 */
public class NBXInputStream extends LittleEndianDataInputStream implements INBXReader {
    private static final byte[] FILE_HEADER = new byte[]{-0x7F, 'N', 'B',
            'X', 0x0D, 0x0A, 0x1A, 0x0A};

    public NBXInputStream(InputStream in) throws IOException {
        super(in);
        byte[] b = new byte[FILE_HEADER.length];
        if (in.read(b) < FILE_HEADER.length) {
            throw new IOException(); // whatever
        }
        for (int i = 0; i < FILE_HEADER.length; i++) {
            if (b[i] != FILE_HEADER[i]) {
                throw new IOException();
            }
        }
    }

    public INBXChunk readChunk() throws IOException {
        final int dataLen = readInt();
        final byte[] idBytes = new byte[4];
        if (read(idBytes) < 4) {
            throw new IOException();
        }
        final String id = new String(idBytes, "US-ASCII");
        final byte[] data = new byte[dataLen];
        if (read(data) < dataLen) {
            throw new IOException();
        }
        CRC32 crc = new CRC32();
        crc.update(idBytes);
        crc.update(data);
        if ((int) (crc.getValue() & 0xFFFFFFFFL) != readInt()) {
            throw new IOException();
        }

        return new SimpleNBXChunk(id, data);
    }
}
