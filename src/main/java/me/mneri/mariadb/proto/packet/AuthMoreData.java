package me.mneri.mariadb.proto.packet;

import me.mneri.mariadb.proto.Packet;
import me.mneri.mariadb.proto.exception.MalformedPacketException;
import me.mneri.mariadb.proto.util.ByteArrayBuilder;
import me.mneri.mariadb.proto.util.ByteArrayWriter;

public class AuthMoreData extends Packet {
    private String data;

    @Override
    public void deserialize(byte[] payload) throws MalformedPacketException {
        ByteArrayWriter reader = new ByteArrayWriter(payload);

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
        ByteArrayBuilder builder = new ByteArrayBuilder();

        builder.putInt1((byte) 1);
        builder.putFixedLengthString(getData(), getData().length());

        return builder.build();
    }
}
