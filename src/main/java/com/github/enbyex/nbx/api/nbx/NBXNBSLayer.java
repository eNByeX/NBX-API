package com.github.enbyex.nbx.api.nbx;

import com.github.enbyex.nbx.api.helper.ChunkHelper;
import com.github.enbyex.nbx.api.nbs.NBSLayer;
import com.github.enbyex.nbx.api.nbx.chunk.INBXChunk;
import com.github.enbyex.nbx.api.stream.nbs.INBSWriter;
import com.github.enbyex.nbx.api.helper.INBSData;
import com.github.enbyex.nbx.api.nbx.chunk.IChunkable;
import com.github.enbyex.nbx.api.nbx.chunk.SimpleNBXChunk;
import com.github.enbyex.nbx.api.stream.nbs.INBSReader;

import java.io.IOException;

/**
 * @author soniex2
 */
public class NBXNBSLayer extends NBSLayer implements IChunkable, INBXChunk, INBSData {
    private short id;

    private byte pan;

    public void setPan(byte pan) {
        this.pan = pan;
    }

    public byte getPan() {
        return pan;
    }

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
        setId(nbsReader.readShort());
        super.read(nbsReader);
        setPan(nbsReader.readByte());
    }

    @Override
    public void write(INBSWriter nbsWriter) throws IOException {
        nbsWriter.writeShort(getId());
        super.write(nbsWriter);
        nbsWriter.writeByte(getPan());
    }
}
