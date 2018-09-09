package me.mneri.mysql.proto.util;

public class ByteArrayReader {
    private byte[] bytes;
    private int offset;

    public ByteArrayReader(byte[] buff) {
        this.bytes = buff;
    }

    public String getFixedLengthString(int len) {
        StringBuilder sb = new StringBuilder(len);
        int end = offset + len;

        while (offset < end)
            sb.append((char) bytes[offset++]);

        return sb.toString();
    }

    public byte getInt1() {
        return (byte) getIntN(1);
    }

    public short getInt2() {
        return (short) getIntN(2);
    }

    public int getInt3() {
        return (int) getIntN(3);
    }

    public int getInt4() {
        return (int) getIntN(4);
    }

    public long getInt6() {
        return getIntN(6);
    }

    public long getInt8() {
        return getIntN(8);
    }

    private long getIntN(int len) {
        long value = 0;

        for (int i = offset + len - 1; i > offset; i--) {
            value |= bytes[i] & 0xFF;
            value <<= 8;
        }

        value |= bytes[offset] & 0xFF;
        offset += len;

        return value;
    }

    public long getLengthEncodedInt() {
        int head = getInt1() & 0xFF;

        //@formatter:off
        if (head < 251)        return head;
        else if (head == 0xFC) return getInt2();
        else if (head == 0xFD) return getInt3();
        else                   return getInt8();
        //@formatter:on
    }

    public String getLengthEncodedString() {
        return getFixedLengthString((int) getLengthEncodedInt());
    }

    public String getNullTerminatedString() {
        StringBuilder sb = new StringBuilder();

        while (bytes[offset] != 0x00)
            sb.append((char) bytes[offset++]);

        offset++;

        return sb.toString();
    }

    public String getStringEOF() {
        StringBuilder sb = new StringBuilder();

        while (offset < bytes.length)
            sb.append((char) bytes[offset++]);

        return sb.toString();
    }

    public boolean hasMore() {
        return offset < bytes.length;
    }

    public void skip(int len) {
        offset += len;
    }
}