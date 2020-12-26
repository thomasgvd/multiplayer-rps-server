package com.thomasgvd.multirps;

import com.thomasgvd.multirps.networking.Server;

public class App {

    public static void main(String[] args) {
        System.out.println("start server");
        Server server = new Server(5555);
        server.start();
    }
}
