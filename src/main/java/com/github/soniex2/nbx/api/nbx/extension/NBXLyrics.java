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
    TODO dictionary-based compression
    Lyrics have lots of repeated words and phrases, so it's pretty easy to compress them.
     */

    public static final byte VERSION = 1;
    private ArrayList<String> lyrics = new ArrayList<String>();

    @Override
    public void read(INBSReader reader) throws IOException {
        // version check
        assert reader.readByte() == VERSION : "Unknown NBXLyrics/lRCS version";

        lyrics.ensureCapacity(reader.readShort());

        short count = -1;
        short jumps;
        while (true) {
            jumps = reader.readShort();
            if (jumps == 0) {
                break;
            }
            count += jumps;
            if (count < 0) {
                throw new IllegalStateException("Too many ticks!");
            }
            /*while (lyrics.size() < count) {
                lyrics.add(null);
            }*/
            // avoid calling lyrics.size() on every iteration? TODO benchmark this
            int loop = count - lyrics.size();
            lyrics.ensureCapacity(count); // optimize when the expected size != true size
            for (int i = 0; i < loop; ++i) { // TODO verify loop count
                lyrics.add(null);
            }
            lyrics.add(count, reader.readUTF());
            if (lyrics.size() > Short.MAX_VALUE) {
                throw new IllegalStateException("Too many ticks!");
            }
        }
    }

    @Override
    public void write(INBSWriter writer) throws IOException {
        if (lyrics.size() > Short.MAX_VALUE) {
            throw new IllegalStateException("Too many ticks!");
        }

        writer.writeByte(VERSION);

        writer.writeShort(lyrics.size());

        short count = 1;
        writer.writeShort(count);
        for (int i = 0; i < lyrics.size(); i++) {
            String str = lyrics.get(i);
            if (str == null) {
                count++;
                continue;
            } else if (count > 1) {
                writer.writeShort(count);
                count = 1;
            }
            writer.writeUTF(str);
        }
        writer.writeShort(0);
    }

    @Override
    public String getChunkId() {
        return "lRCS";
    }
}
