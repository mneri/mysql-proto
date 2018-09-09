package me.mneri.mysql.proto.packet;

public class ComQuit extends Packet {
    private byte sequenceId;

    public ComQuit(byte sequenceId) {
        this.sequenceId = sequenceId;
    }

    @Override
    public byte getSequenceId() {
        return sequenceId;
    }

    @Override
    public byte[] payload() {
        return new byte[]{0x01};
    }

    @Override
    public void payload(byte[] bytes) {
        // Ignore the payload
    }
}
