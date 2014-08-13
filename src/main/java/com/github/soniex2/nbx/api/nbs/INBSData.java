package com.github.soniex2.nbx.api.nbs;

import com.github.soniex2.nbx.api.stream.nbs.INBSReader;
import com.github.soniex2.nbx.api.stream.nbs.INBSWriter;

import java.io.IOException;

/**
 * @author soniex2
 */
public interface INBSData {
    INBSData read(INBSReader reader) throws IOException;

    void write(INBSWriter writer) throws IOException;
}
