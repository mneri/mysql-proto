package me.mneri.mysql.proto.packet;

import me.mneri.mysql.proto.util.ByteArrayReader;

public class HandshakeResponse320 extends Packet {
    private byte sequenceId;

    public HandshakeResponse320(byte sequenceId) {
        this.sequenceId = sequenceId;
    }

    @Override
    public byte getSequenceId() {
        return sequenceId;
    }

    @Override
    public byte[] payloadBytes() {
        return new byte[0];
    }

    @Override
    public void readPayload(byte[] bytes) {
        ByteArrayReader reader = new ByteArrayReader(bytes);
    }
}
