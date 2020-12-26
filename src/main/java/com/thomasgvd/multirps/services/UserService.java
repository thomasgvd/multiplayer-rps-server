package com.thomasgvd.multirps.services;

import com.thomasgvd.multirps.models.MyUser;
import com.thomasgvd.multirps.models.PacketType;

import java.util.Optional;
import java.util.Set;

import static com.thomasgvd.multirps.networking.Server.PLAYER_SPEED;

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

    public boolean authenticateUser(Set<MyUser> users, String userName, String password) {
        Optional<MyUser> userOptional = users.stream()
                .filter(u -> u.getUserName().equals(userName) && u.getPassword().equals(password))
                .findAny();

        return userOptional.isPresent();
    }

    public MyUser getUser(Set<MyUser> users, String userName) {
        return users.stream()
                .filter(u -> u.getUserName().equals(userName))
                .findFirst()
                .orElse(null);
    }
}
