package me.mneri.mysql.proto.server.types;

public final class Integers {
    private Integers() {
    }

    public static byte readInt1(byte[] buff, int offset) {
        return (byte) readIntN(buff, offset, 1);
    }

    public static short readInt2(byte[] buff, int offset) {
        return (short) readIntN(buff, offset, 2);
    }

    public static int readInt3(byte[] buff, int offset) {
        return (int) readIntN(buff, offset, 3);
    }

    public static int readInt4(byte[] buff, int offset) {
        return (int) readIntN(buff, offset, 4);
    }

    public static long readInt6(byte[] buff, int offset) {
        return readIntN(buff, offset, 6);
    }

    public static long readInt8(byte[] buff, int offset) {
        return readIntN(buff, offset, 8);
    }

    private static long readIntN(byte[] buff, int offset, int size) {
        long value = 0;

        for (int i = offset + size - 1; i > offset; i--) {
            value |= buff[i] & 0xFF;
            value <<= 8;
        }

        value |= buff[offset] & 0xFF;

        return value;
    }

    public static void writeInt1(byte[] buff, int offset, byte i) {
        writeIntN(buff, offset, i, 1);
    }

    public static void writeInt2(byte[] buff, int offset, short i) {
        writeIntN(buff, offset, i, 2);
    }

    public static void writeInt3(byte[] buff, int offset, int i) {
        writeIntN(buff, offset, i, 3);
    }

    public static void writeInt4(byte[] buff, int offset, int i) {
        writeIntN(buff, offset, i, 4);
    }

    public static void writeInt6(byte[] buff, int offset, long i) {
        writeIntN(buff, offset, i, 6);
    }

    public static void writeInt8(byte[] buff, int offset, long i) {
        writeIntN(buff, offset, i, 8);
    }

    private static void writeIntN(byte[] buff, int offset, long value, int size) {
        if (size <= 0 || size > 8)
            throw new IllegalArgumentException();

        while (size-- > 0) {
            buff[offset++] = (byte) (value & 0xFF);
            value >>= 8;
        }
    }
}
