package com.thomasgvd.multirps;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    UserService userService = UserService.getInstance();
    Set<MyUser> users;

    @Before
    public void setUp() {
        users = new HashSet<>();
        users.add(new MyUser("user1", "pass1"));
        users.add(new MyUser("user2", "pass2"));
        users.add(new MyUser("user3", "pass3"));
    }

    @Test
    public void failedConnectionResponse() {
        assertEquals("0|bad|0|0|0", userService.handleFailedConnection("bad"));
    }

    @Test
    public void successfulConnectionResponse() {
        MyUser user = new MyUser("good", "pass");
        assertEquals("0|good|1|400|300", userService.handleSuccessfulConnection(user));
    }

    @Test
    public void getUserThatDoesntExistShouldReturnNull() {
        assertNull(userService.getUser(users, "noone"));
    }

    @Test
    public void getUserThatExistsShouldReturnUser() {
        assertSame(userService.getUser(users, "user1").getClass(), MyUser.class);
    }

    @Test
    public void authenticateNonExistingUserShouldFail() {
        assertFalse(userService.authenticateUser(users, "noone", "pass"));
    }

    @Test
    public void authenticateUserWithWrongPasswordShouldFail() {
        assertFalse(userService.authenticateUser(users, "user1", "wrong_pass"));
    }

    @Test
    public void authenticateUserWithRightPasswordShouldSucceed() {
        assertTrue(userService.authenticateUser(users, "user1", "pass1"));
    }

    @Test
    public void noMovementShouldReturnUserPosition() {
        MyUser user = new MyUser("user1", "pass1");
        assertEquals("1|user1|400|300", userService.handleMovement(user, 0, 0));
    }

    @Test
    public void horizontalMovementShouldReturnNewPosition() {
        MyUser user = new MyUser("user1", "pass1");
        assertEquals("1|user1|405|300", userService.handleMovement(user, 1, 0));
    }

    @Test
    public void verticalMovementShouldReturnNewPosition() {
        MyUser user = new MyUser("user1", "pass1");
        assertEquals("1|user1|400|295", userService.handleMovement(user, 0, -1));
    }
}