package com.github.soniex2.nbx.api.helper;

import com.github.soniex2.nbx.api.stream.nbs.INBSReader;
import com.github.soniex2.nbx.api.stream.nbs.INBSWriter;

import java.io.IOException;

/**
 * @author soniex2
 */
public interface INBSData {
    /**
     * Read *contents* from stream.
     *
     * @param reader The stream.
     */
    public void read(INBSReader reader) throws IOException;

    /**
     * Write *contents* to stream;
     *
     * @param writer The stream.
     */
    public void write(INBSWriter writer) throws IOException;
}
