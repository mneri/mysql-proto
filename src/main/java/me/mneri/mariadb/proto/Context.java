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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

    public boolean isCapabilitySet(long capability) {
        return (capabilities & capability) != 0;
    }

    public boolean isStatusFlagSet(int statusFlag) {
        return (status & statusFlag) != 0;
    }

    <T extends Packet> T receive(Class<T> clazz) throws IOException, MalformedPacketException {
        return reader.read(clazz);
    }

    void send(Packet packet) throws IOException {
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
