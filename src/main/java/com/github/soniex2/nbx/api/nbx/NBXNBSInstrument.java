package com.github.soniex2.nbx.api.nbx;

import com.github.soniex2.nbx.api.helper.ChunkHelper;
import com.github.soniex2.nbx.api.helper.INBSData;
import com.github.soniex2.nbx.api.nbs.NBSInstrument;
import com.github.soniex2.nbx.api.nbx.chunk.IChunkable;
import com.github.soniex2.nbx.api.nbx.chunk.INBXChunk;
import com.github.soniex2.nbx.api.nbx.chunk.SimpleNBXChunk;
import com.github.soniex2.nbx.api.stream.nbs.INBSReader;
import com.github.soniex2.nbx.api.stream.nbs.INBSWriter;

import java.io.IOException;

/**
 * @author soniex2
 */
// We extend NBSInstrument so this can be easily converted from/to a NBS file. Because of that, we can't override read(INBSReader)/write(INBSWriter).
// We extend IWriteable/IReadable to avoid boilerplate
public class NBXNBSInstrument extends NBSInstrument implements IChunkable, INBXChunk, INBXInstrument, INBSData {
    private int id;

    @Override
    public void fromChunk(INBXChunk chunk) {
        ChunkHelper.readFromChunk(this, chunk, getChunkId());
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
        return ChunkHelper.writeToByteArray(this);
    }

    @Override
    public void read(INBSReader nbsReader) throws IOException {
        id = nbsReader.readByte();
        super.read(nbsReader);
    }

    @Override
    public void write(INBSWriter nbsWriter) throws IOException {
        nbsWriter.writeByte(id);
        super.write(nbsWriter);
    }
}
