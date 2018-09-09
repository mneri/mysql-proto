package me.mneri.mysql.proto.packet;

import me.mneri.mysql.proto.ProtocolMismatchException;
import me.mneri.mysql.proto.util.ByteArrayBuilder;
import me.mneri.mysql.proto.util.ByteArrayReader;

public class Handshake9 extends Packet {
    private int connectionId;
    private String scramble;
    private byte sequenceId;
    private String serverVersion;

    public Handshake9(byte sequenceId) {
        this.sequenceId = sequenceId;
    }

    public int getConnectionId() {
        return connectionId;
    }

    public String getScramble() {
        return scramble;
    }

    @Override
    public byte getSequenceId() {
        return sequenceId;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    @Override
    public byte[] payloadBytes() {
        ByteArrayBuilder builder = new ByteArrayBuilder();

        //@formatter:off
        builder.putInt1                 ((byte) 9);
        builder.putNullTerminatedString (getServerVersion());
        builder.putInt4                 (getConnectionId());
        builder.putNullTerminatedString (getScramble());
        //@formatter:on

        return builder.build();
    }

    @Override
    public void readPayload(byte[] bytes) {
        ByteArrayReader reader = new ByteArrayReader(bytes);

        if (reader.getInt1() != 9)
            throw new ProtocolMismatchException();

        //@formatter:off
        setServerVersion (reader.getNullTerminatedString());
        setConnectionId  (reader.getInt4());
        setScramble      (reader.getNullTerminatedString());
        //@formatter:on
    }

    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }

    public void setScramble(String scramble) {
        this.scramble = scramble;
    }

    public void setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
    }
}
