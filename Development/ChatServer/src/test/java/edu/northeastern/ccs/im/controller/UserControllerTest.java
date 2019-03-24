package edu.northeastern.ccs.im.controller;
import edu.northeastern.ccs.im.communication.NetworkResponse;
import edu.northeastern.ccs.im.service.UserService;
import edu.northeastern.ccs.im.user_group.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * A class made to test the UserController.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
//    private UserController userController;
//    private UserService userService;
//    private User userOne;
//
//    /**
//     * Sets up the controllers variables and mocks the user service.
//     */
//    @Before
//    public void setUp() {
//        userController = new UserController();
//        userService = mock(UserService.class);
//        userOne = new User();
//        userOne.setPassword("yo123");
//        userOne.setUsername("Jerry");
//    }
//
//    /**
//     * Tests the successful addition of a user to a database.
//     */
//    @Test
//    public void testCreateEntity() {
//        when(userService.addUser(any())).thenReturn(userOne);
//        userController.setUserService(userService);
//        NetworkResponse networkResponse = userController.addEntity(userOne);
//        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
//        verify(userService).addUser(any());
//    }
//
//    /**
//     * Tests the successful update of a user to a database.
//     */
//    @Test
//    public void testUpdateEntity() {
//        when(userService.update(any())).thenReturn(userOne);
//        userController.setUserService(userService);
//        NetworkResponse networkResponse = userController.updateEntity(userOne);
//        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
//        verify(userService).update(any());
//    }
//    /**
//     * Tests the successful deletion of a d to a database.
//     */
//    @Test
//    public void testDeleteEntity() {
//        when(userService.delete(any())).thenReturn(userOne);
//        userController.setUserService(userService);
//        NetworkResponse networkResponse = userController.deleteEntity(userOne);
//        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
//        verify(userService).delete(any());
//    }
//
//    /**
//     * Tests the successful search of a user to a database.
//     */
//    @Test
//    public void testSearchEntity() {
//        when(userService.search(anyString())).thenReturn(userOne);
//        userController.setUserService(userService);
//        NetworkResponse networkResponse = userController.searchEntity("This");
//        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
//        verify(userService).search(anyString());
//    }
//
//    /**
//     * Tests the successful login of a user to a database.
//     */
//    @Test
//    public void testLoginEntity() {
//        when(userService.loginUser(any())).thenReturn(userOne);
//        userController.setUserService(userService);
//        NetworkResponse networkResponse = userController.loginUser(userOne);
//        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
//        verify(userService).loginUser(any());
//    }
//
//    /**
//     * Tests the failed login of a user to a database.
//     */
//    @Test
//    public void testLoginEntityFail() {
//        when(userService.loginUser(any())).thenReturn(null);
//        userController.setUserService(userService);
//        NetworkResponse networkResponse = userController.loginUser(userOne);
//        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
//        verify(userService).loginUser(any());
//    }
//
//    /**
//     * Tests the successful follow of a user to a database.
//     */
//    @Test
//    public void testFollowEntity() {
//        when(userService.follow(anyString(), any())).thenReturn(userOne);
//        userController.setUserService(userService);
//        NetworkResponse networkResponse = userController.followUser("Yas", userOne);
//        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
//        verify(userService).follow(anyString(), any());
//    }
//
//    /**
//     * Tests the unsuccessful follow of a user to a database.
//     */
//    @Test
//    public void testFollowEntityFail() {
//        userController.setUserService(userService);
//        userController.followUser("Yas", userOne);
//    }
//
//    /**
//     * Tests the successful getting of followers.
//     */
//    @Test
//    public void testViewFollowers() {
//        List<User> users = new ArrayList<>();
//        when(userService.getFollowers(any())).thenReturn(users);
//        userController.setUserService(userService);
//        NetworkResponse networkResponse = userController.viewFollowers("Us");
//        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
//        verify(userService).getFollowers(any());
//    }
//
//    /**
//     * Tests the successful getting of followees.
//     */
//    @Test
//    public void testViewFollowees() {
//        List<User> users = new ArrayList<>();
//        when(userService.getFollowees(any())).thenReturn(users);
//        userController.setUserService(userService);
//        NetworkResponse networkResponse = userController.viewFollowees("Us");
//        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
//        verify(userService).getFollowees(any());
//    }
}