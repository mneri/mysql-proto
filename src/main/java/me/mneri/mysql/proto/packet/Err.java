package me.mneri.mysql.proto.packet;

import me.mneri.mysql.proto.Context;
import me.mneri.mysql.proto.Packet;
import me.mneri.mysql.proto.exception.MalformedPacketException;
import me.mneri.mysql.proto.util.ByteArrayBuilder;
import me.mneri.mysql.proto.util.ByteArrayReader;

import static me.mneri.mysql.proto.Capabilities.*;

public class Err extends Packet {
    private short errorCode;
    private String errorMessage;
    private String sqlState;
    private String sqlStateMarker;

    @Override
    public void deserialize(byte[] payload) throws MalformedPacketException {
        Context context = getContext();
        ByteArrayReader reader = new ByteArrayReader(payload);

        if ((reader.getInt1() & 0xFF) != 0xFF)
            throw new MalformedPacketException();

        setErrorCode(reader.getInt2());

        if (context.isCapabilitySet(CLIENT_PROTOCOL_41)) {
            //@formatter:off
            setSqlStateMarker (reader.getFixedLengthString(1));
            setSqlState       (reader.getFixedLengthString(5));
            //@formatter:on
        }

        setErrorMessage(reader.getStringEOF());
    }

    public short getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getSqlState() {
        return sqlState;
    }

    public String getSqlStateMarker() {
        return sqlStateMarker;
    }

    @Override
    public byte[] serialize() throws MalformedPacketException {
        Context context = getContext();
        ByteArrayBuilder builder = new ByteArrayBuilder();

        builder.putInt1((byte) 0xFF);
        builder.putInt2(getErrorCode());

        if (context.isCapabilitySet(CLIENT_PROTOCOL_41)) {
            builder.putFixedLengthString(getSqlStateMarker(), 1);
            builder.putFixedLengthString(getSqlState(), 5);
        }

        builder.putFixedLengthString(getErrorMessage(), getErrorMessage().length());

        return builder.build();
    }

    public void setErrorCode(short errorCode) {
        this.errorCode = errorCode;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setSqlState(String sqlState) {
        this.sqlState = sqlState;
    }

    public void setSqlStateMarker(String sqlStateMarker) {
        this.sqlStateMarker = sqlStateMarker;
    }
}
