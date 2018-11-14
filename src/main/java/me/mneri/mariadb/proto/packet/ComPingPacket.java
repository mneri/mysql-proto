package me.mneri.mariadb.proto.packet;

import me.mneri.mariadb.proto.Packet;
import me.mneri.mariadb.proto.exception.MalformedPacketException;

public class ComPingPacket extends Packet {
    @Override
    public void deserialize(byte[] payload) throws MalformedPacketException {
    }

    @Override
    public byte[] serialize() throws MalformedPacketException {
        return new byte[]{0x0E};
    }
}
