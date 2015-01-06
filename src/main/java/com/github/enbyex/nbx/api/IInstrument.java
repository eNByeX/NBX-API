package com.github.enbyex.nbx.api;

import com.github.enbyex.nbx.api.stream.nbs.INBSWriter;

import java.io.IOException;

public interface IInstrument {
    // TODO find a way to do this properly... or just a better way to do it

    String getName();

    IInstrument copy();

    void write(INBSWriter reader) throws IOException;

}
