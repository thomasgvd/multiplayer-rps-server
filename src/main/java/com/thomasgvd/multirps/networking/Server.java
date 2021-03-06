package com.thomasgvd.multirps.networking;

import com.thomasgvd.multirps.models.MyUser;
import com.thomasgvd.multirps.services.ResponseService;
import com.thomasgvd.multirps.services.UserService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Server {
    private int port;
    private boolean listening;
    private ServerSocket serverSocket;
    private Socket socket;
    private Set<MyUser> users;
    private UserService userService;
    private ResponseService responseService;

    public final static int PLAYER_SPEED = 5;

    public Server(int port, UserService userService, ResponseService responseService) {
        users = new HashSet<>();
        users.add(new MyUser("user1", "pass1"));
        users.add(new MyUser("user2", "pass2"));
        users.add(new MyUser("user3", "pass3"));

        this.port = port;
        this.userService = userService;
        this.responseService = responseService;
    }

    public void start() {
        setListening(true);

        try {
            serverSocket = new ServerSocket(port);

            do {
                System.out.println("accept connections");
                socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(this, socket, userService, responseService);
                Thread clientThread = new Thread(clientHandler, "Client Handler");
                clientThread.start();
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

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Set<MyUser> getUsers() {
        return users;
    }

    public void setUsers(Set<MyUser> users) {
        this.users = users;
    }
}
