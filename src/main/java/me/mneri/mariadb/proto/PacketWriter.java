package me.mneri.mariadb.proto;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

class PacketWriter implements Closeable {
    private Context context;

    PacketWriter(Context context) {
        this.context = context;
    }

    @Override
    public void close() throws IOException {
        context.getOutputStream().close();
    }

    void write(Packet packet) throws IOException {
        PayloadWriter writer = new PayloadWriter();
        byte[] payload = writer.build();

        writer = new PayloadWriter(4);
        writer.putInt3(payload.length);
        writer.putInt1(packet.getSequenceId());
        byte[] header = writer.build();

        OutputStream out = context.getOutputStream();
        out.write(header);
        out.write(payload);
        out.flush();
    }
}
