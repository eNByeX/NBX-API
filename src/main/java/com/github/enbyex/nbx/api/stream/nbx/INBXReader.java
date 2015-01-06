package com.github.enbyex.nbx.api.stream.nbx;

import com.github.enbyex.nbx.api.nbx.chunk.INBXChunk;

import java.io.IOException;

/**
 * @author soniex2
 */
public interface INBXReader {
    public INBXChunk readChunk() throws IOException;
}
