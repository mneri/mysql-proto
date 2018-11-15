package me.mneri.mariadb.proto;

import me.mneri.mariadb.util.ByteArrayWriter;

public class PayloadWriter extends ByteArrayWriter {
    PayloadWriter() {
        super();
    }

    public PayloadWriter(int initialCapacity) {
        super(initialCapacity);
    }
}
