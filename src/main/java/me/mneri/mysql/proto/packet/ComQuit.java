package me.mneri.mysql.proto.packet;

import me.mneri.mysql.proto.Packet;

public class ComQuit extends Packet {
    @Override
    public byte[] serialize() {
        return new byte[]{0x01};
    }

    @Override
    public void deserialize(byte[] payload) {
        // Ignore the payload
    }
}
