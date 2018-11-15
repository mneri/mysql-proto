package me.mneri.mariadb.packet;

import me.mneri.mariadb.proto.Packet;
import me.mneri.mariadb.proto.PayloadReader;
import me.mneri.mariadb.proto.PayloadWriter;
import me.mneri.mariadb.exception.MalformedPacketException;

public class AuthMoreDataPacket extends Packet {
    private String data;

    @Override
    public void deserialize(PayloadReader reader) throws MalformedPacketException {
        if (reader.getInt1() != 1)
            throw new MalformedPacketException();

        setData(reader.getStringEOF());
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public void serialize(PayloadWriter writer) {
        writer.putInt1((byte) 1);
        writer.putFixedLengthString(getData(), getData().length());
    }
}
