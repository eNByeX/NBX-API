package com.github.soniex2.nbx.api.nbx;

import com.github.soniex2.nbx.api.nbs.NBSInstrument;
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
// We extend NBSInstrument so this can be easily converted from/to a NBS file. Because of that, we can't override read(INBSReader)/write(INBSWriter).
public class NBXNBSInstrument extends NBSInstrument implements IChunkable, INBXChunk, INBXInstrument {
    private int id;

    @Override
    public void fromChunk(INBXChunk chunk) {
        if (!chunk.getChunkId().equals(getChunkId())) throw new IllegalArgumentException();
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(chunk.getChunkData());
            NBSInputStream nbsInputStream = new NBSInputStream(bais);
            id = nbsInputStream.readByte();
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
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id % 9;
    }

    @Override
    public String getChunkId() {
        return "SINS";
    }

    @Override
    public byte[] getChunkData() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            NBSOutputStream nbsOutputStream = new NBSOutputStream(baos);
            nbsOutputStream.writeByte(id);
            write(nbsOutputStream);
            nbsOutputStream.close();
            return baos.toByteArray();
        } catch (IOException e) {
            // This shouldn't happen
            throw new RuntimeException(e);
        }
    }
}
