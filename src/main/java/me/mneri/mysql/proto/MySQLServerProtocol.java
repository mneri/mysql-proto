package me.mneri.mysql.proto;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import me.mneri.mysql.proto.packet.*;

import static me.mneri.mysql.proto.packet.Capabilities.*;

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

            Handshake handshake = new Handshake();
            handshake.setProtocolVersion((byte) 0x0a);
            handshake.setServerVersion("5.5.2-m2");
            handshake.setConnectionId(0x0b);
            handshake.setChallenge1("dvH@I-CJ");
            handshake.setCapabilities(CLIENT_LONG_PASSWORD | CLIENT_PROTOCOL_41);
            handshake.setCharacterSet(0x08);
            handshake.setStatusFlag(0);

            writer.write(handshake);

            HandshakeResponse response = reader.read(HandshakeResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
