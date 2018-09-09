package me.mneri.mysql.proto;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import me.mneri.mysql.proto.exception.MalformedPacketException;
import me.mneri.mysql.proto.flag.ServerStatus;
import me.mneri.mysql.proto.packet.Handshake10;
import me.mneri.mysql.proto.packet.HandshakeResponse320;
import me.mneri.mysql.proto.packet.PacketReader;
import me.mneri.mysql.proto.packet.PacketWriter;

public class MySQLServerProtocol {
    private PacketReader reader;
    private PacketWriter writer;

    private MySQLServerProtocol(InputStream in, OutputStream out) {
        reader = new PacketReader(in);
        writer = new PacketWriter(out);
    }

    private void start() {
        try {
            //@formatter:off
            Handshake10 handshake = new Handshake10((byte) 0);

            handshake.setServerVersion  ("5.5.2-m2");
            handshake.setConnectionId   (0);
            handshake.setAuthPluginData ("dvH@I-CJ*4d|cZwk4^]:");
            handshake.setCapabilities   (0xf5ff);
            handshake.setCharacterSet   (0x08);
            handshake.setServerStatus   (ServerStatus.AUTOCOMMIT);
            handshake.setAuthPluginName ("mysql_native_password");

            writer.write(handshake);
            //@formatter:on

            HandshakeResponse320 handshakeResponse = reader.read(HandshakeResponse320.class);
        } catch (IOException | MalformedPacketException e) {
            e.printStackTrace();
        }
    }

    public static void start(InputStream in, OutputStream out) {
        MySQLServerProtocol proto = new MySQLServerProtocol(in, out);
        proto.start();
    }
}
