package com.thomasgvd.multirps;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private int port;
    private boolean listening;
    private ServerSocket serverSocket;
    private Socket socket;

    public Server(int port) {
        this.port = port;
    }

    public Server() {}

    public void start() {
        setListening(true);

        try {
            serverSocket = new ServerSocket(port);

            do {
                System.out.println("accept connections");
                socket = serverSocket.accept();
                Thread clientHandler = new Thread(new ClientHandlerService(socket), "Client Handler");
                clientHandler.start();
            } while (listening);
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean isListening() {
        return listening;
    }

    public void setListening(boolean listening) {
        this.listening = listening;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
