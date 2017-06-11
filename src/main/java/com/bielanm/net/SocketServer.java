package com.bielanm.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer implements Server{

    public static final String html = "<html><head><title>Welcome to the Amazing Site!</title></head><body><p>This site is under construction. Please come back later. Sorry!</p></body></html>";
    public static final String response = "HTTP/1.1 200 OK\r\nServer: YarServer/2009-09-09\r\nContent-Type: text/html\r\nContent-Length: " + html.length() + "\r\nConnection: close\r\n\r\n";
    public static final String result = response + html;

    private boolean isAlive;
    private Integer port;
    private ConnectionHandler сonnectionHandler;

    public SocketServer(Integer port) {
        this.port = port;
    }

    public void setСonnectionHandler(ConnectionHandler сonnectionHandler) {
        this.сonnectionHandler = сonnectionHandler;
    }

    @Override
    public void start() {
        if(this.сonnectionHandler == null) {
            throw new IllegalStateException("Set up connectionHandler for server!...");
        }

        try(ServerSocket server = new ServerSocket(port); ConnectionHandler autoclose = сonnectionHandler) {
            isAlive = true;
            System.out.println("Server started on port " + port);
            while(isAlive) {
                Socket socket = server.accept();
                try {
                    сonnectionHandler.handle(socket);
                } catch (IOException exc) {
                    System.out.println("IO exception");
                    socket.close();
                }
            }
        } catch (IOException e) {
            System.out.println("ServerSocket IOException");
        } catch (Exception exc) {
            System.out.println("Runtime application exception");
        }
    }

    @Override
    public void close() throws Exception {
        System.out.println("Server close work");
        сonnectionHandler.close();
        isAlive = false;
    }
}
