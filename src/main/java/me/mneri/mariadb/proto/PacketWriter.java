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
