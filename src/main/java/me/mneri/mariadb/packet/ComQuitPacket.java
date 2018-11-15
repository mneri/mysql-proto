package me.mneri.mariadb.packet;

import me.mneri.mariadb.proto.Packet;
import me.mneri.mariadb.proto.PayloadReader;
import me.mneri.mariadb.proto.PayloadWriter;

public class ComQuitPacket extends Packet {
    @Override
    public void deserialize(PayloadReader reader) {
    }

    @Override
    public void serialize(PayloadWriter writer) {
        writer.putInt1((byte) 0x01);
    }
}
