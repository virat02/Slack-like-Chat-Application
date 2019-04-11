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
        userOne.setUsername("Jerry1");
        userOne.setPassword("Banjo1");
        userOne.setId(123);

        userTwo.setUsername("Danny1");
        userTwo.setPassword("Dragons1");
        userTwo.setId(2);

        userService = new UserService();
        userJPAService = mock(UserJPAService.class);

        inviteJPAService = mock(InviteJPAService.class);
        groupJPAService = mock(GroupJPAService.class);
        invite = new Invite();
        invite.setSender(userOne);
        invite.setReceiver(userTwo);
        groupOne = new Group();
        groupOne.setGroupCode("Groupie");
        invite.setGroup(groupOne);

    }


    /**
     * A test to ensure create entity works properly.
     * @throws UserNotPersistedException if the user wasn't persisted in the DB.
     * @throws UserNotFoundException if the user wasn't found in the DB.
     * @throws UsernameInvalidException if the username is invalid.
     * @throws PasswordInvalidException if the password is invalid.
     */
    @Test
    public void testCreateEntity() throws UserNotPersistedException, UserNotFoundException, UsernameInvalidException, PasswordInvalidException {
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
     * @throws UserNotPersistedException if the user wasn't persisted in the DB.
     * @throws UserNotFoundException if the user wasn't found in the DB.
     * @throws UsernameInvalidException if the username is invalid.
     * @throws PasswordInvalidException if the password is invalid.
     */
    @Test
    public void testFailToCreateEntity() throws UserNotPersistedException, UserNotFoundException, UsernameInvalidException, PasswordInvalidException {
        when(userJPAService.createUser(any())).thenReturn(0);
        userService.setJPAService(userJPAService);
        User newUser = userService.addUser(userOne);

        assertNull(newUser);
        verify(userJPAService).createUser(any());
    }

    /**
     * A test to ensure we searched for a user properly.
     * @throws UserNotFoundException if the user wasn't found in the DB.
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
     * @throws UserNotFoundException if the user wasn't found in the DB.
     */
    @Test
    public void testFollow() throws UserNotFoundException {
        when(userJPAService.search(any())).thenReturn(userTwo);
        userService.setJPAService(userJPAService);
        userService.follow("Heya", userOne);
    }

    /**
     * Tests the update User functionality.
     * @throws UserNotFoundException if the user wasn't found in the DB.
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
     * @throws UserNotFoundException if the user wasn't found in the DB.
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
     * Tests the login functionality/
     * @throws UserNotFoundException if the user wasn't found in the DB.
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
     * @throws UserNotFoundException if the user wasn't found in the DB.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testFollowFail() throws UserNotFoundException {
        when(userJPAService.search(any())).thenReturn(null);
        userService.setJPAService(userJPAService);
        userService.follow("Heya", userOne);
    }

    /**
     * Tests the getFollowers method is not working when passed null.
     * @throws UserNotFoundException if the user wasn't found in the DB.
     * @throws ListOfUsersNotFound if the list of users isn't found in the DB.
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
     * @throws UserNotFoundException if the user wasn't found in the DB.
     * @throws ListOfUsersNotFound if the list of users isn't found in the DB.
     */
    @Test
    public void testGetFollowers() throws UserNotFoundException, ListOfUsersNotFound {
        List<User> following = new ArrayList<>();
        following.add(userOne);
        userTwo.setFollowing(following);
        when(userJPAService.search(anyString())).thenReturn(userTwo);
        userService.setJPAService(userJPAService);
        userService.getFollowers("Heya");
        verify(userJPAService).search(anyString());
    }

    /**
     * Tests the getFollowees is not working when passed null.
     * @throws UserNotFoundException if the user wasn't found in the DB.
     * @throws ListOfUsersNotFound if the list of users isn't found in the DB.
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
     * @throws UserNotFoundException if the user wasn't found in the DB.
     * @throws ListOfUsersNotFound if the list of users isn't found in the DB.
     */
    @Test
    public void testGetFollowees() throws UserNotFoundException, ListOfUsersNotFound {
        List<User> following = new ArrayList<>();
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
     * @throws UnfollowNotFollowingUserException if the unfollow method fails.
     */
    @Test(expected = UnfollowNotFollowingUserException.class)
    public void testUnfollow() throws UserNotFoundException,UnfollowNotFollowingUserException {
        String username = "username";
        when(userJPAService.search(anyString())).thenReturn(userTwo);
        userService.setJPAService(userJPAService);
        assertEquals(userTwo, userService.unfollow(username, userTwo));
    }

    /**
     * Tests to see when a user is null.
     * @throws UserNotFoundException throws when a user is not found.
     * @throws UnfollowNotFollowingUserException if the unfollow method fails.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testUnfollowFail() throws UserNotFoundException,UnfollowNotFollowingUserException {
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

    /**
     * Tests to ensure we can find a group by it's code.
     * @throws UserNotFoundException user not found
     * @throws GroupNotFoundException when a group isn't found in DB
     * @throws InviteNotFoundException when an invite isn't found in the DB
     */
    @Test
    public void testSearchInviteByGroupCode() throws UserNotFoundException, GroupNotFoundException,
            InviteNotFoundException {
        when(userJPAService.search(anyString())).thenReturn(userOne);
        when(inviteJPAService.searchInviteByGroupCode(anyString(), any())).thenReturn(new ArrayList<>());
        userService.setInviteJPAService(inviteJPAService);
        userService.setJPAService(userJPAService);
        assertEquals(0, userService.searchInviteByGroupCode("code", "user").size());
    }

    /**
     * Tests to ensure we can't have a username without an uppercase letter.
     * @throws UsernameInvalidException username doesn't contain a lowercase letter.
     * @throws PasswordInvalidException password is too small.
     * @throws UserNotPersistedException user is not persisted.
     * @throws UserNotFoundException user is not found in the DB.
     */
    @Test (expected = UsernameInvalidException.class)
    public void testUsernameNoUpper() throws UsernameInvalidException, PasswordInvalidException,
            UserNotPersistedException, UserNotFoundException {
        String username = "user1";
        String password = "Password1";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userService.addUser(user);
    }

    /**
     * Tests to ensure we can't have a username that is too small.
     * @throws UsernameInvalidException username doesn't contain a lowercase letter.
     * @throws PasswordInvalidException password is too small.
     * @throws UserNotPersistedException user is not persisted.
     * @throws UserNotFoundException user is not found in the DB.
     */
    @Test (expected = UsernameInvalidException.class)
    public void testUsernameTooSmall() throws UsernameInvalidException, PasswordInvalidException,
            UserNotPersistedException, UserNotFoundException {
        String username = "Ra2";
        String password = "Password1";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userService.addUser(user);
    }

    /**
     * Tests to ensure we can't have a username without an lowercase letter.
     * @throws UsernameInvalidException username doesn't contain a lowercase letter.
     * @throws PasswordInvalidException password is too small.
     * @throws UserNotPersistedException user is not persisted.
     * @throws UserNotFoundException user is not found in the DB.
     */
    @Test (expected = UsernameInvalidException.class)
    public void testUsernameNoLower() throws UsernameInvalidException, PasswordInvalidException,
            UserNotPersistedException, UserNotFoundException {
        String username = "USER1";
        String password = "Password1";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userService.addUser(user);
    }

    /**
     * Tests to ensure we can't have a username without a number.
     * @throws UsernameInvalidException username doesn't contain a lowercase letter.
     * @throws PasswordInvalidException password is too small.
     * @throws UserNotPersistedException user is not persisted.
     * @throws UserNotFoundException user is not found in the DB.
     */
    @Test (expected = UsernameInvalidException.class)
    public void testUsernameNoNumber() throws UsernameInvalidException, PasswordInvalidException,
            UserNotPersistedException, UserNotFoundException{
        String username = "Userone";
        String password = "Password1";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userService.addUser(user);
    }

    /**
     * Tests to ensure we can't have a password that is too small.
     * @throws UsernameInvalidException username doesn't contain a lowercase letter.
     * @throws PasswordInvalidException password is too small.
     * @throws UserNotPersistedException user is not persisted.
     * @throws UserNotFoundException user is not found in the DB.
     */
    @Test (expected = PasswordInvalidException.class)
    public void testPasswordTooSmall() throws UsernameInvalidException, PasswordInvalidException,
            UserNotPersistedException, UserNotFoundException {
        String username = "User1";
        String password = "Pa1";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userService.addUser(user);
    }

    /**
     * Tests to ensure we can't have a password without an uppercase letter.
     * @throws UsernameInvalidException username doesn't contain a lowercase letter.
     * @throws PasswordInvalidException password is too small.
     * @throws UserNotPersistedException user is not persisted.
     * @throws UserNotFoundException user is not found in the DB.
     */
    @Test (expected = PasswordInvalidException.class)
    public void testPasswordNoUpper() throws UsernameInvalidException, PasswordInvalidException,
            UserNotPersistedException, UserNotFoundException {
        String username = "User1";
        String password = "password1";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userService.addUser(user);
    }

    /**
     * Tests to ensure we can't have a password without an lowercase letter.
     * @throws UsernameInvalidException username doesn't contain a lowercase letter.
     * @throws PasswordInvalidException password is too small.
     * @throws UserNotPersistedException user is not persisted.
     * @throws UserNotFoundException user is not found in the DB.
     */
    @Test (expected = PasswordInvalidException.class)
    public void testPasswordNoLower() throws UsernameInvalidException, PasswordInvalidException,
            UserNotPersistedException, UserNotFoundException {
        String username = "User1";
        String password = "PASSWORD1";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userService.addUser(user);
    }

    /**
     * Tests to ensure we can't have a password without a number.
     * @throws UsernameInvalidException username doesn't contain a lowercase letter.
     * @throws PasswordInvalidException password is too small.
     * @throws UserNotPersistedException user is not persisted.
     * @throws UserNotFoundException user is not found in the DB.
     */
    @Test (expected = PasswordInvalidException.class)
    public void testPasswordNoNumber() throws UsernameInvalidException, PasswordInvalidException,
            UserNotPersistedException, UserNotFoundException {
        String username = "User1";
        String password = "Password";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userService.addUser(user);
    }

    /**
     * Tests to ensure we can't have a password that is too long.
     * @throws UsernameInvalidException username doesn't contain a lowercase letter.
     * @throws PasswordInvalidException password is too small.
     * @throws UserNotPersistedException user is not persisted.
     * @throws UserNotFoundException user is not found in the DB.
     */
    @Test (expected = PasswordInvalidException.class)
    public void testPasswordTooLong() throws UsernameInvalidException, PasswordInvalidException,
            UserNotPersistedException, UserNotFoundException {
        String username = "User1";
        String password = "ThisPasswordIsTooLong11243";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userService.addUser(user);
    }

    /**
     * Tests to ensure we can't have a username that is too long.
     * @throws UsernameInvalidException username doesn't contain a lowercase letter.
     * @throws PasswordInvalidException password is too small.
     * @throws UserNotPersistedException user is not persisted.
     * @throws UserNotFoundException user is not found in the DB.
     */
    @Test (expected = UsernameInvalidException.class)
    public void testUsernameIsTooLong() throws UsernameInvalidException, PasswordInvalidException,
            UserNotPersistedException, UserNotFoundException {
        String username = "ThisUsernameIsDefinitelyTooLong1123";
        String password = "Password1";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userService.addUser(user);
    }

    /**
     * Tests to ensure the password abc1 is valid.
     * @throws UsernameInvalidException username doesn't contain a lowercase letter.
     * @throws PasswordInvalidException password is too small.
     * @throws UserNotPersistedException user is not persisted.
     * @throws UserNotFoundException user is not found in the DB.
     */
    @Test (expected = PasswordInvalidException.class)
    public void testABC1Fail() throws UsernameInvalidException, PasswordInvalidException,
            UserNotPersistedException, UserNotFoundException{
        String username = "Jalannin1";
        String password = "abc1";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userService.addUser(user);
    }

    /**
     * Tests to ensure the UserGroup Event method works properly.
     * @throws UserNotFoundException when a user is not found in the DB.
     * @throws GroupNotFoundException when a group is not found in the DB.
     */
    @Test
    public void testUserGroupEvent() throws UserNotFoundException, GroupNotFoundException {
        when(userJPAService.search(anyString())).thenReturn(userTwo);
        when(groupJPAService.searchUsingCode(anyString())).thenReturn(groupOne);
        userService.setGroupJPAService(groupJPAService);
        userService.setJPAService(userJPAService);
        userService.userGroupEvent(userTwo.getUsername(), groupOne.getGroupCode());
    }

    /**
     * Tests to ensure the UserGroup Event method throws a User Not found exception.
     * @throws UserNotFoundException when a user is not found in the DB.
     * @throws GroupNotFoundException when a group is not found in the DB.
     */
    @Test (expected = UnsupportedOperationException.class)
    public void testUserGroupEventUserNotFound() throws UserNotFoundException, GroupNotFoundException {
        when(userJPAService.search(anyString())).thenThrow(UserNotFoundException.class);
        when(groupJPAService.searchUsingCode(anyString())).thenReturn(groupOne);
        userService.setGroupJPAService(groupJPAService);
        userService.setJPAService(userJPAService);
        userService.userGroupEvent(userTwo.getUsername(), groupOne.getGroupCode());
    }

    /**
     * Tests to ensure the UserGroup Event method throws a Group Not Found Exception.
     * @throws UserNotFoundException when a user is not found in the DB.
     * @throws GroupNotFoundException when a group is not found in the DB.
     */
    @Test (expected = UnsupportedOperationException.class)
    public void testUserGroupEventGroupNotFound() throws UserNotFoundException, GroupNotFoundException {
        when(userJPAService.search(anyString())).thenReturn(userTwo);
        when(groupJPAService.searchUsingCode(anyString())).thenThrow(GroupNotFoundException.class);
        userService.setGroupJPAService(groupJPAService);
        userService.setJPAService(userJPAService);
        userService.userGroupEvent(userTwo.getUsername(), groupOne.getGroupCode());
    }
}