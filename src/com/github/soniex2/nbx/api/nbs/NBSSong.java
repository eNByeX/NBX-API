package com.github.soniex2.nbx.api.nbs;

/**
 * @author soniex2
 */
public class NBSSong {
    private NBSSongData songData;
    private NBSHeader header;

    public NBSSong(NBSSongData song, NBSHeader header) {
        this.songData = song;
        this.header = header;
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

}
