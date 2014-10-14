package com.github.soniex2.nbx.api.nbs;

import com.github.soniex2.nbx.api.IInstrument;
import com.github.soniex2.nbx.api.helper.INBSData;
import com.github.soniex2.nbx.api.stream.nbs.INBSReader;
import com.github.soniex2.nbx.api.stream.nbs.INBSWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.github.soniex2.nbx.api.nbs.NBSSong.WriteLevel;

public class NBSSongData implements Iterable<NBSTick>, INBSData {

    private ArrayList<NBSTick> song = new ArrayList<NBSTick>();
    private short layers = 0;
    private NBSLayer[] layerMetadata = new NBSLayer[0];
    private int modCount = 0;
    private short marker = 0;
    private IInstrument[] customInstruments = new IInstrument[9];

    @Override
    public void read(INBSReader reader) throws IOException {
        // use a dummy header, it doesn't care. (but you won't get layer info)
        read(reader, new NBSHeader());
    }

    public NBSSongData read(INBSReader reader, NBSHeader header) throws IOException {
        //NBSSongData song = new NBSSongData(header.getTicks(), header.getLayers());
        // START part 2 - song data
        short tick = -1;
        short jumps;
        while (true) {
            jumps = reader.readShort();
            if (jumps == 0) {
                break;
            }
            tick += jumps;
            addTick(tick, new NBSTick().read(reader, getLayers()));
        }
        // END part 2 - song data
        try {
            // START part 3 & 4 - optional metadata
            // START part 3 - (optional) layer data
            for (short i = 0; i < header.getLayers(); i++) {
                setLayerName(i, reader.readASCII());
                setLayerVolume(i, reader.readByte());
            }
            // END part 3
            // START part 4 - (optional) custom instrument data
            int a = reader.readByte();
            for (byte i = 0; i < a; i++) {
                NBSInstrument temp = new NBSInstrument();
                temp.read(reader);
                setCustomInstrument(i, temp);
            }
            // END part 4
            // END part 3 & 4
        } catch (IOException e) {
            //return song;
        }
        return this;
    }

    public short getTicks() {
        return (short) song.size();
    }

    /**
     * Returns the song length, in milliseconds.
     */
    public long getLength(NBSHeader header) {
        double x = (double) getTicks() / ((double) header.getTempo() / 100.0);
        x -= 1.0 / ((double) header.getTempo() / 100.0);
        x *= 1000.0;
        return (int) x;
    }

    public short getLayers() {
        return layers;
    }

    public void addTick(NBSTick tick) {
        addTick(song.size(), tick);
    }

    public void addTick(int index, NBSTick tick) {
        if (tick.getLayers() < layers) {
            tick.resize(layers);
        } else if (tick.getLayers() > layers) {
            resize(tick.getLayers());
        }
        if (index > Short.MAX_VALUE) {
            throw new IllegalArgumentException("Too many ticks!");
        }
        if (song.size() >= Short.MAX_VALUE) {
            throw new IllegalStateException("Too many ticks!");
        }
        while (index > song.size()) {
            NBSTick newTick = new NBSTick();
            newTick.resize(layers);
            song.add(newTick);
        }
        song.add(index, tick);
        modCount++;
    }

    public void delTick(short index) {
        song.remove(index);
        modCount++;
    }

    public void resize(short layers) {
        if (layers == this.layers)
            return;
        Iterator<NBSTick> iterator = song.iterator();
        while (iterator.hasNext()) {
            iterator.next().resize(layers);
        }
        NBSLayer[] newLayerMetadata = new NBSLayer[layers];
        System.arraycopy(layerMetadata, 0, newLayerMetadata, 0,
                layers < layerMetadata.length ? layers : layerMetadata.length);
        for (int x = layerMetadata.length; x < newLayerMetadata.length; x++) {
            newLayerMetadata[x] = new NBSLayer();
        }
        layerMetadata = newLayerMetadata;
        modCount++;
        this.layers = layers;
    }

    public NBSTick getTick(short index) {
        return song.get(index);
    }

    public NBSTick getCurrentTick() {
        return song.get(marker);
    }

    public String getLayerName(short layer) {
        return layerMetadata[layer].getName();
    }

    public byte getLayerVolume(short layer) {
        return layerMetadata[layer].getVolume();
    }

    public void setLayerName(short layer, String name) {
        layerMetadata[layer].setName(name);
    }

    public void setLayerVolume(short layer, byte volume) {
        layerMetadata[layer].setVolume(volume);
    }

    public void moveMarker(short tick) {
        if (tick < 0 || tick >= song.size())
            throw new IllegalArgumentException("Invalid position");
        marker = tick;
    }

    public short getMarker() {
        return marker;
    }

    public NBSSongData copy() {
        NBSSongData newSong = new NBSSongData();
        newSong.resize(layers);
        newSong.song.ensureCapacity(song.size());
        int x = 0;
        for (NBSTick t : this) {
            newSong.addTick(x, t.copy());
            x++;
        }
        for (x = 0; x < layerMetadata.length; x++) {
            newSong.layerMetadata[x] = layerMetadata[x].copy();
        }
        for (x = 0; x < 9; x++) {
            newSong.customInstruments[x] = customInstruments[x] != null ? customInstruments[x]
                    .copy() : null;
        }
        return newSong;
    }

    @Override
    public Iterator<NBSTick> iterator() {
        return new Iterator<NBSTick>() {

            private int cursor = 0;
            private int expectedModCount = modCount;

            @Override
            public boolean hasNext() {
                return cursor != song.size();
            }

            @Override
            public NBSTick next() {
                checkForComodification();
                try {
                    int i = cursor;
                    NBSTick next = song.get(i);
                    cursor = i + 1;
                    return next;
                } catch (IndexOutOfBoundsException e) {
                    checkForComodification();
                    throw new NoSuchElementException();
                }
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

            public void checkForComodification() {
                if (expectedModCount != modCount)
                    throw new ConcurrentModificationException();

            }

        };
    }

    public void setCustomInstrument(byte id, IInstrument instrument) {
        customInstruments[id] = instrument;
    }

    public IInstrument getCustomInstrument(byte id) {
        return customInstruments[id];
    }

    @Override
    public void write(INBSWriter writer) throws IOException {
        write(writer, WriteLevel.INSTRUMENTS);
    }

    public void write(INBSWriter writer, WriteLevel level) throws IOException {
        short songTicks = getTicks();
        short lastTick = -1;
        for (short x = 0; x < songTicks; x++) {
            NBSTick tick = getTick(x);
            if (tick.isEmpty())
                continue;
            writer.writeShort(x - lastTick);
            lastTick = x;
            tick.write(writer);
        }
        writer.writeShort(0);
        if (level.compareTo(WriteLevel.LAYERS) >= 0) {
            for (short x = 0; x < getLayers(); x++) {
                if (getLayerName(x) == null)
                    writer.writeASCII("");
                else
                    writer.writeASCII(String.valueOf(getLayerName(x)));
                writer.writeByte(getLayerVolume(x));
            }
            if (level.compareTo(WriteLevel.INSTRUMENTS) >= 0) {
                byte b = 9;
                for (byte x = 8; x >= 0; x--, b--) {
                    if (getCustomInstrument(x) != null)
                        break;
                }
                writer.writeByte(b);
                // avoid constructing multiple objects
                NBSInstrument dummy = new NBSInstrument();
                for (byte x = 0; x < b; x++) {
                    IInstrument inst = getCustomInstrument(x);
                    if (inst != null && inst instanceof NBSInstrument) {
                        inst.write(writer);
                    } else if (inst != null) {
                        dummy.setName(inst.getName());
                        dummy.write(writer);
                    }
                }
            }
        }
    }
}
