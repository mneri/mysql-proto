package me.mneri.mariadb.proto;

import me.mneri.mariadb.util.ByteArrayReader;

public class PayloadReader extends ByteArrayReader {
    PayloadReader(byte[] buff) {
        super(buff);
    }
}