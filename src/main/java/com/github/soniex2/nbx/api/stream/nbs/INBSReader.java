package com.github.soniex2.nbx.api.stream.nbs;

import java.io.DataInput;
import java.io.IOException;

/**
 * @author soniex2
 */
public interface INBSReader extends DataInput {
    String readASCII() throws IOException;
}
