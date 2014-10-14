package com.github.soniex2.nbx.api.nbx;

import com.github.soniex2.nbx.api.nbs.NBSSong;
import com.github.soniex2.nbx.api.nbs.NBSSong.WriteLevel;
import com.github.soniex2.nbx.api.nbx.chunk.AbstractChunkableChunk;
import com.github.soniex2.nbx.api.nbx.chunk.INBXChunk;
import com.github.soniex2.nbx.api.nbx.chunk.SimpleNBXChunk;
import com.github.soniex2.nbx.api.stream.nbs.INBSReader;
import com.github.soniex2.nbx.api.stream.nbs.INBSWriter;
import com.github.soniex2.nbx.api.stream.nbs.NBSOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author soniex2
 */
public class NBXNBSSong extends AbstractChunkableChunk {
    public NBSSong song;

    private WriteLevel writeLevel = WriteLevel.INSTRUMENTS;

    public WriteLevel getDefaultWriteLevel() {
        return writeLevel;
    }

    public void setDefaultWriteLevel(WriteLevel writeLevel) {
        if (writeLevel == null) throw new NullPointerException();
        this.writeLevel = writeLevel;
    }

    public INBXChunk toChunk(WriteLevel level) {
        return new SimpleNBXChunk(getChunkId(), getChunkData(level));
    }

    public byte[] getChunkData(WriteLevel level) { // can't use ChunkHelper here :/
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            NBSOutputStream nbsOutputStream = new NBSOutputStream(baos);
            write(nbsOutputStream, level);
            nbsOutputStream.close();
            return baos.toByteArray();
        } catch (IOException e) {
            // This shouldn't happen
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getChunkId() {
        return "SNBS";
    }

    @Override
    public void write(INBSWriter nbsWriter) throws IOException {
        write(nbsWriter, getDefaultWriteLevel());
    }

    @Override
    public void read(INBSReader nbsReader) throws IOException {
        NBSSong temp = new NBSSong();
        temp.read(nbsReader);
        song = temp;
    }

    public void write(INBSWriter nbsWriter, WriteLevel level) throws IOException {
        song.write(nbsWriter, level);
    }


}
