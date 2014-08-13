package com.github.soniex2.nbx.api.nbs;

import com.github.soniex2.nbx.api.stream.nbs.INBSReader;
import com.github.soniex2.nbx.api.stream.nbs.INBSWriter;

import java.io.IOException;

/**
 * @author soniex2
 */
public class NBSSong {
    private NBSSongData songData;
    private NBSHeader header;

    public NBSSong(NBSSongData song, NBSHeader header) {
        this.songData = song;
        this.header = header;
        header.setLayers(songData.getLayers());
        header.setTicks(songData.getTicks());
    }

    public NBSSong(NBSHeader header, NBSSongData song) {
        this(song, header);
    }

    public static NBSSong read(INBSReader reader) throws IOException {
        NBSHeader header = NBSHeader.read(reader);
        NBSSongData songData = NBSSongData.read(reader, header);
        return new NBSSong(header, songData);
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

    public NBSHeader getHeader() {
        return header;
    }

    public void write(INBSWriter writer, WriteLevel level) throws IOException {
        header.setLayers(songData.getLayers());
        header.setTicks(songData.getTicks());
        header.write(writer);
        songData.write(writer, level);
    }

    public enum WriteLevel {
        SONG,
        LAYERS,
        INSTRUMENTS;
    }
}
