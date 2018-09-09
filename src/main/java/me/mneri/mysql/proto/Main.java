package me.mneri.mysql.proto;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String... args) {
        try {
            ExecutorService service = Executors.newCachedThreadPool();
            ServerSocket serverSocket = new ServerSocket(5050);

            while (true) {
                Socket socket = serverSocket.accept();

                service.submit(() -> {
                    try {
                        MySQLServerProtocol.start(socket.getInputStream(), socket.getOutputStream());
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
