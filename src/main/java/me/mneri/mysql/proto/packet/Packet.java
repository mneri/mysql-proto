package me.mneri.mysql.proto.packet;

public abstract class Packet {
    public abstract int getPayloadLength();

    public abstract int getSequenceId();

    public abstract byte[] toByteArray();
}
