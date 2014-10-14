package com.github.soniex2.nbx.api.helper;

import com.github.soniex2.nbx.api.stream.nbs.INBSReader;
import com.github.soniex2.nbx.api.stream.nbs.INBSWriter;

import java.io.IOException;

/**
 * @author soniex2
 */
public interface INBSData {
    public void read(INBSReader reader) throws IOException;

    public void write(INBSWriter writer) throws IOException;
}
