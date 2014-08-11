package com.github.soniex2.nbx.api.stream.nbx;

import com.github.soniex2.nbx.api.INBXChunk;

import java.io.IOException;

/**
 * @author soniex2
 */
public interface INBXReader {
    public INBXChunk readChunk() throws IOException;
}
