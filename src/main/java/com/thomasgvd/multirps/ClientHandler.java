package com.thomasgvd.multirps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;

public class ClientHandler implements Runnable {

    private Socket socket;
    private Server server;
    private BufferedReader reader;
    private MyUser user;
    private UserService userService;

    public ClientHandler(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
        this.userService = UserService.getInstance();
    }

    @Override
    public void run() {
        try {
            do {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String input = reader.readLine();
                System.out.println("packet received : " + input);
                manageInput(input);
            } while (true);
        } catch (IOException e) {
            disconnect();
            e.printStackTrace();
        }
    }

    private void manageInput(String input) throws IOException {
        String[] inputArray = input.split("\\|");
        int packetType = Integer.parseInt(inputArray[0]);
        boolean sendResponse = false;
        boolean sendResponseToAll = false;
        String response = "";

        if (isConnection(packetType, inputArray)) {
            String userName = inputArray[1];
            String password = inputArray[2];
            response = manageConnection(userName, password);
            sendResponse = true;
        } else if (isMovement(packetType, inputArray)) {
            System.out.println("handle movement request");
            response = userService.handleMovement(user, Integer.parseInt(inputArray[2]), Integer.parseInt(inputArray[3]));
            sendResponseToAll = true;
        } else {
            System.out.println("bad request"); // Discard packet
        }

        manageOutput(sendResponseToAll, sendResponse, response);
    }

    private boolean isConnection(int packetType, String[] inputArray) {
        return packetType == PacketType.CONNECTION.getValue() && inputArray.length == 3;
    }

    private boolean isMovement(int packetType, String[] inputArray) {
        return packetType == PacketType.MOVEMENT.getValue() && inputArray.length == 4;
    }

    private String manageConnection(String userName, String password) {
        System.out.println("handle connection request");
        if (userService.authenticateUser(server, userName, password)) {
            user = userService.getUser(server, userName);
            user.setSocket(socket);
            return userService.handleSuccessfulConnection(user);
        } else {
            return userService.handleFailedConnection(userName);
        }
    }

    private void manageOutput(boolean sendResponseToAll, boolean sendResponse, String response) throws IOException {
        if (sendResponseToAll) {
            List<MyUser> connectedUsers = server.getUsers().stream()
                    .filter(u -> u.getSocket() != null)
                    .collect(Collectors.toList());

            for (MyUser u : connectedUsers) {
                sendResponse(u.getSocket(), response);
            }
        } else if (sendResponse) {
            sendResponse(socket, response);
        }
    }

    private void sendResponse(Socket s, String r) throws IOException {
        System.out.println("============ response : " + r);
        PrintWriter writer = new PrintWriter(s.getOutputStream());
        writer.write(r);
        writer.flush();
    }

    private void disconnect() {
        System.out.println("disconnect socket" + socket);
        try { reader.close(); } catch (Exception e) { e.printStackTrace(); }
        try { socket.close(); user.setSocket(null); } catch (Exception e) { e.printStackTrace(); }
    }
}
