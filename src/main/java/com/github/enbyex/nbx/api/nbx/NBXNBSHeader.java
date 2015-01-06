package com.github.enbyex.nbx.api.nbx;

import com.github.enbyex.nbx.api.helper.ChunkHelper;
import com.github.enbyex.nbx.api.helper.INBSData;
import com.github.enbyex.nbx.api.nbs.NBSHeader;
import com.github.enbyex.nbx.api.nbx.chunk.IChunkable;
import com.github.enbyex.nbx.api.nbx.chunk.INBXChunk;
import com.github.enbyex.nbx.api.nbx.chunk.SimpleNBXChunk;

/**
 * @author soniex2
 */
public class NBXNBSHeader extends NBSHeader implements IChunkable, INBXChunk, INBSData {

    public NBXNBSHeader() {
        super();
    }

    public NBXNBSHeader(NBXNBSHeader nbxnbsHeader) {
        super(nbxnbsHeader);
    }

    @Override
    public String getChunkId() {
        return "SHDR";
    }

    @Override
    public byte[] getChunkData() {
        return ChunkHelper.writeToByteArray(this);
    }

    @Override
    public void fromChunk(INBXChunk chunk) {
        ChunkHelper.readFromChunk(this, chunk, getChunkId());
    }

    @Override
    public INBXChunk toChunk() {
        return new SimpleNBXChunk(getChunkId(), getChunkData());
    }

    @Override
    public NBXNBSHeader copy() {
        return new NBXNBSHeader(this);
    }
}
