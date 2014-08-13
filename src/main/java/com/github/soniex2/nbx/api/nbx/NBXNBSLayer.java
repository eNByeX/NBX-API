package com.github.soniex2.nbx.api.nbx;

import com.github.soniex2.nbx.api.nbx.chunk.IChunkable;
import com.github.soniex2.nbx.api.nbx.chunk.INBXChunk;

/**
 * @author soniex2
 */
public class NBXNBSLayer implements IChunkable, INBXChunk {


    @Override
    public void fromChunk(INBXChunk chunk) {

    }

    @Override
    public INBXChunk toChunk() {
        return null;
    }

    @Override
    public String getChunkId() {
        return null;
    }

    @Override
    public byte[] getChunkData() {
        return new byte[0];
    }
}
