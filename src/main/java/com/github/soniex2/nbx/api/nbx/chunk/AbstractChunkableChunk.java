package com.github.soniex2.nbx.api.nbx.chunk;

import com.github.soniex2.nbx.api.helper.ChunkHelper;
import com.github.soniex2.nbx.api.helper.INBSData;

/**
 * @author soniex2
 */
public abstract class AbstractChunkableChunk implements IChunkable, INBXChunk, INBSData {
    @Override
    public void fromChunk(INBXChunk chunk) {
        ChunkHelper.readFromChunk(this, chunk, getChunkId());
    }

    @Override
    public INBXChunk toChunk() {
        return new SimpleNBXChunk(getChunkId(), getChunkData());
    }

    @Override
    public byte[] getChunkData() {
        return ChunkHelper.writeToByteArray(this);
    }
}
