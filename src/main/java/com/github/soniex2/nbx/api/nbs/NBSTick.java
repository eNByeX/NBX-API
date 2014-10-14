package com.github.soniex2.nbx.api.nbs;

import com.github.soniex2.nbx.api.helper.INBSData;
import com.github.soniex2.nbx.api.stream.nbs.INBSReader;
import com.github.soniex2.nbx.api.stream.nbs.INBSWriter;

import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class NBSTick implements Iterable<NBSBlock>, INBSData {

    private NBSBlock[] notes = new NBSBlock[0];
    private int modCount = 0;

    public NBSTick read(INBSReader reader, int layers) throws IOException {
        resize((short) (layers & 0x7FFF));
        short jumps;
        short layer = -1;
        while (true) {
            jumps = reader.readShort();
            if (jumps == 0) {
                break;
            }
            layer += jumps;
            NBSBlock temp = new NBSBlock();
            temp.read(reader);
            setNote(temp, layer);
        }
        return this;
    }

    @Override
    public void read(INBSReader reader) throws IOException {
        read(reader, 0);
    }

    public short getLayers() {
        return (short) notes.length;
    }

    public NBSBlock getNote(int layer) {
        return notes[layer];
    }

    public void setNote(NBSBlock note, int layer) {
        if (layer >= notes.length)
            resize((short) (layer + 1));
        notes[layer] = note;
        modCount++;
    }

    public boolean isEmpty() {
        for (NBSBlock x : notes) {
            if (x != null)
                return false;
        }
        return true;
    }

    public void resize(short layers) {
        NBSBlock[] newNotes = new NBSBlock[layers];
        System.arraycopy(notes, 0, newNotes, 0, layers < notes.length ? layers
                : notes.length);
        notes = newNotes;
        modCount++;
    }

    @Override
    public String toString() {
        String s = "[";
        for (NBSBlock note : notes) {
            s += String.valueOf(note) + ", ";
        }
        if (s.length() > 2)
            s = s.substring(0, s.length() - 2);
        s += "]";
        return s;
    }

    public NBSTick copy() {
        NBSTick t = new NBSTick();
        t.resize((short) notes.length);
        int x = 0;
        for (NBSBlock note : this) {
            t.setNote(note, x);
            x++;
        }
        return t;
    }

    @Override
    public void write(INBSWriter writer) throws IOException {
        short layers = getLayers();
        short lastLayer = -1;
        for (short y = 0; y < layers; y++) {
            NBSBlock block = getNote(y);
            if (block == null)
                continue;
            writer.writeShort(y - lastLayer);
            block.write(writer);
            lastLayer = y;
        }
        writer.writeShort(0);
    }

    @Override
    public Iterator<NBSBlock> iterator() {
        return new Iterator<NBSBlock>() {

            private int cursor = 0;
            private int last = -1;
            private int expectedModCount = modCount;

            @Override
            public boolean hasNext() {
                return cursor != notes.length;
            }

            @Override
            public NBSBlock next() {
                checkForComodification();
                try {
                    int i = cursor;
                    NBSBlock next = notes[i];
                    last = i;
                    cursor = i + 1;
                    return next;
                } catch (IndexOutOfBoundsException e) {
                    checkForComodification();
                    throw new NoSuchElementException();
                }
            }

            @Override
            public void remove() {
                checkForComodification();
                try {
                    if (last < 0)
                        throw new IllegalStateException();
                    notes[last] = null;
                    last = -1;
                } catch (IndexOutOfBoundsException e) {
                    checkForComodification();
                    throw new NoSuchElementException();
                }
            }

            public void checkForComodification() {
                if (expectedModCount != modCount)
                    throw new ConcurrentModificationException();

            }

        };
    }
}
