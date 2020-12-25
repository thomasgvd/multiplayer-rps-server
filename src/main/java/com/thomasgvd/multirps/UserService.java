package com.thomasgvd.multirps;

import java.util.Optional;

import static com.thomasgvd.multirps.Server.PLAYER_SPEED;

public class UserService {

    private static UserService instance = null;

    public static UserService getInstance() {
        if (instance == null) instance = new UserService();
        return instance;
    }

    private UserService() {}

    public String handleMovement(MyUser user, int xMovement, int yMovement) {
        StringBuilder response = new StringBuilder("");

        user.setPosX(user.getPosX() + (xMovement * PLAYER_SPEED));
        user.setPosY(user.getPosY() + (yMovement * PLAYER_SPEED));

        response.append(PacketType.MOVEMENT.getValue()).append("|")
                .append(user.getUserName()).append("|")
                .append(user.getPosX()).append("|")
                .append(user.getPosY());

        return response.toString();
    }

    public String handleSuccessfulConnection(MyUser user) {
        return PacketType.CONNECTION.getValue() + "|" +
            user.getUserName() + "|" +
            "1|" +
            user.getPosX() + "|" +
            user.getPosY();
    }

    public String handleFailedConnection(String userName) {
        return PacketType.CONNECTION.getValue() + "|" + userName + "|0|0|0";
    }

    public boolean authenticateUser(Server server, String userName, String password) {
        Optional<MyUser> userOptional = server.getUsers().stream()
                .filter(u -> u.getUserName().equals(userName) && u.getPassword().equals(password))
                .findAny();

        return userOptional.isPresent();
    }

    public MyUser getUser(Server server, String userName) {
        return server.getUsers().stream()
                .filter(u -> u.getUserName().equals(userName))
                .findFirst()
                .orElse(null);
    }
}
