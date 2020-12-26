package com.thomasgvd.multirps.networking;

import com.thomasgvd.multirps.models.MyUser;
import com.thomasgvd.multirps.models.PacketType;
import com.thomasgvd.multirps.models.Response;
import com.thomasgvd.multirps.services.ResponseService;
import com.thomasgvd.multirps.services.UserService;

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
    private ResponseService responseService;

    public ClientHandler(Server server, Socket socket, UserService userService, ResponseService responseService) {
        this.server = server;
        this.socket = socket;
        this.userService = userService;
        this.responseService = responseService;
    }

    @Override
    public void run() {
        try {
            do {
                reader = createReader();
                String input = reader.readLine();
                System.out.println("packet received : " + input);
                String[] inputArray = input.split("\\|");

                Response response = new Response();
                response = responseService.manageInput(inputArray, response, server.getUsers(), user);

                if (response.getType() == PacketType.CONNECTION.getValue() && response.isSuccessful()) {
                    user = userService.getUser(server.getUsers(), inputArray[1]);
                    user.setSocket(socket);
                }

                manageOutput(response);
            } while (true);
        } catch (IOException e) {
            disconnect();
            e.printStackTrace();
        }
    }

    private BufferedReader createReader() throws IOException {
        return new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    private void manageOutput(Response response) throws IOException {
        if (response.isRespondToAll()) {
            List<MyUser> connectedUsers = server.getUsers().stream()
                    .filter(u -> u.getSocket() != null)
                    .collect(Collectors.toList());

            for (MyUser u : connectedUsers) {
                sendResponse(u.getSocket(), response.getContent());
            }
        } else if (response.isRespondToOne()) {
            sendResponse(socket, response.getContent());
        }
    }

    private void sendResponse(Socket s, String r) throws IOException {
        System.out.println("============ response : " + r);
        PrintWriter writer = createWriter(s);
        writer.write(r);
        writer.flush();
    }

    private PrintWriter createWriter(Socket s) throws IOException {
        return new PrintWriter(s.getOutputStream());
    }

    private void disconnect() {
        System.out.println("disconnect socket" + socket);
        try { reader.close(); } catch (Exception e) { e.printStackTrace(); }
        try { socket.close(); user.setSocket(null); } catch (Exception e) { e.printStackTrace(); }
    }
}
