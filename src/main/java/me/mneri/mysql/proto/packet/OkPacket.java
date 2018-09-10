package me.mneri.mysql.proto.packet;

import me.mneri.mysql.proto.Context;
import me.mneri.mysql.proto.Packet;
import me.mneri.mysql.proto.exception.MalformedPacketException;
import me.mneri.mysql.proto.flag.ServerStatus;
import me.mneri.mysql.proto.util.ByteArrayBuilder;
import me.mneri.mysql.proto.util.ByteArrayReader;

import static me.mneri.mysql.proto.flag.Capabilities.*;

public class OkPacket extends Packet {
    private long affectedRows;
    private String info;
    private long lastInsertId;
    private byte sequenceId;
    private String sessionStateInfo;
    private short statusFlags;
    private short warnings;

    public OkPacket(byte sequenceId) {
        this.sequenceId = sequenceId;
    }

    public long getAffectedRows() {
        return affectedRows;
    }

    public String getInfo() {
        return info;
    }

    public long getLastInsertId() {
        return lastInsertId;
    }

    @Override
    public byte getSequenceId() {
        return sequenceId;
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
    public byte[] payload() throws MalformedPacketException {
        ByteArrayBuilder builder = new ByteArrayBuilder();

        builder.putInt1((byte) 0x00);
        builder.putLengthEncodedInt(getAffectedRows());
        builder.putLengthEncodedInt(getLastInsertId());

        if (false) { // capabilities
            builder.putInt2(getStatusFlags());
            builder.putInt2(getWarnings());
        } else if (false) { // client transaction
            builder.putInt2(getStatusFlags());
        }

        if (false) { // client session track
            builder.putLengthEncodedString(getInfo());

            if (false) { // server session state changed
                builder.putLengthEncodedString(getSessionStateInfo());
            }
        } else {
            builder.putFixedLengthString(getInfo(), getInfo().length());
        }

        return builder.build();
    }

    @Override
    public void payload(byte[] bytes) throws MalformedPacketException {
        ByteArrayReader reader = new ByteArrayReader(bytes);

        int header = reader.getInt1();

        if (header != 0x00 && header != 0xFE)
            throw new MalformedPacketException();

        setAffectedRows(reader.getLengthEncodedInt());
        setLastInsertId(reader.getLengthEncodedInt());

        Context context = getContext();

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
