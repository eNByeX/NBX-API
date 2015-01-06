package com.github.enbyex.nbx.api.nbx.chunk;

/**
 * @author soniex2
 */
public class SimpleNBXChunk implements INBXChunk {

    private final String id;
    private final byte[] data;

    public SimpleNBXChunk(String id, byte[] data) {
        this.id = id;
        this.data = data;
    }

    @Override
    public String getChunkId() {
        return id;
    }

    @Override
    public byte[] getChunkData() {
        return data;
    }
}
