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
     */
    @Test
    public void testCreateEntity() throws UserNotPersistedException, UserNotFoundException, PasswordDoesNotContainUppercaseException, PasswordDoesNotContainLowercaseException, UsernameDoesNotContainLowercaseException, UsernameDoesNotContainNumberException, PasswordDoesNotContainNumberException, PasswordTooSmallException, UsernameTooSmallException, UsernameDoesNotContainUppercaseException, UsernameTooLongException, PasswordTooLargeException {
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
    public void testFailToCreateEntity() throws UserNotPersistedException, UserNotFoundException, PasswordDoesNotContainUppercaseException, PasswordDoesNotContainLowercaseException, UsernameDoesNotContainLowercaseException, UsernameDoesNotContainNumberException, PasswordDoesNotContainNumberException, PasswordTooSmallException, UsernameTooSmallException, UsernameDoesNotContainUppercaseException, UsernameTooLongException, PasswordTooLargeException {
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

    /**
     * Tests to ensure we can find a group by it's code.
     * @throws UserNotFoundException user not found
     * @throws GroupNotFoundException when a group isn't found in DB
     * @throws InviteNotFoundException when an invite isn't found in the DB
     * @throws IllegalAccessException when there is illegal access in the DB
     */
    @Test
    public void testSearchInviteByGroupCode() throws UserNotFoundException, GroupNotFoundException,
            InviteNotFoundException, IllegalAccessException {
        when(userJPAService.search(anyString())).thenReturn(userOne);
        when(inviteJPAService.searchInviteByGroupCode(anyString(), any())).thenReturn(new ArrayList<>());
        userService.setInviteJPAService(inviteJPAService);
        userService.setJPAService(userJPAService);
        assertEquals(0, userService.searchInviteByGroupCode("code", "user").size());
    }

    /**
     * Tests to ensure we can't have a username without an uppercase letter.
     * @throws UsernameDoesNotContainUppercaseException username doesn't contain an uppercase letter.
     * @throws UsernameDoesNotContainNumberException username doesn't contain a number.
     * @throws UsernameDoesNotContainLowercaseException username doesn't contain a lowercase letter.
     * @throws PasswordDoesNotContainUppercaseException password does not contain an uppercase letter.
     * @throws PasswordTooSmallException password is too small.
     * @throws UserNotPersistedException user is not persisted.
     * @throws UserNotFoundException user is not found in the DB.
     * @throws PasswordDoesNotContainLowercaseException password doesn't contain a lower case letter.
     * @throws UsernameTooSmallException username is too small.
     * @throws PasswordDoesNotContainNumberException password does not contain a number.
     */
    @Test (expected = UsernameDoesNotContainUppercaseException.class)
    public void testUsernameNoUpper() throws UsernameDoesNotContainUppercaseException, UsernameDoesNotContainNumberException, UsernameDoesNotContainLowercaseException, PasswordDoesNotContainUppercaseException, PasswordTooSmallException, UserNotPersistedException, UserNotFoundException, PasswordDoesNotContainLowercaseException, UsernameTooSmallException, PasswordDoesNotContainNumberException, UsernameTooLongException, PasswordTooLargeException {
        String username = "user1";
        String password = "Password1";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userService.addUser(user);
    }

    /**
     * Tests to ensure we can't have a username that is too small.
     * @throws UsernameDoesNotContainUppercaseException username doesn't contain an uppercase letter.
     * @throws UsernameDoesNotContainNumberException username doesn't contain a number.
     * @throws UsernameDoesNotContainLowercaseException username doesn't contain a lowercase letter.
     * @throws PasswordDoesNotContainUppercaseException password does not contain an uppercase letter.
     * @throws PasswordTooSmallException password is too small.
     * @throws UserNotPersistedException user is not persisted.
     * @throws UserNotFoundException user is not found in the DB.
     * @throws PasswordDoesNotContainLowercaseException password doesn't contain a lower case letter.
     * @throws UsernameTooSmallException username is too small.
     * @throws PasswordDoesNotContainNumberException password does not contain a number.
     */
    @Test (expected = UsernameTooSmallException.class)
    public void testUsernameTooSmall() throws UsernameDoesNotContainUppercaseException, UsernameDoesNotContainNumberException, UsernameDoesNotContainLowercaseException, PasswordDoesNotContainUppercaseException, PasswordTooSmallException, UserNotPersistedException, UserNotFoundException, PasswordDoesNotContainLowercaseException, UsernameTooSmallException, PasswordDoesNotContainNumberException, UsernameTooLongException, PasswordTooLargeException {
        String username = "Ra2";
        String password = "Password1";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userService.addUser(user);
    }

    /**
     * Tests to ensure we can't have a username without an lowercase letter.
     * @throws UsernameDoesNotContainUppercaseException username doesn't contain an uppercase letter.
     * @throws UsernameDoesNotContainNumberException username doesn't contain a number.
     * @throws UsernameDoesNotContainLowercaseException username doesn't contain a lowercase letter.
     * @throws PasswordDoesNotContainUppercaseException password does not contain an uppercase letter.
     * @throws PasswordTooSmallException password is too small.
     * @throws UserNotPersistedException user is not persisted.
     * @throws UserNotFoundException user is not found in the DB.
     * @throws PasswordDoesNotContainLowercaseException password doesn't contain a lower case letter.
     * @throws UsernameTooSmallException username is too small.
     * @throws PasswordDoesNotContainNumberException password does not contain a number.
     */
    @Test (expected = UsernameDoesNotContainLowercaseException.class)
    public void testUsernameNoLower() throws UsernameDoesNotContainUppercaseException, UsernameDoesNotContainNumberException, UsernameDoesNotContainLowercaseException, PasswordDoesNotContainUppercaseException, PasswordTooSmallException, UserNotPersistedException, UserNotFoundException, PasswordDoesNotContainLowercaseException, UsernameTooSmallException, PasswordDoesNotContainNumberException, UsernameTooLongException, PasswordTooLargeException {
        String username = "USER1";
        String password = "Password1";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userService.addUser(user);
    }

    /**
     * Tests to ensure we can't have a username without a number.
     * @throws UsernameDoesNotContainUppercaseException username doesn't contain an uppercase letter.
     * @throws UsernameDoesNotContainNumberException username doesn't contain a number.
     * @throws UsernameDoesNotContainLowercaseException username doesn't contain a lowercase letter.
     * @throws PasswordDoesNotContainUppercaseException password does not contain an uppercase letter.
     * @throws PasswordTooSmallException password is too small.
     * @throws UserNotPersistedException user is not persisted.
     * @throws UserNotFoundException user is not found in the DB.
     * @throws PasswordDoesNotContainLowercaseException password doesn't contain a lower case letter.
     * @throws UsernameTooSmallException username is too small.
     * @throws PasswordDoesNotContainNumberException password does not contain a number.
     */
    @Test (expected = UsernameDoesNotContainNumberException.class)
    public void testUsernameNoNumber() throws UsernameDoesNotContainUppercaseException, UsernameDoesNotContainNumberException, UsernameDoesNotContainLowercaseException, PasswordDoesNotContainUppercaseException, PasswordTooSmallException, UserNotPersistedException, UserNotFoundException, PasswordDoesNotContainLowercaseException, UsernameTooSmallException, PasswordDoesNotContainNumberException, UsernameTooLongException, PasswordTooLargeException {
        String username = "Userone";
        String password = "Password1";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userService.addUser(user);
    }

    /**
     * Tests to ensure we can't have a password that is too small.
     * @throws UsernameDoesNotContainUppercaseException username doesn't contain an uppercase letter.
     * @throws UsernameDoesNotContainNumberException username doesn't contain a number.
     * @throws UsernameDoesNotContainLowercaseException username doesn't contain a lowercase letter.
     * @throws PasswordDoesNotContainUppercaseException password does not contain an uppercase letter.
     * @throws PasswordTooSmallException password is too small.
     * @throws UserNotPersistedException user is not persisted.
     * @throws UserNotFoundException user is not found in the DB.
     * @throws PasswordDoesNotContainLowercaseException password doesn't contain a lower case letter.
     * @throws UsernameTooSmallException username is too small.
     * @throws PasswordDoesNotContainNumberException password does not contain a number.
     */
    @Test (expected = PasswordTooSmallException.class)
    public void testPasswordTooSmall() throws UsernameDoesNotContainUppercaseException, UsernameDoesNotContainNumberException, UsernameDoesNotContainLowercaseException, PasswordDoesNotContainUppercaseException, PasswordTooSmallException, UserNotPersistedException, UserNotFoundException, PasswordDoesNotContainLowercaseException, UsernameTooSmallException, PasswordDoesNotContainNumberException, UsernameTooLongException, PasswordTooLargeException {
        String username = "User1";
        String password = "Pa1";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userService.addUser(user);
    }

    /**
     * Tests to ensure we can't have a password without an uppercase letter.
     * @throws UsernameDoesNotContainUppercaseException username doesn't contain an uppercase letter.
     * @throws UsernameDoesNotContainNumberException username doesn't contain a number.
     * @throws UsernameDoesNotContainLowercaseException username doesn't contain a lowercase letter.
     * @throws PasswordDoesNotContainUppercaseException password does not contain an uppercase letter.
     * @throws PasswordTooSmallException password is too small.
     * @throws UserNotPersistedException user is not persisted.
     * @throws UserNotFoundException user is not found in the DB.
     * @throws PasswordDoesNotContainLowercaseException password doesn't contain a lower case letter.
     * @throws UsernameTooSmallException username is too small.
     * @throws PasswordDoesNotContainNumberException password does not contain a number.
     */
    @Test (expected = PasswordDoesNotContainUppercaseException.class)
    public void testPasswordNoUpper() throws UsernameDoesNotContainUppercaseException, UsernameDoesNotContainNumberException, UsernameDoesNotContainLowercaseException, PasswordDoesNotContainUppercaseException, PasswordTooSmallException, UserNotPersistedException, UserNotFoundException, PasswordDoesNotContainLowercaseException, UsernameTooSmallException, PasswordDoesNotContainNumberException, UsernameTooLongException, PasswordTooLargeException {
        String username = "User1";
        String password = "password1";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userService.addUser(user);
    }

    /**
     * Tests to ensure we can't have a password without an lowercase letter.
     * @throws UsernameDoesNotContainUppercaseException username doesn't contain an uppercase letter.
     * @throws UsernameDoesNotContainNumberException username doesn't contain a number.
     * @throws UsernameDoesNotContainLowercaseException username doesn't contain a lowercase letter.
     * @throws PasswordDoesNotContainUppercaseException password does not contain an uppercase letter.
     * @throws PasswordTooSmallException password is too small.
     * @throws UserNotPersistedException user is not persisted.
     * @throws UserNotFoundException user is not found in the DB.
     * @throws PasswordDoesNotContainLowercaseException password doesn't contain a lower case letter.
     * @throws UsernameTooSmallException username is too small.
     * @throws PasswordDoesNotContainNumberException password does not contain a number.
     */
    @Test (expected = PasswordDoesNotContainLowercaseException.class)
    public void testPasswordNoLower() throws UsernameDoesNotContainUppercaseException, UsernameDoesNotContainNumberException, UsernameDoesNotContainLowercaseException, PasswordDoesNotContainUppercaseException, PasswordTooSmallException, UserNotPersistedException, UserNotFoundException, PasswordDoesNotContainLowercaseException, UsernameTooSmallException, PasswordDoesNotContainNumberException, UsernameTooLongException, PasswordTooLargeException {
        String username = "User1";
        String password = "PASSWORD1";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userService.addUser(user);
    }

    /**
     * Tests to ensure we can't have a password without a number.
     * @throws UsernameDoesNotContainUppercaseException username doesn't contain an uppercase letter.
     * @throws UsernameDoesNotContainNumberException username doesn't contain a number.
     * @throws UsernameDoesNotContainLowercaseException username doesn't contain a lowercase letter.
     * @throws PasswordDoesNotContainUppercaseException password does not contain an uppercase letter.
     * @throws PasswordTooSmallException password is too small.
     * @throws UserNotPersistedException user is not persisted.
     * @throws UserNotFoundException user is not found in the DB.
     * @throws PasswordDoesNotContainLowercaseException password doesn't contain a lower case letter.
     * @throws UsernameTooSmallException username is too small.
     * @throws PasswordDoesNotContainNumberException password does not contain a number.
     */
    @Test (expected = PasswordDoesNotContainNumberException.class)
    public void testPasswordNoNumber() throws UsernameDoesNotContainUppercaseException, UsernameDoesNotContainNumberException, UsernameDoesNotContainLowercaseException, PasswordDoesNotContainUppercaseException, PasswordTooSmallException, UserNotPersistedException, UserNotFoundException, PasswordDoesNotContainLowercaseException, UsernameTooSmallException, PasswordDoesNotContainNumberException, UsernameTooLongException, PasswordTooLargeException {
        String username = "User1";
        String password = "Password";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userService.addUser(user);
    }

    /**
     * Tests to ensure we can't have a password that is too long.
     * @throws UsernameDoesNotContainUppercaseException username doesn't contain an uppercase letter.
     * @throws UsernameDoesNotContainNumberException username doesn't contain a number.
     * @throws UsernameDoesNotContainLowercaseException username doesn't contain a lowercase letter.
     * @throws PasswordDoesNotContainUppercaseException password does not contain an uppercase letter.
     * @throws PasswordTooSmallException password is too small.
     * @throws UserNotPersistedException user is not persisted.
     * @throws UserNotFoundException user is not found in the DB.
     * @throws PasswordDoesNotContainLowercaseException password doesn't contain a lower case letter.
     * @throws UsernameTooSmallException username is too small.
     * @throws PasswordDoesNotContainNumberException password does not contain a number.
     */
    @Test (expected = PasswordTooLargeException.class)
    public void testPasswordTooLong() throws UsernameDoesNotContainUppercaseException, UsernameDoesNotContainNumberException, UsernameDoesNotContainLowercaseException, PasswordDoesNotContainUppercaseException, PasswordTooSmallException, UserNotPersistedException, UserNotFoundException, PasswordDoesNotContainLowercaseException, UsernameTooSmallException, PasswordDoesNotContainNumberException, UsernameTooLongException, PasswordTooLargeException {
        String username = "User1";
        String password = "ThisPasswordIsTooLong11243";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userService.addUser(user);
    }

    /**
     * Tests to ensure we can't have a username that is too long.
     * @throws UsernameDoesNotContainUppercaseException username doesn't contain an uppercase letter.
     * @throws UsernameDoesNotContainNumberException username doesn't contain a number.
     * @throws UsernameDoesNotContainLowercaseException username doesn't contain a lowercase letter.
     * @throws PasswordDoesNotContainUppercaseException password does not contain an uppercase letter.
     * @throws PasswordTooSmallException password is too small.
     * @throws UserNotPersistedException user is not persisted.
     * @throws UserNotFoundException user is not found in the DB.
     * @throws PasswordDoesNotContainLowercaseException password doesn't contain a lower case letter.
     * @throws UsernameTooSmallException username is too small.
     * @throws PasswordDoesNotContainNumberException password does not contain a number.
     */
    @Test (expected = UsernameTooLongException.class)
    public void testUsernameIsTooLong() throws UsernameDoesNotContainUppercaseException, UsernameDoesNotContainNumberException, UsernameDoesNotContainLowercaseException, PasswordDoesNotContainUppercaseException, PasswordTooSmallException, UserNotPersistedException, UserNotFoundException, PasswordDoesNotContainLowercaseException, UsernameTooSmallException, PasswordDoesNotContainNumberException, UsernameTooLongException, PasswordTooLargeException {
        String username = "ThisUsernameIsDefinitelyTooLong1123";
        String password = "Password1";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userService.addUser(user);
    }

    @Test (expected = PasswordDoesNotContainUppercaseException.class)
    public void testABC1Fail() throws UsernameTooLongException, UsernameDoesNotContainUppercaseException, UsernameDoesNotContainNumberException, UsernameDoesNotContainLowercaseException, PasswordDoesNotContainUppercaseException, PasswordTooSmallException, UserNotPersistedException, UserNotFoundException, PasswordDoesNotContainLowercaseException, UsernameTooSmallException, PasswordDoesNotContainNumberException, PasswordTooLargeException {
        String username = "Jalannin1";
        String password = "abc1";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userService.addUser(user);
    }
}