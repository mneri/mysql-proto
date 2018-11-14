package me.mneri.mariadb.proto.packet;

import me.mneri.mariadb.proto.Packet;
import me.mneri.mariadb.proto.exception.MalformedPacketException;
import me.mneri.mariadb.proto.util.ByteArrayWriter;
import me.mneri.mariadb.proto.util.ByteArrayReader;

public class AuthMoreDataPacket extends Packet {
    private String data;

    @Override
    public void deserialize(byte[] payload) throws MalformedPacketException {
        ByteArrayReader reader = new ByteArrayReader(payload);

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
    public byte[] serialize() {
        ByteArrayWriter builder = new ByteArrayWriter();

        builder.putInt1((byte) 1);
        builder.putFixedLengthString(getData(), getData().length());

        return builder.build();
    }
}
