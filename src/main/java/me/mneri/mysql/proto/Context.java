package me.mneri.mysql.proto;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import me.mneri.mysql.proto.exception.InternalProtocolException;
import me.mneri.mysql.proto.exception.MalformedPacketException;

public class Context {
    private int capabilities;
    private InputStream in;
    private OutputStream out;
    private PacketReader reader;
    private int status;
    private PacketWriter writer;

    Context(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
        reader = new PacketReader(this);
        writer = new PacketWriter(this);
    }

    <T extends Packet> T create(Class<T> clazz, byte sequenceId) {
        try {
            T packet = clazz.newInstance();
            packet.setSequenceId(sequenceId);
            packet.setContext(this);

            return packet;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new InternalProtocolException();
        }
    }

    InputStream getInputStream() {
        return in;
    }

    OutputStream getOutputStream() {
        return out;
    }

    public boolean isCapabilitySet(int capability) {
        return (capabilities & capability) != 0;
    }

    public boolean isStatusFlagSet(int statusFlag) {
        return (status & statusFlag) != 0;
    }

    <T extends Packet> T receive(Class<T> clazz) throws IOException, MalformedPacketException {
        return reader.read(clazz);
    }

    void send(Packet packet) throws IOException, MalformedPacketException {
        writer.write(packet);
    }

    void setCapability(int capability) {
        capabilities |= capability;
    }

    void setStatus(int status) {
        this.status |= status;
    }

    void unsetCapability(int capability) {
        capabilities ^= capability;
    }

    void unsetStatusFlag(int statusFlag) {
        status ^= statusFlag;
    }
}
