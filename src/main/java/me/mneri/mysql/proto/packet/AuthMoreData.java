package me.mneri.mysql.proto.packet;

import me.mneri.mysql.proto.exception.MalformedPacketException;
import me.mneri.mysql.proto.util.ByteArrayBuilder;
import me.mneri.mysql.proto.util.ByteArrayReader;

public class AuthMoreData extends Packet {
    private String data;
    private byte sequenceId;

    public AuthMoreData(byte sequenceId) {
        this.sequenceId = sequenceId;
    }

    public String getData() {
        return data;
    }

    @Override
    public byte getSequenceId() {
        return sequenceId;
    }

    @Override
    public byte[] payload() throws MalformedPacketException {
        ByteArrayBuilder builder = new ByteArrayBuilder();

        builder.putInt1((byte) 1);
        builder.putFixedLengthString(getData(), getData().length());

        return builder.build();
    }

    @Override
    public void payload(byte[] bytes) throws MalformedPacketException {
        ByteArrayReader reader = new ByteArrayReader(bytes);

        if (reader.getInt1() != 1)
            throw new MalformedPacketException();

        setData(reader.getStringEOF());
    }

    public void setData(String data) {
        this.data = data;
    }
}
