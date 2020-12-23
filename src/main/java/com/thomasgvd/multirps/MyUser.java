package com.thomasgvd.multirps;

import java.net.Socket;

public class MyUser {

    private int id;
    private String userName;
    private String password;
    private int wins;
    private int losses;
    private int posX;
    private int posY;
    private Socket socket;

    public MyUser(String userName, String password) {
        this.userName = userName;
        this.password = password;
        wins = 0;
        losses = 0;
        posX = 400;
        posY = 300;
        socket = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getWins() {
        return wins;
    }

    public void addWin() {
        this.wins++;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void addLoss() {
        this.losses++;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
