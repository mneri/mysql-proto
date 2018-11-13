package me.mneri.mariadb.proto.packet;

import me.mneri.mariadb.proto.Packet;
import me.mneri.mariadb.proto.util.ByteArrayBuilder;
import me.mneri.mariadb.proto.util.ByteArrayReader;
import me.mneri.mariadb.proto.exception.MalformedPacketException;

public class AuthMoreData extends Packet {
    private String data;

    public String getData() {
        return data;
    }

    @Override
    public byte[] serialize() throws MalformedPacketException {
        ByteArrayBuilder builder = new ByteArrayBuilder();

        builder.putInt1((byte) 1);
        builder.putFixedLengthString(getData(), getData().length());

        return builder.build();
    }

    @Override
    public void deserialize(byte[] payload) throws MalformedPacketException {
        ByteArrayReader reader = new ByteArrayReader(payload);

        if (reader.getInt1() != 1)
            throw new MalformedPacketException();

        setData(reader.getStringEOF());
    }

    public void setData(String data) {
        this.data = data;
    }
}
