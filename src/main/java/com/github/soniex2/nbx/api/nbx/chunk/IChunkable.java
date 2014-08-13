package com.github.soniex2.nbx.api.nbx.chunk;

import com.github.soniex2.nbx.api.nbx.chunk.INBXChunk;

/**
 * @author soniex2
 */
public interface IChunkable {
    void fromChunk(INBXChunk chunk);

    INBXChunk toChunk();
}
