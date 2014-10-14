package com.github.soniex2.nbx.api.helper;

import com.github.soniex2.nbx.api.nbx.chunk.INBXChunk;
import com.github.soniex2.nbx.api.stream.nbs.NBSInputStream;
import com.github.soniex2.nbx.api.stream.nbs.NBSOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * THIS IS THE CLASS THAT MAKES ALL CODE LOOK CLEAN!
 *
 * @author soniex2
 */
public class ChunkHelper {

    public static byte[] writeToByteArray(INBSData object) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            NBSOutputStream nbsOutputStream = new NBSOutputStream(baos);
            object.write(nbsOutputStream);
            nbsOutputStream.close();
            return baos.toByteArray();
        } catch (IOException e) {
            // This shouldn't happen
            throw new RuntimeException(e);
        }
    }

    public static <T extends INBSData> T readFromByteArray(T object, byte[] bytes) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            NBSInputStream nbsInputStream = new NBSInputStream(bais);
            object.read(nbsInputStream);
            nbsInputStream.close();
            return object;
        } catch (IOException e) {
            // This shouldn't happen
            throw new RuntimeException(e);
        }
    }

    public static <T extends INBSData> T readFromChunk(T object, INBXChunk chunk, String chunkId) {
        if (!chunk.getChunkId().equals(chunkId)) throw new IllegalArgumentException();
        return readFromByteArray(object, chunk.getChunkData());
    }
}
