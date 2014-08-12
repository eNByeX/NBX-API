package com.github.soniex2.nbx.api.stream.nbx;

import com.github.soniex2.nbx.api.nbx.chunk.INBXChunk;

import java.io.IOException;

/**
 * @author soniex2
 */
public interface INBXWriter {
    public void writeChunk(INBXChunk chunk) throws IOException;

    public void writeChunk(String id, byte[] data) throws IOException;
}
