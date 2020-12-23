package com.thomasgvd.multirps;

public class App {

    public static void main(String[] args) {
        System.out.println("start server");
        Server server = new Server(5555);
        server.start();
    }
}
