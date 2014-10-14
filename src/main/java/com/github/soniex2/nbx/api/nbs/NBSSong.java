package com.github.soniex2.nbx.api.nbs;

import com.github.soniex2.nbx.api.helper.INBSData;
import com.github.soniex2.nbx.api.stream.nbs.INBSReader;
import com.github.soniex2.nbx.api.stream.nbs.INBSWriter;

import java.io.IOException;

/**
 * @author soniex2
 */
public class NBSSong implements INBSData {
    private NBSSongData songData;
    private NBSHeader header;

    @Override
    public void read(INBSReader reader) throws IOException {
        NBSHeader temp = new NBSHeader();
        temp.read(reader);
        header = temp;
        songData = new NBSSongData().read(reader, header);
    }

    /**
     * Returns the song length, in milliseconds.
     */
    public long getLength() {
        return songData.getLength(header);
    }

    public NBSSongData getSongData() {
        return songData;
    }

    public void setSongData(NBSSongData songData) {
        this.songData = songData;
    }

    public NBSHeader getHeader() {
        return header;
    }

    public void setHeader(NBSHeader header) {
        this.header = header;
    }

    public void write(INBSWriter writer, WriteLevel level) throws IOException {
        header.setLayers(songData.getLayers());
        header.setTicks(songData.getTicks());
        header.write(writer);
        songData.write(writer, level);
    }

    @Override
    public void write(INBSWriter writer) throws IOException {
        write(writer, WriteLevel.INSTRUMENTS);
    }

    public enum WriteLevel {
        SONG,
        LAYERS,
        INSTRUMENTS;
    }
}
