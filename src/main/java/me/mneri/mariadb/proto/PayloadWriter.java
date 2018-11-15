package me.mneri.mariadb.proto;

public class PayloadWriter extends ByteArrayWriter {
    PayloadWriter() {
        super();
    }

    public PayloadWriter(int initialCapacity) {
        super(initialCapacity);
    }
}
