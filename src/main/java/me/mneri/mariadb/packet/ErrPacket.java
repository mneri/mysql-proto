package me.mneri.mariadb.packet;

import me.mneri.mariadb.proto.*;
import me.mneri.mariadb.exception.MalformedPacketException;

public class ErrPacket extends Packet {
    private short errorCode;
    private String errorMessage;
    private String sqlState;
    private String sqlStateMarker;

    @Override
    public void deserialize(PayloadReader reader) throws MalformedPacketException {
        Context context = getContext();

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
    public void serialize(PayloadWriter writer) {
        Context context = getContext();

        writer.putInt1((byte) 0xFF);
        writer.putInt2(getErrorCode());

        if (context.isCapabilitySet(Capabilities.PROTOCOL_41)) {
            writer.putFixedLengthString(getSqlStateMarker(), 1);
            writer.putFixedLengthString(getSqlState(), 5);
        }

        writer.putFixedLengthString(getErrorMessage(), getErrorMessage().length());
    }
}
