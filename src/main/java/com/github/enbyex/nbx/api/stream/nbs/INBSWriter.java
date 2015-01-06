package com.github.enbyex.nbx.api.stream.nbs;

import java.io.DataOutput;
import java.io.IOException;

/**
 * @author soniex2
 */
public interface INBSWriter extends DataOutput {
    void writeASCII(String s) throws IOException;
}
