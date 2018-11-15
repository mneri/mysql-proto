/*
 * Copyright 2018 Massimo Neri <hello@mneri.me>
 *
 * This file is part of mariadb-proto.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.mneri.mariadb.proto;

public class ProtoStreamReader {
    private byte[] buff;
    private int offset;

    public ProtoStreamReader(byte[] buff) {
        this.buff = buff;
    }

    public String getFixedLengthString(int len) {
        StringBuilder sb = new StringBuilder(len);
        int end = offset + len;

        while (offset < end) {
            sb.append((char) buff[offset++]);
        }

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
            value |= buff[i] & 0xFF;
            value <<= 8;
        }

        value |= buff[offset] & 0xFF;
        offset += len;

        return value;
    }

    public long getLengthEncodedInt() {
        int head = getInt1() & 0xFF;

        //@formatter:off
        if (head < 251)        { return head; }
        else if (head == 0xFC) { return getInt2(); }
        else if (head == 0xFD) { return getInt3(); }
        else                   { return getInt8(); }
        //@formatter:on
    }

    public String getLengthEncodedString() {
        return getFixedLengthString((int) getLengthEncodedInt());
    }

    public String getNullTerminatedString() {
        StringBuilder sb = new StringBuilder();

        while (buff[offset] != 0x00) {
            sb.append((char) buff[offset++]);
        }

        offset++;

        return sb.toString();
    }

    public String getStringEOF() {
        StringBuilder sb = new StringBuilder();

        while (offset < buff.length) {
            sb.append((char) buff[offset++]);
        }

        return sb.toString();
    }

    public boolean hasMore() {
        return offset < buff.length;
    }

    public void skip(int len) {
        offset += len;
    }
}
