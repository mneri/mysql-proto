package me.mneri.mysql.proto.packet;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import java.lang.reflect.InvocationTargetException;

import me.mneri.mysql.proto.exception.InternalProtocolException;
import me.mneri.mysql.proto.util.ByteArrayReader;

public class PacketReader implements Closeable {
    private InputStream in;

    public PacketReader(InputStream in) {
        this.in = in;
    }

    @Override
    public void close() throws IOException {
        in.close();
    }

    public <T extends Packet> T read(Class<T> clazz) throws IOException {
        try {
            byte[] header = new byte[4];

            in.read(header);

            ByteArrayReader reader = new ByteArrayReader(header);
            int length = reader.getInt3();
            byte sequenceId = reader.getInt1();

            T packet = clazz.getConstructor(byte.class).newInstance(sequenceId);

            byte[] buff = new byte[length];
            in.read(buff, 0, length);

            packet.payload(buff);

            return packet;
            //@formatter:off
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            //@formatter:on
            throw new InternalProtocolException(e);
        }
    }
}
