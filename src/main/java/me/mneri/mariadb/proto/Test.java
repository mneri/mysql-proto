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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {
    public static void main(String... args) {
        try {
            ExecutorService service = Executors.newCachedThreadPool();
            ServerSocket serverSocket = new ServerSocket(5050);

            while (true) {
                Socket socket = serverSocket.accept();

                service.submit(() -> {
                    try {
                        ServerProtocol.start(socket.getInputStream(), socket.getOutputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        //@formatter:off
                        try { socket.close(); } catch (Exception ignored) { }
                        //@formatter:on
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
