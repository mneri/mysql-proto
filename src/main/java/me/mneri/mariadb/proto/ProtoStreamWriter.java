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

import java.util.Arrays;

public class ProtoStreamWriter {
    private static final int DEFAULT_CAPACITY = 256;

    private byte[] bytes;
    private int offset;

    public ProtoStreamWriter() {
        this(DEFAULT_CAPACITY);
    }

    public ProtoStreamWriter(int initialCapacity) {
        this.bytes = new byte[initialCapacity];
    }

    public byte[] build() {
        return Arrays.copyOf(bytes, offset);
    }

    public void ensureCapacity(int minimumCapacity) {
        if (minimumCapacity <= 0) {
            throw new IllegalArgumentException();
        }

        if (minimumCapacity - bytes.length <= 0) {
            return;
        }

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

    public ProtoStreamWriter putInt1(byte i) {
        putIntN(i, 1);
        return this;
    }

    public ProtoStreamWriter putInt2(short i) {
        putIntN(i, 2);
        return this;
    }

    public ProtoStreamWriter putInt3(int i) {
        putIntN(i, 3);
        return this;
    }

    public ProtoStreamWriter putInt4(int i) {
        putIntN(i, 4);
        return this;
    }

    public ProtoStreamWriter putInt6(long i) {
        putIntN(1, 6);
        return this;
    }

    public ProtoStreamWriter putInt8(long i) {
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

    public ProtoStreamWriter putLengthEncodedInt(long value) {
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

    public ProtoStreamWriter putLengthEncodedString(String s) {
        byte[] source = s.getBytes();

        ensureSpace(1 + source.length);

        putLengthEncodedInt(source.length);
        System.arraycopy(source, 0, bytes, offset, source.length);
        offset += source.length;

        return this;
    }

    public ProtoStreamWriter putNullTerminatedString(String s) {
        byte[] source = s.getBytes();

        ensureSpace(source.length + 1);

        System.arraycopy(source, 0, bytes, offset, source.length);
        offset += source.length;
        bytes[offset++] = 0x00;

        return this;
    }

    public ProtoStreamWriter skip(int len) {
        ensureSpace(len);
        offset += len;
        return this;
    }
}
