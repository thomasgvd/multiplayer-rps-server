package com.thomasgvd.multirps.services;

import com.thomasgvd.multirps.models.MyUser;
import com.thomasgvd.multirps.models.Response;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResponseServiceTest {

    Set<MyUser> users;
    ResponseService responseService;
    UserService userService;
    Response response;

    @Before
    public void setUp() {
        users = new HashSet<>();
        users.add(new MyUser("user1", "pass1"));
        users.add(new MyUser("user2", "pass2"));
        users.add(new MyUser("user3", "pass3"));
        response = new Response();
        userService = mock(UserService.class);
        responseService = new ResponseService(userService);
    }

    @Test
    public void emptyPacketShouldBeDiscarded() throws IOException {
        MyUser user = mock(MyUser.class);
        String[] input = new String[]{""};

        response = responseService.manageInput(input, response, users, user);
        assertFalse(response.isSuccessful());
    }

    @Test
    public void goodPacketShouldBeSuccessful() throws IOException {
        MyUser user = new MyUser("user1", "pass1");
        String[] input = new String[]{"0", "user1", "pass1"};
        when(userService.authenticateUser(users, "user1", "pass1")).thenReturn(true);

        response = responseService.manageInput(input, response, users, user);
        assertTrue(response.isSuccessful());
    }

    @Test
    public void connectionPacketShouldRespondToOne() throws IOException {
        MyUser user = mock(MyUser.class);
        String[] input = new String[]{"0", "user", "pass"};

        response = responseService.manageInput(input, response, users, user);
        assertTrue(response.isRespondToOne());
    }

    @Test
    public void movementPacketShouldRespondToAll() throws IOException {
        MyUser user = mock(MyUser.class);
        String[] input = new String[]{"1", "user", "0", "0"};

        response = responseService.manageInput(input, response, users, user);
        assertTrue(response.isRespondToAll());
    }
}