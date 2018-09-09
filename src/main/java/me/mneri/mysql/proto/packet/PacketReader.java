package me.mneri.mysql.proto.packet;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
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

            byte[] buff = new byte[length + 4];
            System.arraycopy(header, 0, buff, 0, 4);
            in.read(buff, 4, length);

            T packet = clazz.newInstance();
            packet.readBytes(buff);

            return packet;
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        return null;
    }
}
