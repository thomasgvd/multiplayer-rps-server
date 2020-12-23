package com.thomasgvd.multirps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Optional;

import static com.thomasgvd.multirps.Server.PLAYER_SPEED;

public class ClientHandlerService implements Runnable {

    private Socket socket;
    private Server server;
    private PrintWriter writer;
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
                writer = new PrintWriter(socket.getOutputStream());
                String input = reader.readLine();
                System.out.println("packet received : " + input);

                String[] inputArray = input.split("\\|");

                int packetType = Integer.parseInt(inputArray[0]);

                if (packetType == PacketType.CONNECTION.getValue() && inputArray.length == 3) {
                    System.out.println("handle connection request");
                    String response = handleConnection(inputArray[1], inputArray[2]);
                    System.out.println("respond with : " + response);
                    writer.write(response);
                    writer.flush();
                } else if (packetType == PacketType.MOVEMENT.getValue() && inputArray.length == 3) {
                    System.out.println("handle movement request");
                    String response = handleMovement(Integer.parseInt(inputArray[1]), Integer.parseInt(inputArray[2]));
                    System.out.println("respond with : " + response);
                    writer.write(response);
                    writer.flush();
                } else {
                    System.out.println("bad request"); // Discard packet
                }
            } while (true);
        } catch (IOException e) {
            disconnect();
            e.printStackTrace();
        }
    }

    private String handleMovement(int xMovement, int yMovement) {
        StringBuilder response = new StringBuilder("");
        System.out.println("Movement : " + xMovement + " - " + yMovement);

        user.setPosX(user.getPosX() + (xMovement * PLAYER_SPEED));
        user.setPosY(user.getPosY() + (yMovement * PLAYER_SPEED));

        response.append(PacketType.MOVEMENT.getValue()).append("|")
                .append(user.getPosX()).append("|")
                .append(user.getPosY());

        return response.toString();
    }

    private String handleConnection(String userName, String password) {
        StringBuilder response = new StringBuilder("");
        boolean authenticated = authenticateUser(userName, password);
        response.append(PacketType.CONNECTION.getValue()).append("|").append(authenticated ? "1" : "0");
        return response.toString();
    }

    private boolean authenticateUser(String userName, String password) {
        Optional<MyUser> userOptional = server.getUsers().stream()
                .filter(u -> u.getUserName().equals(userName) && u.getPassword().equals(password))
                .findAny();

        if (userOptional.isPresent()) {
            user = userOptional.get();
            return true;
        }

        return false;
    }

    private void disconnect() {
        System.out.println("disconnect socket" + socket);
        try { reader.close(); } catch (Exception e) { e.printStackTrace(); }
        try { writer.close(); } catch (Exception e) { e.printStackTrace(); }
        try { socket.close(); } catch (Exception e) { e.printStackTrace(); }
    }
}
