package com.github.soniex2.nbx.api.stream.nbs;

import com.github.soniex2.nbx.api.stream.LittleEndianDataOutputStream;

import java.io.OutputStream;

public class NBSOutputStream extends LittleEndianDataOutputStream implements INBSWriter {

    public NBSOutputStream(OutputStream os) {
        super(os);
    }

}
