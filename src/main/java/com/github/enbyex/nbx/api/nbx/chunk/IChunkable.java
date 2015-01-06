package com.github.enbyex.nbx.api.nbx.chunk;

/**
 * @author soniex2
 */
public interface IChunkable {
    void fromChunk(INBXChunk chunk);

    INBXChunk toChunk();
}
