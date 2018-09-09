package me.mneri.mysql.proto;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import me.mneri.mysql.proto.packet.*;

public class MySQLServerProtocol {
    private InputStream in;
    private OutputStream out;

    private MySQLServerProtocol(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }

    public static void start(InputStream in, OutputStream out) {
        MySQLServerProtocol proto = new MySQLServerProtocol(in, out);

        try {
            PacketReader reader = new PacketReader(in);
            PacketWriter writer = new PacketWriter(out);

            Handshake10 handshake = new Handshake10((byte) 0x00);
            handshake.setProtocolVersion((byte) 0x0a);
            handshake.setServerVersion("5.5.2-m2");
            handshake.setConnectionId(0x0b);
            handshake.setChallenge1("dvH@I-CJ");
            handshake.setCapabilities(0x0000f7ff);
            handshake.setCharacterSet(0x08);
            handshake.setStatusFlag(0x0002);
            handshake.setChallenge2Length(0);
            handshake.setChallenge2("*4d|cZwk4^]:");

            writer.write(handshake);

            HandshakeResponse41 response = reader.read(HandshakeResponse41.class);
            response.getSequenceId();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
