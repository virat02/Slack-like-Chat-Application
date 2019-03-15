package edu.northeastern.ccs.im.controller;

import edu.northeastern.ccs.im.communication.NetworkResponse;
import edu.northeastern.ccs.im.service.UserService;
import edu.northeastern.ccs.im.userGroup.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * A class made to test the UserController.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    private UserController userController;
    private UserService userService;
    private User userOne;

    /**
     * Sets up the controllers variables and mocks the user service.
     */
    @Before
    public void setUp() throws IOException {
        userController = new UserController();
        userService = mock(UserService.class);
        userOne = new User();
        userOne.setPassword("yo123");
        userOne.setUsername("Jerry");
    }

    /**
     * Tests the successful addition of a user to a database.
     */
    @Test
    public void testCreateEntity() {
        when(userService.addUser(any())).thenReturn(userOne);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.addEntity(userOne);
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(userService).addUser(any());
    }

    /**
     * Tests the successful update of a user to a database.
     */
    @Test
    public void testUpdateEntity() {
        when(userService.update(any())).thenReturn(userOne);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.updateEntity(userOne);
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(userService).update(any());
    }

    /**
     * Tests the successful deletion of a d to a database.
     */
    @Test
    public void testDeleteEntity() {
        when(userService.delete(any())).thenReturn(userOne);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.deleteEntity(userOne);
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(userService).delete(any());
    }

    /**
     * Tests the successful search of a user to a database.
     */
    @Test
    public void testSearchEntity() {
        when(userService.search(anyString())).thenReturn(userOne);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.searchEntity("This");
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(userService).search(anyString());
    }

    /**
     * Tests the successful login of a user to a database.
     */
    @Test
    public void testLoginEntity() {
        when(userService.loginUser(any())).thenReturn(userOne);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.loginUser(userOne);
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(userService).loginUser(any());
    }

    /**
     * Tests the failed login of a user to a database.
     */
    @Test
    public void testLoginEntityFail() {
        when(userService.loginUser(any())).thenReturn(null);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.loginUser(userOne);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(userService).loginUser(any());
    }

    /**
     * Tests the successful follow of a user to a database.
     */
    @Test
    public void testFollowEntity() {
        userController.setUserService(userService);
        userController.followUser("Yas", userOne);
    }
}