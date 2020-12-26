package com.thomasgvd.multirps;

import com.thomasgvd.multirps.networking.Server;
import com.thomasgvd.multirps.services.ServiceFactory;

public class App {

    public static void main(String[] args) {
        System.out.println("start server");
        ServiceFactory serviceFactory = new ServiceFactory();
        Server server = new Server(5555, serviceFactory.getUserService(), serviceFactory.getResponseService());
        server.start();
    }
}
