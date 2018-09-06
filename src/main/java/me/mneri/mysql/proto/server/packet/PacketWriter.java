package me.mneri.mysql.proto.server.packet;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

public class PacketWriter implements Closeable {
    private OutputStream out;

    private PacketWriter(OutputStream out) {
        this.out = out;
    }

    @Override
    public void close() throws IOException {
        out.close();
    }

    public void write(Packet packet) throws IOException {
        out.write(packet.toByteArray());
    }
}
