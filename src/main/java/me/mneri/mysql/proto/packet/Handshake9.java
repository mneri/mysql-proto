package me.mneri.mysql.proto.packet;

import me.mneri.mysql.proto.Packet;
import me.mneri.mysql.proto.exception.ProtocolMismatchException;
import me.mneri.mysql.proto.util.ByteArrayBuilder;
import me.mneri.mysql.proto.util.ByteArrayReader;

public class Handshake9 extends Packet {
    private int connectionId;
    private String scramble;
    private String serverVersion;

    public int getConnectionId() {
        return connectionId;
    }

    public String getScramble() {
        return scramble;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    @Override
    public byte[] payload() {
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
    public void payload(byte[] bytes) {
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
