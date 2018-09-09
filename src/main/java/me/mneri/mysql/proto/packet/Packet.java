package me.mneri.mysql.proto.packet;

import me.mneri.mysql.proto.exception.MalformedPacketException;

public abstract class Packet {
    public abstract byte getSequenceId();

    public abstract byte[] payload() throws MalformedPacketException;

    public abstract void payload(byte[] bytes) throws MalformedPacketException;

    @Override
    public String toString() {
        try {
            StringBuilder sb = new StringBuilder();
            byte[] payload = payload();

            for (int i = 0; i < payload.length; i++) {
                sb.append(String.format("%02X", payload[i]));
                sb.append((i != 0 && i % 8 == 0) ? "   " : " ");
            }

            return sb.toString();
        } catch (MalformedPacketException e) {
            return "Malformed packet";
        }
    }
}
