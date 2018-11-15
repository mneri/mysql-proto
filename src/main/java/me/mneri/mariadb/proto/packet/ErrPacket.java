package me.mneri.mariadb.proto.packet;

import me.mneri.mariadb.proto.Capabilities;
import me.mneri.mariadb.proto.Context;
import me.mneri.mariadb.proto.Packet;
import me.mneri.mariadb.proto.exception.MalformedPacketException;
import me.mneri.mariadb.proto.util.ByteArrayWriter;
import me.mneri.mariadb.proto.util.ByteArrayReader;

public class ErrPacket extends Packet {
    private short errorCode;
    private String errorMessage;
    private String sqlState;
    private String sqlStateMarker;

    @Override
    public void deserialize(byte[] payload) throws MalformedPacketException {
        Context context = getContext();
        ByteArrayReader reader = new ByteArrayReader(payload);

        if ((reader.getInt1() & 0xFF) != 0xFF) {
            throw new MalformedPacketException();
        }

        setErrorCode(reader.getInt2());

        if (context.isCapabilitySet(Capabilities.PROTOCOL_41)) {
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

    public void setErrorCode(short errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getSqlState() {
        return sqlState;
    }

    public void setSqlState(String sqlState) {
        this.sqlState = sqlState;
    }

    public String getSqlStateMarker() {
        return sqlStateMarker;
    }

    public void setSqlStateMarker(String sqlStateMarker) {
        this.sqlStateMarker = sqlStateMarker;
    }

    @Override
    public byte[] serialize() {
        Context context = getContext();
        ByteArrayWriter builder = new ByteArrayWriter();

        builder.putInt1((byte) 0xFF);
        builder.putInt2(getErrorCode());

        if (context.isCapabilitySet(Capabilities.PROTOCOL_41)) {
            builder.putFixedLengthString(getSqlStateMarker(), 1);
            builder.putFixedLengthString(getSqlState(), 5);
        }

        builder.putFixedLengthString(getErrorMessage(), getErrorMessage().length());

        return builder.build();
    }
}
