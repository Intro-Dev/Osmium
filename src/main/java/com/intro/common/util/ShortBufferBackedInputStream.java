package com.intro.common.util;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ShortBuffer;

public class ShortBufferBackedInputStream extends InputStream {

    private final ShortBuffer buf;

    public ShortBufferBackedInputStream(ShortBuffer buf) {
        this.buf = buf;
    }


    public int read() {
        if (!buf.hasRemaining()) {
            return -1;
        }
        int got = buf.get() & 65535;
        System.out.println("read: " + got);
        return got;
    }

    public int read(byte @NotNull [] bytes, int off, int len) throws IOException {
        if (!buf.hasRemaining()) {
            return -1;
        }

        len = Math.min(len, buf.remaining());
        short[] shorts = new short[bytes.length];
        buf.get(shorts, off, len);
        for(int i = 0; i < shorts.length; i++) {
            short s = bytes[i];
            bytes[i] = (byte) s;
        }
        System.out.println("read: " + len);
        return len;
    }



}