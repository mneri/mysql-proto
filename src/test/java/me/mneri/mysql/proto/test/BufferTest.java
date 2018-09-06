package me.mneri.mysql.proto.test;

import java.util.Arrays;
import me.mneri.mysql.proto.server.buffer.ByteArrayReader;
import me.mneri.mysql.proto.server.buffer.ByteArrayWriter;

public class BufferTest {
    public static void main(String... args) {
        byte[] buff = new byte[16];
        ByteArrayWriter writer = new ByteArrayWriter(buff);
        ByteArrayReader reader = new ByteArrayReader(buff);
        writer.lengthEncoded(1313131313);
        System.out.println(Arrays.toString(buff));
        System.out.println(reader.lengthEncoded());
    }
}
