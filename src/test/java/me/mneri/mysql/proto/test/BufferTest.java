package me.mneri.mysql.proto.test;

import java.util.Arrays;
import me.mneri.mysql.proto.server.buffer.BufferReader;
import me.mneri.mysql.proto.server.buffer.BufferWriter;

public class BufferTest {
    public static void main(String... args) {
        byte[] buff = new byte[16];
        BufferWriter writer = new BufferWriter(buff);
        BufferReader reader = new BufferReader(buff);
        writer.lengthEncoded(1313131313);
        System.out.println(Arrays.toString(buff));
        System.out.println(reader.lengthEncoded());
    }
}
