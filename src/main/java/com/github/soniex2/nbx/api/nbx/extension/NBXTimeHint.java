package com.github.soniex2.nbx.api.nbx.extension;

import com.github.soniex2.nbx.api.nbx.chunk.IChunkable;
import com.github.soniex2.nbx.api.nbx.chunk.INBXChunk;
import com.github.soniex2.nbx.api.nbx.chunk.SimpleNBXChunk;

/**
 * @author soniex2
 */
// TODO
public class NBXTimeHint implements IChunkable, INBXChunk {
    @Override
    public void fromChunk(INBXChunk chunk) {

    }

    @Override
    public INBXChunk toChunk() {
        return new SimpleNBXChunk(getChunkId(), getChunkData());
    }

    @Override
    public String getChunkId() {
        // [s]ong [N]ote [T]ime [H]int
        return "sNTH";
    }

    @Override
    public byte[] getChunkData() {
        return new byte[0];
    }
}
