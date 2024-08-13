package com.example.recommendation.functionality;
import java.io.IOException;
import java.io.OutputStream;

public class ChecksumOutputStream extends OutputStream {
    private final OutputStream out;
    private int checksum = 0;

    public ChecksumOutputStream(OutputStream out) {
        this.out = out;
    }

    @Override
    public void write(int b) throws IOException {
        checksum += b;
        out.write(b);
    }

    public int getChecksum() {
        return checksum;
    }

    @Override
    public void write(byte[] b) throws IOException {
        for (byte value : b) {
            write(value);
        }
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        for (int i = off; i < off + len; i++) {
            write(b[i]);
        }
    }
}

