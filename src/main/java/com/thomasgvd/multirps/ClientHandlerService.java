package com.thomasgvd.multirps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandlerService implements Runnable {

    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    public ClientHandlerService(Socket socket) {
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
                } else {
                    System.out.println("bad request"); // Discard packet
                }
            } while (true);
        } catch (IOException e) {
            disconnect();
            e.printStackTrace();
        }
    }

    private String handleConnection(String userName, String password) {
        StringBuilder response = new StringBuilder("");
        boolean authenticated = authenticateUser(userName, password);
        response.append(PacketType.CONNECTION.getValue()).append("|").append(authenticated ? "1" : "0");
        return response.toString();
    }

    private boolean authenticateUser(String userName, String password) {
        return userName.equals("user1") && password.equals("pass1") ||
                userName.equals("user2") && password.equals("pass2") ||
                userName.equals("user3") && password.equals("pass3");
    }

    private void disconnect() {
        System.out.println("disconnect socket" + socket);
        try { reader.close(); } catch (Exception e) { e.printStackTrace(); }
        try { writer.close(); } catch (Exception e) { e.printStackTrace(); }
        try { socket.close(); } catch (Exception e) { e.printStackTrace(); }
    }
}
