package edu.northeastern.ccs.im.service;


import edu.northeastern.ccs.im.service.jpa_service.UserJPAService;
import edu.northeastern.ccs.im.user_group.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Class to test the User service methods
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    private UserJPAService userJPAService;
    private User userOne;
    private User userTwo;
    private UserService userService;

    /**
     * Sets up the necessary variables to test the UserService.
     */
    @Before
    public void setUp() {
        userOne = new User();
        userTwo = new User();
        userOne.setUsername("Jerry");
        userOne.setPassword("Banjo");
        userOne.setId(123);

        userTwo.setUsername("Danny");
        userTwo.setPassword("Dragons");
        userTwo.setId(2);

        userService = new UserService();
        userJPAService = mock(UserJPAService.class);

    }


    /**
     * A test to ensure create entity works properly.
     */
    @Test
    public void testCreateEntity() {
        when(userJPAService.createUser(any())).thenReturn(1);
        when(userJPAService.getUser(anyInt())).thenReturn(userOne);
        userService.setJPAService(userJPAService);
        User newUser = userService.addUser(userOne);

        assert newUser != null;
        assertEquals(userOne,newUser);
        verify(userJPAService).createUser(any());
    }

    /**
     * A test to ensure create entity doesn't work when the int returns 0.
     */
    @Test
    public void testFailToCreateEntity() {
        when(userJPAService.createUser(any())).thenReturn(0);
        when(userJPAService.getUser(anyInt())).thenReturn(userOne);
        userService.setJPAService(userJPAService);
        User newUser = userService.addUser(userOne);

        assertNull(newUser);
        verify(userJPAService).createUser(any());
    }

    /**
     * A test to ensure we searched for a user properly.
     */
    @Test
    public void testSearchUser() {
        when(userJPAService.search(any())).thenReturn(userOne);
        userService.setJPAService(userJPAService);
        User newUser = userService.search("Heya");
        assertEquals(userOne, newUser);
        verify(userJPAService).search(any());
    }

    /**
     * Tests to ensure follow is working properly.
     */
    @Test
    public void testFollow() {
        when(userJPAService.search(any())).thenReturn(userTwo);
        userService.setJPAService(userJPAService);
        userService.follow("Heya", userOne);
        assertEquals(userTwo, userOne.getFollowing().get(0));
    }

    /**
     * Tests the update User functionality.
     */
    @Test
    public void testUpdateUser() {
        when(userJPAService.getUser(anyInt())).thenReturn(userOne);
        userService.setJPAService(userJPAService);
        User newUser = userService.update(userOne);
        verify(userJPAService).getUser(anyInt());
        assertEquals(userOne, newUser);

    }

    /**
     * Tests the delete User functionality.
     */
    @Test
    public void testDeleteUser() {
        when(userJPAService.getUser(anyInt())).thenReturn(userOne);
        userService.setJPAService(userJPAService);
        User newUser = userService.delete(userOne);
        assertEquals(userOne, newUser);
        verify(userJPAService).getUser(anyInt());
    }

    /**
     * Tests the login functionality.
     */
    @Test
    public void testLoginUser() {
        when(userJPAService.loginUser(any())).thenReturn(userOne);
        userService.setJPAService(userJPAService);
        User newUser = userService.loginUser(userOne);
        assertEquals(userOne, newUser);
    }

    /**
     * Tests to ensure follow is not working properly.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testFollowFail() {
        when(userJPAService.search(any())).thenReturn(null);
        userService.setJPAService(userJPAService);
        userService.follow("Heya", userOne);
    }

    /**
     * Tests the getFollowers method is not working when passed null.
     */
    @Test
    public void testGetFollowersFail() {
        when(userJPAService.search(any())).thenReturn(null);
        userService.setJPAService(userJPAService);
        userService.getFollowers("Heya");
        assertEquals(0, userOne.getFollowing().size());
    }

    /**
     * Tests to ensure follow is working properly.
     */
    @Test
    public void testGetFollowers() {
        List<User> following = new ArrayList<User>();
        following.add(userOne);
        userTwo.setFollowing(following);
        when(userJPAService.search(anyString())).thenReturn(userTwo);
        userService.setJPAService(userJPAService);
        userService.getFollowers("Heya");
        verify(userJPAService).search(anyString());
    }

    /**
     * Tests the getFollowees is not working when passed null.
     */
    @Test
    public void testGetFolloweesFail() {
        when(userJPAService.search(any())).thenReturn(null);
        userService.setJPAService(userJPAService);
        userService.getFollowees("Heya");
        assertEquals(0, userOne.getFollowing().size());
    }

    /**
     * Tests to ensure getFollowee is working properly.
     */
    @Test
    public void testGetFollowees() {
        List<User> following = new ArrayList<User>();
        following.add(userOne);
        userTwo.setFollowing(following);
        when(userJPAService.search(anyString())).thenReturn(userTwo);
        userService.setJPAService(userJPAService);
        userService.getFollowees("Heya");
        verify(userJPAService).search(anyString());
    }

}