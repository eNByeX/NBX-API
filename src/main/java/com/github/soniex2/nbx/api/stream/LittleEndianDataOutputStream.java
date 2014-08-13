package com.github.soniex2.nbx.api.stream;

import java.io.*;

public class LittleEndianDataOutputStream extends BufferedOutputStream implements
        DataOutput {

    private byte[] bytearr = null;

    protected LittleEndianDataOutputStream(OutputStream out) {
        super(out);
    }

    public void writeASCII(String s) throws IOException {
        byte[] data = s.getBytes("US-ASCII");
        writeInt(data.length);
        write(data);
    }

    @Override
    public void writeBoolean(boolean v) throws IOException {
        write(v ? 1 : 0);
    }

    @Override
    public void writeByte(int v) throws IOException {
        write(v);
    }

    @Override
    public void writeShort(int s) throws IOException {
        write(s & 0xFF);
        write((s >>> 8) & 0xFF);
    }

    @Override
    public void writeInt(int i) throws IOException {
        writeShort(i & 0xFFFF);
        writeShort((i >>> 16) & 0xFFFF);
    }

    @Override
    public void writeLong(long v) throws IOException {
        writeInt((int) (v & 0xFFFFFFFFL));
        writeInt((int) ((v >>> 16) & 0xFFFFFFFFL));
    }

    @Override
    public void writeChar(int v) throws IOException {
        write(v & 0xFF);
        write((v >>> 8) & 0xFF);
    }

    @Override
    public void writeFloat(float v) throws IOException {
        writeInt(Float.floatToIntBits(v));
    }

    @Override
    public void writeDouble(double v) throws IOException {
        writeLong(Double.doubleToLongBits(v));
    }

    @Override
    public void writeBytes(String s) throws IOException {
        int len = s.length();
        for (int i = 0; i < len; i++) {
            write((byte) s.charAt(i));
        }
    }

    @Override
    public void writeChars(String s) throws IOException {
        int len = s.length();
        for (int i = 0; i < len; i++) {
            writeChar(s.charAt(i));
        }
    }

    @Override
    public void writeUTF(String str) throws IOException {
        int strlen = str.length();
        int utflen = 0;
        int c, count = 0;
        /* use charAt instead of copying String to char array */
        for (int i = 0; i < strlen; i++) {
            c = str.charAt(i);
            if ((c >= 0x0001) && (c <= 0x007F)) {
                utflen++;
            } else if (c > 0x07FF) {
                utflen += 3;
            } else {
                utflen += 2;
            }
        }
        if (utflen > 65535)
            throw new UTFDataFormatException("encoded string too long: "
                    + utflen + " bytes");
        byte[] bytearr = null;
        if (this.bytearr == null || (this.bytearr.length < (utflen + 4)))
            this.bytearr = new byte[(utflen * 2) + 2];
        bytearr = this.bytearr;
        bytearr[count++] = (byte) ((utflen >>> 8) & 0xFF);
        bytearr[count++] = (byte) ((utflen >>> 0) & 0xFF);
        int i = 0;
        for (i = 0; i < strlen; i++) {
            c = str.charAt(i);
            if (!((c >= 0x0001) && (c <= 0x007F)))
                break;
            bytearr[count++] = (byte) c;
        }
        for (; i < strlen; i++) {
            c = str.charAt(i);
            if ((c >= 0x0001) && (c <= 0x007F)) {
                bytearr[count++] = (byte) c;
            } else if (c > 0x07FF) {
                bytearr[count++] = (byte) (0xE0 | ((c >> 12) & 0x0F));
                bytearr[count++] = (byte) (0x80 | ((c >> 6) & 0x3F));
                bytearr[count++] = (byte) (0x80 | ((c >> 0) & 0x3F));
            } else {
                bytearr[count++] = (byte) (0xC0 | ((c >> 6) & 0x1F));
                bytearr[count++] = (byte) (0x80 | ((c >> 0) & 0x3F));
            }
        }
        out.write(bytearr, 0, utflen + 2);
    }

}
