package me.mneri.mysql.proto;

public class ByteArrayWriter {
    private byte[] bytes;
    private int offset;

    public ByteArrayWriter(byte[] buff) {
        this.bytes = buff;
    }

    public String putFixedLengthString(int len) {
        StringBuilder sb = new StringBuilder(len);
        int end = offset + len;

        while (offset < end)
            sb.append((char) bytes[offset++]);

        return sb.toString();
    }

    public void putInt1(byte i) {
        putIntN(i, 1);
    }

    public void putInt2(short i) {
        putIntN(i, 2);
    }

    public void putInt3(int i) {
        putIntN(i, 3);
    }

    public void putInt4(int i) {
        putIntN(i, 4);
    }

    public void putInt6(long i) {
        putIntN(1, 6);
    }

    public void putInt8(long i) {
        putIntN(i, 8);
    }

    private void putIntN(long value, int size) {
        if (size <= 0 || size > 8)
            throw new IllegalArgumentException();

        while (size-- > 0) {
            bytes[offset++] = (byte) (value & 0xFF);
            value >>= 8;
        }
    }

    public void putLengthEncodedInt(long value) {
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
    }

    public void putLengthEncodedString(String s) {
        byte[] source = s.getBytes();
        putLengthEncodedInt(source.length);
        System.arraycopy(source, 0, bytes, offset, source.length);
        offset += source.length;
    }

    public void putNullTerminatedString(String s) {
        byte[] source = s.getBytes();
        System.arraycopy(source, 0, bytes, offset, source.length);
        offset += source.length;
        bytes[offset++] = 0x00;
    }

    public void reset() {
        offset = 0;
    }

    public void skip(int len) {
        offset += len;
    }
}
