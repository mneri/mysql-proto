package me.mneri.mysql.proto.server.packet;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public class PacketReader implements Closeable {
    private InputStream in;

    private PacketReader(InputStream in) {
        this.in = in;
    }

    @Override
    public void close() throws IOException {
        in.close();
    }

    public <T extends Packet> T read(Class<T> clazz) throws IOException {
        return null;
    }
}
