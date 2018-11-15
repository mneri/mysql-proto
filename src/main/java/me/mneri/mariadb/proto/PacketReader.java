/*
 * Copyright 2018 Massimo Neri <hello@mneri.me>
 *
 * This file is part of mariadb-proto.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.mneri.mariadb.proto;

import me.mneri.mariadb.exception.InternalProtocolException;
import me.mneri.mariadb.exception.MalformedPacketException;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

class PacketReader implements Closeable {
    private Context context;

    PacketReader(Context context) {
        this.context = context;
    }

    @Override
    public void close() throws IOException {
        context.getInputStream().close();
    }

    <T extends Packet> T read(Class<T> clazz) throws IOException, MalformedPacketException {
        try {
            InputStream in = context.getInputStream();
            byte[] header = new byte[4];

            in.read(header);

            PayloadReader reader = new PayloadReader(header);
            int length = reader.getInt3();
            byte sequenceId = reader.getInt1();

            T packet = clazz.newInstance();
            packet.setSequenceId(sequenceId);
            packet.setContext(context);

            byte[] buff = new byte[length];
            in.read(buff, 0, length);
            reader = new PayloadReader(buff);

            packet.deserialize(reader);

            return packet;
            //@formatter:off
        } catch (IllegalAccessException | InstantiationException e) {
            //@formatter:on
            throw new InternalProtocolException(e);
        }
    }
}
