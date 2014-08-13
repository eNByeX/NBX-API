package com.github.soniex2.nbx.api.nbs;

import com.github.soniex2.nbx.api.stream.nbs.INBSReader;
import com.github.soniex2.nbx.api.stream.nbs.INBSWriter;

import java.io.IOException;

public class NBSBlock implements Comparable<NBSBlock>, INBSData {

    private int inst;
    private int note;

    public void setInst(int inst) {
        this.inst = inst % 14;
    }

    public void setNote(int note) {
        this.note = note % 88;
    }

    public int getInst() {
        return inst;
    }

    public int getNote() {
        return note;
    }

    @Override
    public NBSBlock read(INBSReader reader) throws IOException {
        inst = reader.readByte();
        note = reader.readByte();
        return this;
    }

    @Override
    public String toString() {
        return inst + ":" + note;
    }

    @Override
    public int hashCode() {
        return (inst << 7) | note;
    }

    @Override
    public int compareTo(NBSBlock o) {
        return hashCode() - o.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof NBSBlock) {
            NBSBlock another = (NBSBlock) o;
            return hashCode() == another.hashCode();
        }
        return false;
    }

    @Override
    public void write(INBSWriter writer) throws IOException {
        writer.writeByte(inst);
        writer.writeByte(note);
    }

    public NBSBlock copy() {
        NBSBlock copy = new NBSBlock();
        copy.setInst(inst);
        copy.setNote(note);
        return copy;
    }

}
