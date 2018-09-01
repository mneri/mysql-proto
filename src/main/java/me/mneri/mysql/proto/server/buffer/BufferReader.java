package me.mneri.mysql.proto.server.buffer;

import me.mneri.mysql.proto.server.types.Integers;

public class BufferReader {
    private byte[] buff;
    private int offset;

    public BufferReader(byte[] buff) {
        this.buff = buff;
    }

    public byte int1() {
        try {
            return Integers.readInt1(buff, offset++);
        } catch (ArrayIndexOutOfBoundsException ignored) {
            throw new BufferOverflowException();
        }
    }

    public short int2() {
        try {
            short i = Integers.readInt2(buff, offset);
            offset += 2;
            return i;
        } catch (ArrayIndexOutOfBoundsException ignored) {
            throw new BufferOverflowException();
        }
    }

    public int int3() {
        try {
            int i = Integers.readInt3(buff, offset);
            offset += 3;
            return i;
        } catch (ArrayIndexOutOfBoundsException ignored) {
            throw new BufferOverflowException();
        }
    }

    public int int4() {
        try {
            int i = Integers.readInt4(buff, offset);
            offset += 4;
            return i;
        } catch (ArrayIndexOutOfBoundsException ignored) {
            throw new BufferOverflowException();
        }
    }

    public long int6() {
        try {
            long i = Integers.readInt6(buff, offset);
            offset += 6;
            return i;
        } catch (ArrayIndexOutOfBoundsException ignored) {
            throw new BufferOverflowException();
        }
    }

    public long int8() {
        try {
            long i = Integers.readInt8(buff, offset);
            offset += 8;
            return i;
        } catch (ArrayIndexOutOfBoundsException ignored) {
            throw new BufferOverflowException();
        }
    }

    public long lengthEncoded() {
        int i = buff[offset] & 0xFF;

        if (i < 251) {
            offset++;
            return i;
        } else if (i == 0xFC) {
            int n = Integers.readInt2(buff, offset + 1);
            offset += 3;
            return n;
        } else if (i == 0xFD) {
            int n = Integers.readInt3(buff, offset + 1);
            offset += 4;
            return n;
        } else {
            long n = Integers.readInt8(buff, offset + 1);
            offset += 9;
            return n;
        }
    }
}