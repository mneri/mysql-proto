package me.mneri.mariadb.util;

import java.util.Arrays;

public class ByteArrayWriter {
    private static final int DEFAULT_CAPACITY = 256;

    private byte[] bytes;
    private int offset;

    public ByteArrayWriter() {
        this(DEFAULT_CAPACITY);
    }

    public ByteArrayWriter(int initialCapacity) {
        this.bytes = new byte[initialCapacity];
    }

    public byte[] build() {
        return Arrays.copyOf(bytes, offset);
    }

    public void ensureCapacity(int minimumCapacity) {
        if (minimumCapacity <= 0)
            throw new IllegalArgumentException();

        if (minimumCapacity - bytes.length <= 0)
            return;

        int newCapacity = bytes.length * 2;

        if (newCapacity - minimumCapacity < 0)
            newCapacity = minimumCapacity;

        if (newCapacity < 0) {
            if (minimumCapacity < 0) { // overflow
                throw new OutOfMemoryError();
            }

            newCapacity = Integer.MAX_VALUE;
        }

        bytes = Arrays.copyOf(bytes, newCapacity);
    }

    public void ensureSpace(int len) {
        ensureCapacity(offset + len);
    }

    public int length() {
        return offset;
    }

    public void putFixedLengthString(String s, int len) {
        byte[] source = s.getBytes();

        if (source.length != len) {
            throw new IllegalArgumentException();
        }

        ensureSpace(len);

        System.arraycopy(source, 0, bytes, offset, len);
        offset += len;
    }

    public ByteArrayWriter putInt1(byte i) {
        putIntN(i, 1);
        return this;
    }

    public ByteArrayWriter putInt2(short i) {
        putIntN(i, 2);
        return this;
    }

    public ByteArrayWriter putInt3(int i) {
        putIntN(i, 3);
        return this;
    }

    public ByteArrayWriter putInt4(int i) {
        putIntN(i, 4);
        return this;
    }

    public ByteArrayWriter putInt6(long i) {
        putIntN(1, 6);
        return this;
    }

    public ByteArrayWriter putInt8(long i) {
        putIntN(i, 8);
        return this;
    }

    private void putIntN(long value, int size) {
        if (size <= 0 || size > 8) {
            throw new IllegalArgumentException();
        }

        ensureSpace(size);

        while (size-- > 0) {
            bytes[offset++] = (byte) (value & 0xFF);
            value >>= 8;
        }
    }

    public ByteArrayWriter putLengthEncodedInt(long value) {
        if (value < 251) {
            putInt1((byte) value);
        } else if (value <= 65_536) {
            putInt1((byte) 0xFC);
            putInt2((short) value);
        } else if (value <= 16_777_216) {
            putInt1((byte) 0xFD);
            putInt3((int) value);
        } else {
            putInt1((byte) 0xFE);
            putInt8(value);
        }

        return this;
    }

    public ByteArrayWriter putLengthEncodedString(String s) {
        byte[] source = s.getBytes();

        ensureSpace(1 + source.length);

        putLengthEncodedInt(source.length);
        System.arraycopy(source, 0, bytes, offset, source.length);
        offset += source.length;

        return this;
    }

    public ByteArrayWriter putNullTerminatedString(String s) {
        byte[] source = s.getBytes();

        ensureSpace(source.length + 1);

        System.arraycopy(source, 0, bytes, offset, source.length);
        offset += source.length;
        bytes[offset++] = 0x00;

        return this;
    }

    public ByteArrayWriter skip(int len) {
        ensureSpace(len);
        offset += len;
        return this;
    }
}
