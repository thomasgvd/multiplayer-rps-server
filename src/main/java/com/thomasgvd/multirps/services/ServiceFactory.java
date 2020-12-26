package com.thomasgvd.multirps.services;

public class ServiceFactory {

    private UserService userService = null;
    private ResponseService responseService = null;

    public ResponseService getResponseService() {
        if (responseService == null) responseService = new ResponseService(getUserService());
        return responseService;
    }

    public UserService getUserService() {
        if (userService == null) userService = new UserService();
        return userService;
    }
}
