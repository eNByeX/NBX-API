package com.github.soniex2.nbx.api.nbx;

import com.github.soniex2.nbx.api.helper.ChunkHelper;
import com.github.soniex2.nbx.api.helper.INBSData;
import com.github.soniex2.nbx.api.nbs.NBSLayer;
import com.github.soniex2.nbx.api.nbx.chunk.IChunkable;
import com.github.soniex2.nbx.api.nbx.chunk.INBXChunk;
import com.github.soniex2.nbx.api.nbx.chunk.SimpleNBXChunk;
import com.github.soniex2.nbx.api.stream.nbs.INBSReader;
import com.github.soniex2.nbx.api.stream.nbs.INBSWriter;

import java.io.IOException;

/**
 * @author soniex2
 */
public class NBXNBSLayer extends NBSLayer implements IChunkable, INBXChunk, INBSData {
    private short id;

    public NBXNBSLayer() {
    }

    public NBXNBSLayer(NBSLayer layer) {
        super(layer);
    }

    public NBXNBSLayer(NBSLayer layer, short id) {
        this(layer);
        this.id = id;
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
    public String getChunkId() {
        return "SLAY";
    }

    @Override
    public byte[] getChunkData() {
        return ChunkHelper.writeToByteArray(this);
    }

    @Override
    public NBXNBSLayer copy() {
        return new NBXNBSLayer(this, id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = (short) (id & 0x7FFF);
    }

    @Override
    public void read(INBSReader nbsReader) throws IOException {
        id = nbsReader.readShort();
        super.read(nbsReader);
    }

    @Override
    public void write(INBSWriter nbsWriter) throws IOException {
        nbsWriter.writeShort(id);
        super.write(nbsWriter);
    }
}
