package edu.northeastern.ccs.im.service;


import edu.northeastern.ccs.im.customexceptions.*;
import edu.northeastern.ccs.im.service.jpa_service.GroupJPAService;
import edu.northeastern.ccs.im.service.jpa_service.InviteJPAService;
import edu.northeastern.ccs.im.service.jpa_service.Status;
import edu.northeastern.ccs.im.service.jpa_service.UserJPAService;
import edu.northeastern.ccs.im.user_group.Group;
import edu.northeastern.ccs.im.user_group.Invite;
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
    private InviteJPAService inviteJPAService;
    private GroupJPAService groupJPAService;
    private Invite invite;
    private Group groupOne;

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

        inviteJPAService = mock(InviteJPAService.class);
        groupJPAService = mock(GroupJPAService.class);
        invite = new Invite();
        userOne.setUsername("User");
        userTwo.setUsername("Name");
        invite.setSender(userOne);
        invite.setReceiver(userTwo);
        groupOne = new Group();
        groupOne.setGroupCode("Groupie");
        invite.setGroup(groupOne);

    }


    /**
     * A test to ensure create entity works properly.
     */
    @Test
    public void testCreateEntity() throws UserNotPersistedException, UserNotFoundException {
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
    public void testFailToCreateEntity() throws UserNotPersistedException, UserNotFoundException {
        when(userJPAService.createUser(any())).thenReturn(0);
        userService.setJPAService(userJPAService);
        User newUser = userService.addUser(userOne);

        assertNull(newUser);
        verify(userJPAService).createUser(any());
    }

    /**
     * A test to ensure we searched for a user properly.
     */
    @Test
    public void testSearchUser() throws UserNotFoundException {
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
    public void testFollow() throws UserNotFoundException {
        when(userJPAService.search(any())).thenReturn(userTwo);
        userService.setJPAService(userJPAService);
        userService.follow("Heya", userOne);
        assertEquals(userTwo, userOne.getFollowing().get(0));
    }

    /**
     * Tests the update User functionality.
     */
    @Test
    public void testUpdateUser() throws UserNotFoundException {
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
    public void testDeleteUser() throws UserNotFoundException {
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
    public void testLoginUser() throws UserNotFoundException {
        when(userJPAService.loginUser(any())).thenReturn(userOne);
        userService.setJPAService(userJPAService);
        User newUser = userService.loginUser(userOne);
        assertEquals(userOne, newUser);
    }

    /**
     * Tests to ensure follow is not working properly.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testFollowFail() throws UserNotFoundException {
        when(userJPAService.search(any())).thenReturn(null);
        userService.setJPAService(userJPAService);
        userService.follow("Heya", userOne);
    }

    /**
     * Tests the getFollowers method is not working when passed null.
     */
    @Test
    public void testGetFollowersFail() throws UserNotFoundException, ListOfUsersNotFound {
        when(userJPAService.search(any())).thenReturn(null);
        userService.setJPAService(userJPAService);
        userService.getFollowers("Heya");
        assertEquals(0, userOne.getFollowing().size());
    }

    /**
     * Tests to ensure follow is working properly.
     */
    @Test
    public void testGetFollowers() throws UserNotFoundException, ListOfUsersNotFound {
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
    public void testGetFolloweesFail() throws UserNotFoundException, ListOfUsersNotFound {
        when(userJPAService.search(any())).thenReturn(null);
        userService.setJPAService(userJPAService);
        userService.getFollowees("Heya");
        assertEquals(0, userOne.getFollowing().size());
    }

    /**
     * Tests to ensure getFollowee is working properly.
     */
    @Test
    public void testGetFollowees() throws UserNotFoundException, ListOfUsersNotFound {
        List<User> following = new ArrayList<User>();
        following.add(userOne);
        userTwo.setFollowing(following);
        when(userJPAService.search(anyString())).thenReturn(userTwo);
        userService.setJPAService(userJPAService);
        userService.getFollowees("Heya");
        verify(userJPAService).search(anyString());
    }

    /**
     * Tests the unfollow method to ensure it doesn't throw any exceptions.
     * @throws UserNotFoundException exception thrown when a user is not found.
     */
    @Test
    public void testUnfollow() throws UserNotFoundException {
        String username = "username";
        when(userJPAService.search(anyString())).thenReturn(userTwo);
        userService.setJPAService(userJPAService);
        assertEquals(userTwo, userService.unfollow(username, userTwo));
    }

    /**
     * Tests to see when a user is null.
     * @throws UserNotFoundException throws when a user is not found.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testUnfollowFail() throws UserNotFoundException {
        String username = "username";
        when(userJPAService.search(anyString())).thenReturn(null);
        userService.setJPAService(userJPAService);
        userService.unfollow(username, userTwo);
    }

    /**
     * Tests to ensure we can send an invite
     * @throws UserNotFoundException when a user is not found
     * @throws InviteNotAddedException when an invite is not added to the DB.
     * @throws GroupNotFoundException when a group is not found
     * @throws InviteNotFoundException when an invite is not found.
     */
    @Test
    public void testSendInvite() throws UserNotFoundException, InviteNotAddedException, GroupNotFoundException, InviteNotFoundException {
        when(userJPAService.search(anyString())).thenReturn(userOne);
        when(inviteJPAService.createInvite(any())).thenReturn(123);
        when(groupJPAService.searchUsingCode(anyString())).thenReturn(groupOne);
        when(inviteJPAService.getInvite(anyInt())).thenReturn(invite);
        userService.setJPAService(userJPAService);
        userService.setInviteJPAService(inviteJPAService);
        userService.setGroupJPAService(groupJPAService);
        assertEquals(invite, userService.sendInvite(invite));
    }

    /**
     * Tests to ensure the invite can be deleted
     * @throws InviteNotDeletedException if invite was not deleted.
     */
    @Test
    public void testDeleteInvite() throws InviteNotDeletedException {
        when(inviteJPAService.deleteInvite(any())).thenReturn(invite);
        userService.setInviteJPAService(inviteJPAService);
        assertEquals(Status.DELETED, userService.deleteInvite(invite).getStatus());
    }

    /**
     * Tests to ensure the update invite method is working properly.
     * @throws InviteNotFoundException if the invite not found
     * @throws InviteNotUpdatedException if the invite was not updated
     */
    @Test
    public void testUpdateInvite() throws InviteNotFoundException, InviteNotUpdatedException {
        when(inviteJPAService.getInvite(anyInt())).thenReturn(invite);
        userService.setInviteJPAService(inviteJPAService);
        assertEquals(invite, userService.updateInvite(invite));
    }


    @Test
    public void testSearchInviteByGroupCode() throws UserNotFoundException, GroupNotFoundException,
            InviteNotFoundException, IllegalAccessException {
        when(userJPAService.search(anyString())).thenReturn(userOne);
        when(inviteJPAService.searchInviteByGroupCode(anyString(), any())).thenReturn(new ArrayList<>());
        userService.setInviteJPAService(inviteJPAService);
        userService.setJPAService(userJPAService);
        assertEquals(0, userService.searchInviteByGroupCode("code", "user").size());
    }
}