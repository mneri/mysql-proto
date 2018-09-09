package me.mneri.mysql.proto.packet;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
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

    public void write(Packet packet) throws IOException {
        byte[] test = {
                0x36, 0x00, 0x00, // length
                0x00, // sequence id
                0x0a, // protocol version
                0x35, 0x2e, 0x35, 0x2e, 0x32, 0x2d, 0x6d, 0x32, 0x00, // server version
                0x0b, 0x00, 0x00, 0x00, // connection id
                0x64, 0x76, 0x48, 0x40, 0x49, 0x2d, 0x43, 0x4a, // auth plugin data
                0x00, // fill
                (byte) 0xff, (byte) 0xf7, // capabilities
                0x08, // character set
                0x02, 0x00, // status flag
                0x00, 0x00, // capabilities
                0x00, // auth plugin data len (else 0)
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // reserved
                0x2a, 0x34, 0x64, 0x7c, 0x63, 0x5a, 0x77, 0x6b, 0x34, 0x5e, 0x5d, 0x3a, 0x00
        };
        byte[] payload = packet.payloadBytes();

        ByteArrayBuilder builder = new ByteArrayBuilder(4);
        builder.putInt3(payload.length);
        builder.putInt1(packet.getSequenceId());
        byte[] header = builder.build();

        out.write(header);
        out.write(payload);
//        out.write(test);
        out.flush();
    }
}
