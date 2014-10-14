package com.github.soniex2.nbx.api.nbx.extension;

import com.github.soniex2.nbx.api.nbx.chunk.AbstractChunkableChunk;
import com.github.soniex2.nbx.api.stream.nbs.INBSReader;
import com.github.soniex2.nbx.api.stream.nbs.INBSWriter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author soniex2
 */
public class NBXLyrics extends AbstractChunkableChunk {
    /*
    TODO basic/simple compression
    Lyrics have lots of repeated words and phrases, so it's pretty easy to compress them.
     */

    ArrayList<String> lyrics = new ArrayList<String>();

    public static final byte VERSION = 0;

    @Override
    public void read(INBSReader reader) throws IOException {
        // version check
        assert reader.readByte() == VERSION : "Unknown NBXLyrics/lRCS version";

        short count = reader.readShort();
        lyrics.ensureCapacity(count + 10);
        for (int i = 0; i < count; i++) {
            String str = reader.readUTF();
            lyrics.set(i, str);
        }
    }

    @Override
    public void write(INBSWriter writer) throws IOException {

        writer.writeByte(VERSION);

        writer.writeShort(lyrics.size());
        for (int i = 0; i < lyrics.size(); i++) {
            String str = lyrics.get(i);
            if (str == null) str = "";
            writer.writeUTF(str);
        }
    }

    @Override
    public String getChunkId() {
        return "lRCS";
    }
}
