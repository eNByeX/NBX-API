package com.github.soniex2.nbx.api.nbs;

import com.github.soniex2.nbx.api.stream.nbs.INBSReader;
import com.github.soniex2.nbx.api.stream.nbs.INBSWriter;

import java.io.IOException;

/**
 * @author soniex2
 */
public class NBSLayer implements INBSData {

    private String name = "";
    private byte volume = 100;

    public void setName(String name) {
        this.name = name;
    }

    public void setVolume(byte volume) {
        this.volume = (byte) (volume % 101);
    }

    public byte getVolume() {
        return volume;
    }

    public String getName() {
        return name;
    }

    @Override
    public NBSLayer read(INBSReader reader) throws IOException {
        name = reader.readASCII();
        volume = (byte) (reader.readByte() % 101);
        return this;
    }

    @Override
    public void write(INBSWriter writer) throws IOException {
        writer.writeASCII(name);
        writer.writeByte(volume);
    }

    public NBSLayer copy() {
        NBSLayer copy = new NBSLayer();
        copy.setName(getName());
        copy.setVolume(getVolume());
        return copy;
    }
}
