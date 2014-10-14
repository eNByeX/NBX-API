package com.github.soniex2.nbx.api.nbx.extension;

import com.github.soniex2.nbx.api.nbx.chunk.AbstractChunkableChunk;
import com.github.soniex2.nbx.api.stream.nbs.INBSReader;
import com.github.soniex2.nbx.api.stream.nbs.INBSWriter;

import java.io.IOException;

/**
 * @author soniex2
 */
public class NBXLoop extends AbstractChunkableChunk {

    /**
     * Start and end of loop. Negative values = from end of song.
     */
    private short loopStart = 1, loopEnd = -1;
    /**
     * How many times to loop. -1 = repeat forever.
     */
    private int loopCount = -1;

    public int getLoopCount() {
        return loopCount;
    }

    public void setLoopCount(int loopCount) {
        this.loopCount = loopCount;
    }

    public short getLoopStart() {
        return loopStart;
    }

    public void setLoopStart(short loopStart) {
        this.loopStart = loopStart;
    }

    public short getLoopEnd() {
        return loopEnd;
    }

    public void setLoopEnd(short loopEnd) {
        this.loopEnd = loopEnd;
    }

    @Override
    public void read(INBSReader reader) throws IOException {
        // there are (currently) 2 ways to use this:
        // 0 = loop section,
        // 1 = song loop
        byte mode = reader.readByte();
        switch (mode) {
            default: // Default to last known mode. This is not on the spec!
            case 1:
                loopStart = reader.readShort();
                loopCount = -1;
                loopEnd = -1;
                break;
            case 0:
                loopCount = reader.readInt();
                loopStart = reader.readShort();
                loopEnd = reader.readShort();
                break;
        }
    }

    @Override
    public void write(INBSWriter writer) throws IOException {
        if (loopCount == -1 && loopEnd == -1) {
            writer.writeByte(1);
            writer.writeShort(loopStart);
        } else {
            writer.writeByte(0);
            writer.writeInt(loopCount);
            writer.writeShort(loopStart);
            writer.writeShort(loopEnd);
        }
    }

    @Override
    public String getChunkId() {
        return "lOOP";
    }
}
