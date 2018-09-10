package me.mneri.mysql.proto;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import me.mneri.mysql.proto.exception.MalformedPacketException;
import me.mneri.mysql.proto.flag.ServerStatus;
import me.mneri.mysql.proto.packet.Handshake10;
import me.mneri.mysql.proto.packet.HandshakeResponse;
import me.mneri.mysql.proto.packet.OkPacket;

public class ServerProtocol {
    private Context context;

    private ServerProtocol(InputStream in, OutputStream out) {
        context = new Context(in, out);
    }

    private void start() {
        try {
            //@formatter:off
            Handshake10 handshake = context.create(Handshake10.class, (byte) 0);

            handshake.setServerVersion  ("5.5.2-m2");
            handshake.setConnectionId   (0);
            handshake.setAuthPluginData ("dvH@I-CJ*4d|cZwk4^]:");
            handshake.setCapabilities   (0xf5ff);
            handshake.setCharacterSet   (0x08);
            handshake.setServerStatus   (ServerStatus.AUTOCOMMIT);
            handshake.setAuthPluginName ("mysql_native_password");

            context.write(handshake);
            //@formatter:on

            HandshakeResponse handshakeResponse = context.read(HandshakeResponse.class);

            OkPacket ok = new OkPacket((byte) 0);
            ok.setInfo("Success");

            context.write(ok);

            System.out.println("yay");
        } catch (IOException | MalformedPacketException e) {
            e.printStackTrace();
        }
    }

    public static void start(InputStream in, OutputStream out) {
        ServerProtocol proto = new ServerProtocol(in, out);
        proto.start();
    }
}
