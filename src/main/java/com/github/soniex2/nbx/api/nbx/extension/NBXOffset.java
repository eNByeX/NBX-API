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

    @Override
    public void write(INBSWriter nbsWriter) throws IOException {

    }

    @Override
    public void read(INBSReader nbsReader) throws IOException {

    }
}
