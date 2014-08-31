package com.github.soniex2.nbx.api.nbx;

import com.github.soniex2.nbx.api.nbs.NBSHeader;
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
public class NBXNBSHeader implements IChunkable, INBXChunk {
    public NBSHeader header;

    @Override
    public void fromChunk(INBXChunk chunk) {
        if (!chunk.getChunkId().equals(getChunkId())) throw new IllegalArgumentException();
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(chunk.getChunkData());
            NBSInputStream nbsInputStream = new NBSInputStream(bais);
            header = new NBSHeader().read(nbsInputStream);
            nbsInputStream.close();
        } catch (IOException e) {
            // This shouldn't happen
            throw new RuntimeException(e);
        }
    }

    @Override
    public INBXChunk toChunk() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            NBSOutputStream nbsOutputStream = new NBSOutputStream(baos);
            header.write(nbsOutputStream);
            nbsOutputStream.close();
            return new SimpleNBXChunk(getChunkId(), baos.toByteArray());
        } catch (IOException e) {
            // This shouldn't happen
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getChunkId() {
        return "SHDR";
    }

    @Override
    public byte[] getChunkData() {
        return toChunk().getChunkData();
    }
}
