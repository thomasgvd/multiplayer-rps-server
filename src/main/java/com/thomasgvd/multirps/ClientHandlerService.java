package com.thomasgvd.multirps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.thomasgvd.multirps.Server.PLAYER_SPEED;

public class ClientHandlerService implements Runnable {

    private Socket socket;
    private Server server;
    private BufferedReader reader;
    private MyUser user;

    public ClientHandlerService(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            do {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String input = reader.readLine();
                System.out.println("packet received : " + input);

                String[] inputArray = input.split("\\|");

                int packetType = Integer.parseInt(inputArray[0]);
                boolean sendResponse = false;
                boolean sendResponseToAll = false;
                String response = "";

                if (packetType == PacketType.CONNECTION.getValue() && inputArray.length == 3) {
                    System.out.println("handle connection request");
                    response = handleConnection(inputArray[1], inputArray[2]);
                    sendResponse = true;
                } else if (packetType == PacketType.MOVEMENT.getValue() && inputArray.length == 4) {
                    System.out.println("handle movement request");
                    response = handleMovement(Integer.parseInt(inputArray[2]), Integer.parseInt(inputArray[3]));
                    sendResponseToAll = true;
                } else {
                    System.out.println("bad request"); // Discard packet
                }

                if (sendResponseToAll) {
                    List<MyUser> connectedUsers = server.getUsers().stream()
                            .filter(u -> u.getSocket() != null)
                            .collect(Collectors.toList());

                    for (MyUser u : connectedUsers) {
                        System.out.println("============ respond to " + u.getUserName() + " with : " + response);
                        sendResponse(u.getSocket(), response);
                    }
                } else if (sendResponse) {
                    System.out.println("============ respond to " + user.getUserName() + " with : " + response);
                    sendResponse(socket, response);
                }

            } while (true);
        } catch (IOException e) {
            disconnect();
            e.printStackTrace();
        }
    }

    private void sendResponse(Socket s, String r) throws IOException {
        PrintWriter writer = new PrintWriter(s.getOutputStream());
        writer.write(r);
        writer.flush();
    }

    private String handleMovement(int xMovement, int yMovement) {
        StringBuilder response = new StringBuilder("");

        user.setPosX(user.getPosX() + (xMovement * PLAYER_SPEED));
        user.setPosY(user.getPosY() + (yMovement * PLAYER_SPEED));

        response.append(PacketType.MOVEMENT.getValue()).append("|")
                .append(user.getUserName()).append("|")
                .append(user.getPosX()).append("|")
                .append(user.getPosY());

        return response.toString();
    }

    private String handleConnection(String userName, String password) {
        StringBuilder response = new StringBuilder("");
        boolean authenticated = authenticateUser(userName, password);
        response.append(PacketType.CONNECTION.getValue()).append("|")
                .append(user.getUserName()).append("|")
                .append(authenticated ? "1" : "0").append("|")
                .append(user.getPosX()).append("|")
                .append(user.getPosY());
        return response.toString();
    }

    private boolean authenticateUser(String userName, String password) {
        Optional<MyUser> userOptional = server.getUsers().stream()
                .filter(u -> u.getUserName().equals(userName) && u.getPassword().equals(password))
                .findAny();

        if (userOptional.isPresent()) {
            user = userOptional.get();
            user.setSocket(socket);
            return true;
        }

        return false;
    }

    private void disconnect() {
        System.out.println("disconnect socket" + socket);
        try { reader.close(); } catch (Exception e) { e.printStackTrace(); }
        try { socket.close(); user.setSocket(null); } catch (Exception e) { e.printStackTrace(); }
    }
}
