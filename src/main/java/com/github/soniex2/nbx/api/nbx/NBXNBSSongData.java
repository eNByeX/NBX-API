package com.github.soniex2.nbx.api.nbx;

import com.github.soniex2.nbx.api.nbs.NBSSong.WriteLevel;
import com.github.soniex2.nbx.api.nbs.NBSSongData;
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
public class NBXNBSSongData extends AbstractChunkableChunk {
    public NBSSongData songData;

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

    @Override
    public String getChunkId() {
        return "SDAT";
    }

    @Override
    public void write(INBSWriter nbsWriter) throws IOException {
        write(nbsWriter, getDefaultWriteLevel());
    }

    public void write(INBSWriter nbsWriter, WriteLevel level) throws IOException {
        songData.write(nbsWriter, level);
    }

    @Override
    public void read(INBSReader nbsReader) throws IOException {
        NBSSongData temp = new NBSSongData();
        temp.read(nbsReader);
        songData = temp;
    }

    public byte[] getChunkData(WriteLevel level) { // can't use ChunkHelper here :/
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            NBSOutputStream nbsOutputStream = new NBSOutputStream(baos);
            songData.write(nbsOutputStream, level);
            nbsOutputStream.close();
            return baos.toByteArray();
        } catch (IOException e) {
            // This shouldn't happen
            throw new RuntimeException(e);
        }
    }
}
