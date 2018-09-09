package me.mneri.mysql.proto.packet;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import me.mneri.mysql.proto.exception.MalformedPacketException;
import me.mneri.mysql.proto.util.ByteArrayBuilder;

public class PacketWriter implements Closeable {
    private OutputStream out;

    public PacketWriter(OutputStream out) {
        this.out = out;
    }

    @Override
    public void close() throws IOException {
        out.close();
    }

    public void write(Packet packet) throws IOException, MalformedPacketException {
        byte[] payload = packet.payload();

        ByteArrayBuilder builder = new ByteArrayBuilder(4);
        builder.putInt3(payload.length);
        builder.putInt1(packet.getSequenceId());
        byte[] header = builder.build();

        out.write(header);
        out.write(payload);
        out.flush();
    }
}
