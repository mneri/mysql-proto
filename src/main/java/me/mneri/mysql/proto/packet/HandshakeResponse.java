package me.mneri.mysql.proto.packet;

import me.mneri.mysql.proto.util.ByteArrayReader;

public class HandshakeResponse extends Packet {
    private byte sequenceId;

    @Override
    public byte getSequenceId() {
        return sequenceId;
    }

    @Override
    public byte[] payloadBytes() {
        return new byte[0];
    }

    private int capabilities;
    private int maxPacketSize;
    private int characterSet;
    private String username;

    @Override
    public void readPayload(byte[] bytes) {
        ByteArrayReader reader = new ByteArrayReader(bytes);

        //@formatter:off
        capabilities  = reader.getInt4();
        maxPacketSize = reader.getInt4();
        characterSet  = reader.getInt1();
                        reader.skip(23);
        username      = reader.getNullTerminatedString();
        //@formatter:on

    }

    @Override
    public void setSequenceId(byte sequenceId) {
        this.sequenceId = sequenceId;
    }
}
