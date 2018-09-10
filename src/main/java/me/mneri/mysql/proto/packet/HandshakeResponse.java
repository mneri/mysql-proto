package me.mneri.mysql.proto.packet;

import me.mneri.mysql.proto.Packet;
import me.mneri.mysql.proto.util.ByteArrayReader;

public class HandshakeResponse extends Packet {
    @Override
    public byte[] payload() {
        return new byte[0];
    }

    @Override
    public void payload(byte[] bytes) {
        ByteArrayReader reader = new ByteArrayReader(bytes);
    }
}
