package com.bielanm.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer implements Server{

    private boolean isAlive;
    private Integer port;
    private ConnectionHandler сonnectionHandler;

    public SocketServer(Integer port, ConnectionHandler сonnectionHandler) {
        this.port = port;
        this.сonnectionHandler = сonnectionHandler;
    }

    @Override
    public void start() {
        try {
            ServerSocket server = new ServerSocket(port);
            isAlive = true;
            while(isAlive) {
                try (Socket socket = server.accept();
                     InputStream inputStream = socket.getInputStream();
                     OutputStream outputStream = socket.getOutputStream()) {
                    сonnectionHandler.handle(inputStream, outputStream);
                } catch (Exception exception) {
                    System.out.println(exception.getMessage());
                }
            }
        } catch (IOException e) {
            сonnectionHandler.shutDown();
            System.out.println("Server crashed");
        }
    }

    @Override
    public void stop() {
        сonnectionHandler.shutDown();
        System.out.println("Server stop work");
    }
}
