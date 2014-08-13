package com.github.soniex2.nbx.api.nbx;

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
public class NBXSong {
    public NBSSong song;

    public NBXSong(NBSSong song) {
        this.song = song;
    }

    public static NBXSong fromChunk(INBXChunk chunk) {
        if (!chunk.getId().equals("SDAT")) throw new IllegalArgumentException();
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(chunk.getData());
            NBSInputStream nbsInputStream = new NBSInputStream(bais);
            NBSSong song = NBSSong.read(nbsInputStream);
            nbsInputStream.close();
            return new NBXSong(song);
        } catch (IOException e) {
            // This shouldn't happen
            throw new RuntimeException(e);
        }
    }

    public INBXChunk toChunk() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            NBSOutputStream nbsOutputStream = new NBSOutputStream(baos);
            song.write(nbsOutputStream, WriteLevel.INSTRUMENTS);
            nbsOutputStream.close();
            return new SimpleNBXChunk("SDAT", baos.toByteArray());
        } catch (IOException e) {
            // This shouldn't happen
            throw new RuntimeException(e);
        }
    }
}
