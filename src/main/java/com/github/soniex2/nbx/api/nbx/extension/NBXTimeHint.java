package com.github.soniex2.nbx.api.nbx.extension;

import com.github.soniex2.nbx.api.nbx.chunk.IChunkable;
import com.github.soniex2.nbx.api.nbx.chunk.INBXChunk;
import com.github.soniex2.nbx.api.nbx.chunk.SimpleNBXChunk;

/**
 * @author soniex2
 */
// TODO
public class NBXTimeHint implements IChunkable, INBXChunk {
    /*
    So like do we want this to go like:
    [nextTick][nextNote][startOffset][nextNote][startOffset][0][nextTick][nextNote][startOffset][0][0]
    [nextTick][nextNote][duration][nextNote][duration][0][nextTick][nextNote][duration][0][0]
    OR do we want:
    [nextTick][nextNote][startOffset][duration][0][0]

    I think the former would be more flexible, but it uses more space...
     */
    
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
