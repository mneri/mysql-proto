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
    private int statusFlags;
    private PacketWriter writer;

    Context(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
        reader = new PacketReader(this);
        writer = new PacketWriter(this);
    }

    public <T extends Packet> T create(Class<T> clazz, byte sequenceId) {
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
        return (statusFlags & statusFlag) != 0;
    }

    public <T extends Packet> T read(Class<T> clazz) throws IOException, MalformedPacketException {
        return reader.read(clazz);
    }

    public void setCapability(int capability) {
        capabilities |= capability;
    }

    public void setStatusFlag(int statusFlag) {
        statusFlags |= statusFlag;
    }

    public void unsetCapability(int capability) {
        capabilities ^= capability;
    }

    public void unsetStatusFlag(int statusFlag) {
        statusFlags ^= statusFlag;
    }

    public void write(Packet packet) throws IOException, MalformedPacketException {
        writer.write(packet);
    }
}
