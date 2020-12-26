package com.thomasgvd.multirps.services;

import com.thomasgvd.multirps.models.MyUser;
import com.thomasgvd.multirps.models.PacketType;
import com.thomasgvd.multirps.models.Response;

import java.io.IOException;
import java.util.Set;

public class ResponseService {

    private static ResponseService instance = null;
    private UserService userService;

    public static ResponseService getInstance() {
        if (instance == null) instance = new ResponseService(UserService.getInstance());
        return instance;
    }

    private ResponseService(UserService userService) {
        this.userService = userService;
    }

    public Response manageInput(String[] inputArray, Set<MyUser> users, MyUser user) throws IOException {
        int packetType = Integer.parseInt(inputArray[0]);
        Response response = new Response(packetType);

        if (isConnection(packetType, inputArray)) {
            String userName = inputArray[1];
            String password = inputArray[2];
            response.setContent(manageConnection(users, response, userName, password));
            response.setRespondToOne(true);
        } else if (isMovement(packetType, inputArray)) {
            System.out.println("handle movement request");

            int xMovement = Integer.parseInt(inputArray[2]);
            int yMovement = Integer.parseInt(inputArray[3]);

            response.setContent(userService.handleMovement(user, xMovement, yMovement));
            response.setRespondToAll(true);
            response.setSuccessful(true);
        } else {
            System.out.println("bad request"); // Discard packet
        }

        return response;
    }

    private boolean isConnection(int packetType, String[] inputArray) {
        return packetType == PacketType.CONNECTION.getValue() && inputArray.length == 3;
    }

    private boolean isMovement(int packetType, String[] inputArray) {
        return packetType == PacketType.MOVEMENT.getValue() && inputArray.length == 4;
    }

    private String manageConnection(Set<MyUser> users, Response response, String userName, String password) {
        System.out.println("handle connection request");
        if (userService.authenticateUser(users, userName, password)) {
            MyUser user = userService.getUser(users, userName);
            response.setSuccessful(true);
            return userService.handleSuccessfulConnection(user);
        } else {
            response.setSuccessful(false);
            return userService.handleFailedConnection(userName);
        }
    }

}
