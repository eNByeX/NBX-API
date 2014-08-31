package com.github.soniex2.nbx.api.nbx;

import com.github.soniex2.nbx.api.nbs.NBSLayer;
import com.github.soniex2.nbx.api.nbx.chunk.IChunkable;
import com.github.soniex2.nbx.api.nbx.chunk.INBXChunk;
import com.github.soniex2.nbx.api.nbx.chunk.SimpleNBXChunk;
import com.github.soniex2.nbx.api.stream.nbs.NBSInputStream;
import com.github.soniex2.nbx.api.stream.nbs.NBSOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author soniex2
 */
public class NBXNBSLayer extends NBSLayer implements IChunkable, INBXChunk {
    private short id;

    public NBXNBSLayer() {
    }

    public NBXNBSLayer(NBSLayer layer) {
        super(layer);
    }

    @Override
    public void fromChunk(INBXChunk chunk) {
        if (!chunk.getChunkId().equals(getChunkId())) throw new IllegalArgumentException();
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(chunk.getChunkData());
            NBSInputStream nbsInputStream = new NBSInputStream(bais);
            id = nbsInputStream.readShort();
            read(nbsInputStream);
            nbsInputStream.close();
        } catch (IOException e) {
            // This shouldn't happen
            throw new RuntimeException(e);
        }
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
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            NBSOutputStream nbsOutputStream = new NBSOutputStream(baos);
            nbsOutputStream.writeShort(id);
            write(nbsOutputStream);
            nbsOutputStream.close();
            return baos.toByteArray();
        } catch (IOException e) {
            // This shouldn't happen
            throw new RuntimeException(e);
        }
    }

    @Override
    @Deprecated
    public NBXNBSLayer copy() {
        return new NBXNBSLayer(this);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = (short) (id & 0x7FFF);
    }
}
