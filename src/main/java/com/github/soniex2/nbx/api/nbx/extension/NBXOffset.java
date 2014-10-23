package com.github.soniex2.nbx.api.nbx.extension;

import com.github.soniex2.nbx.api.nbx.chunk.AbstractChunkableChunk;
import com.github.soniex2.nbx.api.stream.nbs.INBSReader;
import com.github.soniex2.nbx.api.stream.nbs.INBSWriter;

import java.io.IOException;

/**
 * @author soniex2
 */
// TODO
public class NBXOffset extends AbstractChunkableChunk {

    @Override
    public String getChunkId() {
        // [n]ote [OFF]set
        return "nOFF";
    }

    /*
    I have 0 idea of how to do this, but here's what I know:

    2 bits: 2^2 = 4
    00 = 0/4
    01 = 1/4
    10 = -2/4
    11 = -1/4

    Alternatively:
    00 = 0/4
    01 = 1/4
    10 = 2/4
    11 = 3/4

    Both MCNBS and Java like signed numbers, so the former is "better".
    1/256 precision should be more than enough, if not I'll make a version with 1/65536 precision.
     */

    @Override
    public void write(INBSWriter nbsWriter) throws IOException {

    }

    @Override
    public void read(INBSReader nbsReader) throws IOException {

    }
}
