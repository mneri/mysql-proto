package me.mneri.mysql.proto.server.buffer;

import me.mneri.mysql.proto.server.types.Integers;

public class ByteArrayWriter {
    private byte[] buff;
    private int offset;

    public ByteArrayWriter(byte[] buff) {
        this.buff = buff;
    }

    public void int1(byte i) {
        Integers.writeInt1(buff, offset, i);
        offset++;
    }

    public void int2(short i) {
        Integers.writeInt2(buff, offset, i);
        offset += 2;
    }

    public void int3(int i) {
        Integers.writeInt3(buff, offset, i);
        offset += 3;
    }

    public void int4(int i) {
        Integers.writeInt4(buff, offset, i);
        offset += 4;
    }

    public void int6(long i) {
        Integers.writeInt6(buff, offset, i);
        offset += 6;
    }

    public void int8(long i) {
        Integers.writeInt8(buff, offset, i);
        offset += 8;
    }

    public void lengthEncoded(long value) {
        if (value < 251) {
            Integers.writeInt1(buff, offset, (byte) value);
            offset++;
        } else if (value <= 65_536) {
            Integers.writeInt1(buff, offset, (byte) 0xFC);
            Integers.writeInt2(buff, offset + 1, (short) value);
            offset += 3;
        } else if (value <= 16_777_216) {
            Integers.writeInt1(buff, offset, (byte) 0xFD);
            Integers.writeInt3(buff, offset + 1, (int) value);
            offset += 4;
        } else {
            Integers.writeInt1(buff, offset, (byte) 0xFE);
            Integers.writeInt8(buff, offset + 1, value);
            offset += 9;
        }
    }
}
