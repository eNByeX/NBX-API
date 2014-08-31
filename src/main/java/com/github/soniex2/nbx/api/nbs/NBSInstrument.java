package com.github.soniex2.nbx.api.nbs;

import com.github.soniex2.nbx.api.IInstrument;
import com.github.soniex2.nbx.api.stream.nbs.INBSReader;
import com.github.soniex2.nbx.api.stream.nbs.INBSWriter;

import java.io.IOException;

public class NBSInstrument implements IInstrument, INBSData {

    private String name = "";
    private String location = "";
    private byte pitch = 45;
    private boolean press = false;

    public NBSInstrument() {
    }

    public NBSInstrument(NBSInstrument instrument) {
        setName(instrument.getName());
        setLocation(instrument.getLocation());
        setPress(instrument.getPress());
        setPitch(instrument.getPitch());
    }

    @Override
    public NBSInstrument read(INBSReader reader) throws IOException {
        setName(reader.readASCII());
        setLocation(reader.readASCII());
        setPitch(reader.readByte());
        setPress(reader.readBoolean());
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public byte getPitch() {
        return pitch;
    }

    public void setPitch(byte pitch) {
        if (pitch > 87 || pitch < 0)
            this.pitch = 45;
        else
            this.pitch = pitch;
    }

    public boolean getPress() {
        return press;
    }

    public void setPress(boolean press) {
        this.press = press;
    }

    @Override
    public NBSInstrument copy() {
        return new NBSInstrument(this);
    }

    @Override
    public void write(INBSWriter writer) throws IOException {
        writer.writeASCII(getName());
        writer.writeASCII(getLocation());
        writer.writeByte(getPitch());
        writer.writeBoolean(getPress());
    }
}
