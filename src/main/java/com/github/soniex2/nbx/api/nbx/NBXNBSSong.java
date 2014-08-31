package com.github.soniex2.nbx.api.nbx;

import com.github.soniex2.nbx.api.nbs.NBSSong;
import com.github.soniex2.nbx.api.nbx.chunk.IChunkable;
import com.github.soniex2.nbx.api.nbx.chunk.INBXChunk;
import com.github.soniex2.nbx.api.nbx.chunk.SimpleNBXChunk;
import com.github.soniex2.nbx.api.stream.nbs.NBSInputStream;
import com.github.soniex2.nbx.api.stream.nbs.NBSOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.github.soniex2.nbx.api.nbs.NBSSong.WriteLevel;

/**
 * @author soniex2
 */
public class NBXNBSSong implements IChunkable, INBXChunk {
    public NBSSong song;

    private NBSSong.WriteLevel writeLevel = NBSSong.WriteLevel.INSTRUMENTS;

    public void setDefaultWriteLevel(NBSSong.WriteLevel writeLevel) {
        if (writeLevel == null) throw new NullPointerException();
        this.writeLevel = writeLevel;
    }

    public NBSSong.WriteLevel getDefaultWriteLevel() {
        return writeLevel;
    }

    @Override
    public void fromChunk(INBXChunk chunk) {
        if (!chunk.getChunkId().equals(getChunkId())) throw new IllegalArgumentException();
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(chunk.getChunkData());
            NBSInputStream nbsInputStream = new NBSInputStream(bais);
            song = new NBSSong().read(nbsInputStream);
            nbsInputStream.close();
        } catch (IOException e) {
            // This shouldn't happen
            throw new RuntimeException(e);
        }
    }

    public INBXChunk toChunk(WriteLevel level) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            NBSOutputStream nbsOutputStream = new NBSOutputStream(baos);
            song.write(nbsOutputStream, level);
            nbsOutputStream.close();
            return new SimpleNBXChunk(getChunkId(), baos.toByteArray());
        } catch (IOException e) {
            // This shouldn't happen
            throw new RuntimeException(e);
        }
    }

    @Override
    public INBXChunk toChunk() {
        return toChunk(getDefaultWriteLevel());
    }

    @Override
    public String getChunkId() {
        return "SNBS";
    }

    @Override
    public byte[] getChunkData() {
        return toChunk().getChunkData();
    }
}
