package me.mneri.mysql.proto.packet;

public abstract class Packet {
    public abstract byte getSequenceId();

    public abstract byte[] payloadBytes();

    public abstract void readPayload(byte[] bytes);

    public abstract void setSequenceId(byte sequenceId);

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        byte[] payload = payloadBytes();

        for (int i = 0; i < payload.length; i++) {
            sb.append(String.format("%02X", payload[i]));
            sb.append((i != 0 && i % 8 == 0) ? "   " : " ");
        }

        return sb.toString();
    }
}
