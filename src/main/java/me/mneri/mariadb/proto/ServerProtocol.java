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

import me.mneri.mariadb.exception.MalformedPacketException;
import me.mneri.mariadb.packet.HandshakePacket;
import me.mneri.mariadb.packet.HandshakeResponsePacket;
import me.mneri.mariadb.packet.OkPacket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static me.mneri.mariadb.proto.Capabilities.PLUGIN_AUTH;
import static me.mneri.mariadb.proto.Capabilities.SECURE_CONNECTION;

public class ServerProtocol {
    private Context context;

    private ServerProtocol(InputStream in, OutputStream out) {
        context = new Context(in, out);
    }

    private void start() {
        try {
            //@formatter:off
            HandshakePacket handshake = context.create(HandshakePacket.class, (byte) 0);

            handshake.setServerVersion  ("5.5.2-m2");
            handshake.setConnectionId   (0);
            handshake.setAuthPluginData ("dvH@I-CJ*4d|cZwk4^]:.");
            handshake.setCapabilities   (0xf5ff | PLUGIN_AUTH | SECURE_CONNECTION);
            handshake.setCharacterSet   (0x08);
            handshake.setServerStatus   (ServerStatus.AUTOCOMMIT);
            handshake.setAuthPluginName ("mysql_native_password");

            context.send(handshake);
            //@formatter:on

            HandshakeResponsePacket handshakeResponse = context.receive(HandshakeResponsePacket.class);
            handshakeResponse.toString();

            OkPacket ok = context.create(OkPacket.class, (byte) 2);
            ok.setInfo("Login successful");
            context.send(ok);
            System.out.println("end");
        } catch (IOException | MalformedPacketException e) {
            e.printStackTrace();
        }
    }

    public static void start(InputStream in, OutputStream out) {
        ServerProtocol proto = new ServerProtocol(in, out);
        proto.start();
    }
}
