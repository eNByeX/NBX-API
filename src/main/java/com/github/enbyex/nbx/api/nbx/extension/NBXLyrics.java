package com.github.enbyex.nbx.api.nbx.extension;

import com.github.enbyex.nbx.api.nbx.chunk.AbstractChunkableChunk;
import com.github.enbyex.nbx.api.stream.nbs.INBSReader;
import com.github.enbyex.nbx.api.stream.nbs.INBSWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author soniex2
 */
public class NBXLyrics extends AbstractChunkableChunk {

    public static final byte VERSION = 0;
    private ArrayList<String> lyrics = new ArrayList<String>();

    @Override
    public void read(INBSReader reader) throws IOException {
        // version check
        assert reader.readByte() == VERSION : "Unknown NBXLyrics/lRCS version";

        // load dictionary
        short dictionarySize = reader.readShort();
        ArrayList<String> dictionary = new ArrayList<String>();
        dictionary.ensureCapacity(dictionarySize);
        for (int i = 0; i < dictionarySize; i++) {
            dictionary.add(reader.readUTF());
        }

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
            for (int i = 1; i < loop; ++i) {
                lyrics.add(null);
            }
            lyrics.add(count, dictionary.get(reader.readShort()));
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

        // Build a dictionary
        HashMap<String, Integer> simap = new HashMap<String, Integer>();
        int last = -1;

        for (int i = 0, lyricsSize = lyrics.size(); i < lyricsSize; i++) {
            String s = lyrics.get(i);
            if (s != null && !simap.containsKey(s)) {
                simap.put(s, ++last);
            }
        }

        assert last <= Short.MAX_VALUE;

        ArrayList<String> dictionary = new ArrayList<String>(Arrays.asList(new String[last + 1]));

        for (Map.Entry<String, Integer> entry : simap.entrySet()) {
            dictionary.set(entry.getValue(), entry.getKey());
        }

        // Serialize dictionary
        int dictionarySize = dictionary.size();
        writer.writeShort(dictionarySize);
        for (int i = 0; i < dictionarySize; i++) {
            String s = dictionary.get(i);
            writer.writeUTF(s);
        }

        // Serialize lyrics
        int lyricsSize = lyrics.size();
        writer.writeShort(lyricsSize); // size hint

        short lastTick = -1;
        for (short i = 0; i < lyricsSize; i++) {
            String str = lyrics.get(i);
            if (str == null)
                continue;
            writer.writeShort(i - lastTick);
            lastTick = i;
            writer.writeShort(simap.get(str));
        }
        writer.writeShort(0);
    }

    @Override
    public String getChunkId() {
        return "lRCS";
    }
}
