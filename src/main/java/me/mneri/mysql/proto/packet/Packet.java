package me.mneri.mysql.proto.packet;

public interface Packet {
    void readBytes(byte[] bytes);

    byte[] toByteArray();
}
