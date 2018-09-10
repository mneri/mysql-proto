package me.mneri.mysql.proto.packet;

import me.mneri.mysql.proto.Packet;

public class ComQuit extends Packet {
    @Override
    public byte[] payload() {
        return new byte[]{0x01};
    }

    @Override
    public void payload(byte[] bytes) {
        // Ignore the payload
    }
}
