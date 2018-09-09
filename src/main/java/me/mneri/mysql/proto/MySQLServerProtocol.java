package me.mneri.mysql.proto;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import me.mneri.mysql.proto.packet.Capabilities;
import me.mneri.mysql.proto.packet.Handshake;
import me.mneri.mysql.proto.packet.PacketReader;
import me.mneri.mysql.proto.packet.PacketWriter;

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

            while (true) {
                Handshake handshake = new Handshake();
                handshake.setProtocolVersion(0x0a);
                handshake.setServerVersion("me.mneri.mysql.proto");
                handshake.setConnectionId(1);
                handshake.setChallenge1("aaaaaaaa");
                handshake.setCapabilities(Capabilities.CLIENT_PLUGIN_AUTH);

                writer.write(handshake);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
