package me.mneri.mariadb.proto.packet;

import me.mneri.mariadb.proto.Packet;
import me.mneri.mariadb.proto.exception.ProtocolMismatchException;
import me.mneri.mariadb.proto.util.ByteArrayWriter;
import me.mneri.mariadb.proto.util.ByteArrayReader;

public class Handshake9Packet extends Packet {
    private int connectionId;
    private String scramble;
    private String serverVersion;

    @Override
    public void deserialize(byte[] payload) {
        ByteArrayReader reader = new ByteArrayReader(payload);

        if (reader.getInt1() != 9) {
            throw new ProtocolMismatchException();
        }

        //@formatter:off
        setServerVersion (reader.getNullTerminatedString());
        setConnectionId  (reader.getInt4());
        setScramble      (reader.getNullTerminatedString());
        //@formatter:on
    }

    public int getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }

    public String getScramble() {
        return scramble;
    }

    public void setScramble(String scramble) {
        this.scramble = scramble;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
    }

    @Override
    public byte[] serialize() {
        ByteArrayWriter builder = new ByteArrayWriter();

        //@formatter:off
        builder.putInt1                 ((byte) 9);
        builder.putNullTerminatedString (getServerVersion());
        builder.putInt4                 (getConnectionId());
        builder.putNullTerminatedString (getScramble());
        //@formatter:on

        return builder.build();
    }
}
