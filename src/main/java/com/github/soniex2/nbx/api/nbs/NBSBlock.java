package com.github.soniex2.nbx.api.nbs;

import com.github.soniex2.nbx.api.stream.nbs.INBSReader;
import com.github.soniex2.nbx.api.stream.nbs.INBSWriter;

import java.io.IOException;

public final class NBSBlock implements Comparable<NBSBlock> {

    public final int inst;
    public final int note;

    public NBSBlock(int instrument, int note) {
        if (note > 87 || note < 0) {
            throw new IllegalArgumentException("Valid range for note is 0-87");
        } else if (instrument > 13 || instrument < 0) {
            throw new IllegalArgumentException(
                    "Valid range for instrument is 0-13");
        }
        inst = instrument & 0x7F;
        this.note = note & 0x7F;
    }

    public static NBSBlock read(INBSReader reader) throws IOException {
        int inst = reader.readByte();
        int key = reader.readByte();
        return new NBSBlock(inst, key);
    }

    public String toString() {
        return inst + ":" + note;
    }

    public int hashCode() {
        return (inst << 7) | note;
    }

    @Override
    public int compareTo(NBSBlock o) {
        return hashCode() - o.hashCode();
    }

    public void write(INBSWriter writer) throws IOException {
        writer.writeByte(inst);
        writer.writeByte(note);
    }

}
