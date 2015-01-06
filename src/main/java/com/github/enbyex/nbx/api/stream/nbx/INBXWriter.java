package com.github.enbyex.nbx.api.stream.nbx;

import com.github.enbyex.nbx.api.nbx.chunk.INBXChunk;

import java.io.IOException;

/**
 * @author soniex2
 */
public interface INBXWriter {
    public void writeChunk(INBXChunk chunk) throws IOException;

    public void writeChunk(String id, byte[] data) throws IOException;
}
