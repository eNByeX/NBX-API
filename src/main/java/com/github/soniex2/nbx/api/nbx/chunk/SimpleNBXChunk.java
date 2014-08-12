package com.github.soniex2.nbx.api.nbx.chunk;

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
    public String getId() {
        return id;
    }

    @Override
    public byte[] getData() {
        return data;
    }
}
