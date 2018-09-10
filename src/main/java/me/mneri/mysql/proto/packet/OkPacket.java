package me.mneri.mysql.proto.packet;

import me.mneri.mysql.proto.Context;
import me.mneri.mysql.proto.Packet;
import me.mneri.mysql.proto.exception.MalformedPacketException;
import me.mneri.mysql.proto.ServerStatus;
import me.mneri.mysql.proto.util.ByteArrayBuilder;
import me.mneri.mysql.proto.util.ByteArrayReader;

import static me.mneri.mysql.proto.Capabilities.*;

public class OkPacket extends Packet {
    private long affectedRows;
    private String info;
    private long lastInsertId;
    private String sessionStateInfo;
    private short statusFlags;
    private short warnings;

    public long getAffectedRows() {
        return affectedRows;
    }

    public String getInfo() {
        return info;
    }

    public long getLastInsertId() {
        return lastInsertId;
    }

    public String getSessionStateInfo() {
        return sessionStateInfo;
    }

    public short getStatusFlags() {
        return statusFlags;
    }

    public short getWarnings() {
        return warnings;
    }

    @Override
    public byte[] serialize() {
        Context context = getContext();
        ByteArrayBuilder builder = new ByteArrayBuilder();

        //@formatter:off
        builder.putInt1             ((byte) 0x00);
        builder.putLengthEncodedInt (getAffectedRows());
        builder.putLengthEncodedInt (getLastInsertId());
        //@formatter:on

        if (context.isCapabilitySet(CLIENT_PROTOCOL_41)) {
            builder.putInt2(getStatusFlags());
            builder.putInt2(getWarnings());
        } else if (context.isCapabilitySet(CLIENT_TRANSACTIONS)) {
            builder.putInt2(getStatusFlags());
        }

        if (context.isCapabilitySet(CLIENT_SESSION_TRACK)) {
            builder.putLengthEncodedString(getInfo());

            if (context.isStatusFlagSet(ServerStatus.SESSION_STATE_CHANGED))
                builder.putLengthEncodedString(getSessionStateInfo());
        } else {
            builder.putFixedLengthString(getInfo(), getInfo().length());
        }

        return builder.build();
    }

    @Override
    public void deserialize(byte[] payload) throws MalformedPacketException {
        Context context = getContext();
        ByteArrayReader reader = new ByteArrayReader(payload);

        int header = reader.getInt1();

        if (header != 0x00 && header != 0xFE)
            throw new MalformedPacketException();

        setAffectedRows(reader.getLengthEncodedInt());
        setLastInsertId(reader.getLengthEncodedInt());

        if (context.isCapabilitySet(CLIENT_PROTOCOL_41)) {
            setStatusFlags(reader.getInt2());
            setWarnings(reader.getInt2());
        } else if (context.isCapabilitySet(CLIENT_TRANSACTIONS)) {
            setStatusFlags(reader.getInt2());
        }

        if (context.isCapabilitySet(CLIENT_SESSION_TRACK)) {
            setInfo(reader.getLengthEncodedString());

            if (context.isStatusFlagSet(ServerStatus.SESSION_STATE_CHANGED))
                setSessionStateInfo(reader.getLengthEncodedString());
        } else {
            setInfo(reader.getStringEOF());
        }
    }

    public void setAffectedRows(long affectedRows) {
        this.affectedRows = affectedRows;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setLastInsertId(long lastInsertId) {
        this.lastInsertId = lastInsertId;
    }

    public void setSessionStateInfo(String sessionStateInfo) {
        this.sessionStateInfo = sessionStateInfo;
    }

    public void setStatusFlags(short statusFlags) {
        this.statusFlags = statusFlags;
    }

    public void setWarnings(short warnings) {
        this.warnings = warnings;
    }
}
