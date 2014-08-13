package com.github.soniex2.nbx.api.nbs;

import com.github.soniex2.nbx.api.IInstrument;
import com.github.soniex2.nbx.api.stream.nbs.INBSReader;
import com.github.soniex2.nbx.api.stream.nbs.INBSWriter;

import java.io.IOException;

public class NBSInstrument implements IInstrument {

    private String name;
    private String location;
    private byte pitch;
    private boolean press;

    /**
     * Constructs a new NBSInstrument.
     *
     * @param name     The name of the instrument.
     * @param location The sound file of the instrument (just the filename, not the
     *                 path). (On MCNBS, this is relative to the Sounds directory)
     * @param pitch    The pitch of the sound file. Just like the note blocks, this
     *                 ranges from 0-87. Default is 45.
     * @param press    Whether the piano should automatically press keys with this
     *                 instrument when the marker passes them.
     */
    public NBSInstrument(String name, String location, byte pitch, boolean press) {
        this.name = name;
        this.location = location;
        if (pitch > 87 || pitch < 0)
            this.pitch = 45;
        else
            this.pitch = pitch;
        this.press = press;
    }

    public static NBSInstrument read(INBSReader reader) throws IOException {
        String name = reader.readASCII();
        String file = reader.readASCII();
        byte pitch = reader.readByte();
        boolean play = reader.readBoolean();
        return new NBSInstrument(name, file, pitch, play);
    }

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

    public NBSInstrument copy() {
        return new NBSInstrument(name, location, pitch, press);
    }

    public void write(INBSWriter writer) throws IOException {
        writer.writeASCII(getName());
        writer.writeASCII(getLocation());
        writer.writeByte(getPitch());
        writer.writeBoolean(getPress());
    }
}
