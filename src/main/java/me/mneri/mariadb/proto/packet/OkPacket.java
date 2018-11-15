package me.mneri.mariadb.proto.packet;

import me.mneri.mariadb.proto.*;
import me.mneri.mariadb.proto.exception.MalformedPacketException;

public class OkPacket extends Packet {
    private long affectedRows;
    private String info;
    private long lastInsertId;
    private String sessionStateInfo;
    private short statusFlags;
    private short warnings;

    @Override
    public void deserialize(PayloadReader reader) throws MalformedPacketException {
        Context context = getContext();
        int header = reader.getInt1();

        if (header != 0x00 && header != 0xFE) {
            throw new MalformedPacketException();
        }

        setAffectedRows(reader.getLengthEncodedInt());
        setLastInsertId(reader.getLengthEncodedInt());

        if (context.isCapabilitySet(Capabilities.PROTOCOL_41)) {
            setStatusFlags(reader.getInt2());
            setWarnings(reader.getInt2());
        } else if (context.isCapabilitySet(Capabilities.TRANSACTIONS)) {
            setStatusFlags(reader.getInt2());
        }

        if (context.isCapabilitySet(Capabilities.CLIENT_SESSION_TRACK)) {
            setInfo(reader.getLengthEncodedString());

            if (context.isStatusFlagSet(ServerStatus.SESSION_STATE_CHANGED)) {
                setSessionStateInfo(reader.getLengthEncodedString());
            }
        } else {
            setInfo(reader.getStringEOF());
        }
    }

    public long getAffectedRows() {
        return affectedRows;
    }

    public void setAffectedRows(long affectedRows) {
        this.affectedRows = affectedRows;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public long getLastInsertId() {
        return lastInsertId;
    }

    public void setLastInsertId(long lastInsertId) {
        this.lastInsertId = lastInsertId;
    }

    public String getSessionStateInfo() {
        return sessionStateInfo;
    }

    public void setSessionStateInfo(String sessionStateInfo) {
        this.sessionStateInfo = sessionStateInfo;
    }

    public short getStatusFlags() {
        return statusFlags;
    }

    public void setStatusFlags(short statusFlags) {
        this.statusFlags = statusFlags;
    }

    public short getWarnings() {
        return warnings;
    }

    public void setWarnings(short warnings) {
        this.warnings = warnings;
    }

    @Override
    public void serialize(PayloadWriter writer) {
        Context context = getContext();

        //@formatter:off
        writer.putInt1             ((byte) 0x00);
        writer.putLengthEncodedInt (getAffectedRows());
        writer.putLengthEncodedInt (getLastInsertId());
        //@formatter:on

        if (context.isCapabilitySet(Capabilities.PROTOCOL_41)) {
            writer.putInt2(getStatusFlags());
            writer.putInt2(getWarnings());
        } else if (context.isCapabilitySet(Capabilities.TRANSACTIONS)) {
            writer.putInt2(getStatusFlags());
        }

        if (context.isCapabilitySet(Capabilities.CLIENT_SESSION_TRACK)) {
            writer.putLengthEncodedString(getInfo());

            if (context.isStatusFlagSet(ServerStatus.SESSION_STATE_CHANGED)) {
                writer.putLengthEncodedString(getSessionStateInfo());
            }
        } else {
            writer.putFixedLengthString(getInfo(), getInfo().length());
        }
    }
}
