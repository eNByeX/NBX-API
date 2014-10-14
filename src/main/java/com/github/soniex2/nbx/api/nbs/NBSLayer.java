package com.github.soniex2.nbx.api.nbs;

import com.github.soniex2.nbx.api.helper.INBSData;
import com.github.soniex2.nbx.api.stream.nbs.INBSReader;
import com.github.soniex2.nbx.api.stream.nbs.INBSWriter;

import java.io.IOException;

/**
 * @author soniex2
 */
public class NBSLayer implements INBSData {

    private String name = "";
    private byte volume = 100;

    public NBSLayer() {
    }

    public NBSLayer(NBSLayer layer) {
        setName(layer.getName());
        setVolume(layer.getVolume());
    }

    public byte getVolume() {
        return volume;
    }

    public void setVolume(byte volume) {
        this.volume = (byte) (volume % 101);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void read(INBSReader reader) throws IOException {
        name = reader.readASCII();
        volume = (byte) (reader.readByte() % 101);
    }

    @Override
    public void write(INBSWriter writer) throws IOException {
        writer.writeASCII(name);
        writer.writeByte(volume);
    }

    // this isn't deprecated because subclasses might add extra data
    public NBSLayer copy() {
        return new NBSLayer(this);
    }
}
