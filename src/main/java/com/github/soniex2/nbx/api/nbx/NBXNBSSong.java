package com.github.soniex2.nbx.api.nbx;

import com.github.soniex2.nbx.api.nbx.chunk.IChunkable;
import com.github.soniex2.nbx.api.nbs.NBSSong;
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

    @Override
    public void fromChunk(INBXChunk chunk) {
        if (!chunk.getId().equals("SNBS")) throw new IllegalArgumentException();
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(chunk.getData());
            NBSInputStream nbsInputStream = new NBSInputStream(bais);
            song = NBSSong.read(nbsInputStream);
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
            return new SimpleNBXChunk("SNBS", baos.toByteArray());
        } catch (IOException e) {
            // This shouldn't happen
            throw new RuntimeException(e);
        }
    }

    @Override
    public INBXChunk toChunk() {
        return toChunk(WriteLevel.INSTRUMENTS);
    }

    @Override
    public String getId() {
        return "SNBS";
    }

    @Override
    public byte[] getData() {
        return toChunk().getData();
    }
}
