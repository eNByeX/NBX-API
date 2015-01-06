package com.github.enbyex.nbx.api.stream.nbs;

import com.github.enbyex.nbx.api.stream.LittleEndianDataInputStream;

import java.io.InputStream;

public class NBSInputStream extends LittleEndianDataInputStream implements INBSReader {

    public NBSInputStream(InputStream is) {
        super(is);
    }

}
