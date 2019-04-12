package edu.northeastern.ccs.im.controller;
import edu.northeastern.ccs.im.communication.NetworkResponse;
import edu.northeastern.ccs.im.customexceptions.*;
import edu.northeastern.ccs.im.service.UserService;
import edu.northeastern.ccs.im.user_group.Invite;
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
    private UserController userController;
    private UserService userService;
    private User userOne;

    /**
     * Sets up the controllers variables and mocks the user service.
     */
    @Before
    public void setUp() {
        userController = UserController.getInstance();
        userService = mock(UserService.class);
        userOne = new User();
        userOne.setPassword("yo123");
        userOne.setUsername("Jerry");
    }

    /**
     * Tests the successful addition of a user to a database.
     * @throws UserNotFoundException if the user is not found.
     * @throws UserNotPersistedException if the user is not persisted in DB.
     * @throws UsernameInvalidException if the username is invalid
     */
    @Test
    public void testCreateEntity() throws UserNotPersistedException,
            UserNotFoundException, UsernameInvalidException, PasswordInvalidException {
        when(userService.addUser(any())).thenReturn(userOne);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.addEntity(userOne);
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(userService).addUser(any());
    }

    /**
     * Tests the unsuccessful addition of a user to a database.
     * @throws UserNotPersistedException if the user is not created.
     * @throws UserNotFoundException if the user is not found.
     * @throws UsernameInvalidException if the username is invalid.
     * @throws PasswordInvalidException if the password is invalid.
     */
    @Test
    public void testUserNotFailException() throws UserNotPersistedException, UserNotFoundException,
            UsernameInvalidException, PasswordInvalidException {
        when(userService.addUser(any())).thenThrow(UserNotFoundException.class);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.addEntity(userOne);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(userService).addUser(any());
    }

    /**
     * Tests the unsuccessful addition of a user to a database.
     * @throws UserNotFoundException if the user is not found.
     * @throws UserNotPersistedException if the user is not created.
     * @throws UsernameInvalidException if the username is invalid.
     * @throws PasswordInvalidException if the password is invalid.
     */
    @Test
    public void testCreateEntityFail() throws UserNotPersistedException,
            UserNotFoundException, UsernameInvalidException, PasswordInvalidException {
        when(userService.addUser(any())).thenThrow(UserNotPersistedException.class);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.addEntity(userOne);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(userService).addUser(any());
    }

    /**
     * Tests the successful update of a user to a database.
     * @throws UserNotFoundException if the user is not found.
     */
    @Test
    public void testUpdateEntity() throws UserNotFoundException{
        when(userService.update(any())).thenReturn(userOne);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.updateEntity(userOne);
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(userService).update(any());
    }

    /**
     * Tests the unsuccessful update of a user to a database.
     * @throws UserNotFoundException if the user is not found.
     */
    @Test
    public void testUpdateEntityFail() throws UserNotFoundException{
        when(userService.update(any())).thenThrow(UserNotFoundException.class);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.updateEntity(userOne);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(userService).update(any());
    }

    /**
     * Tests the successful deletion of a d to a database.
     * @throws UserNotFoundException if the user is not found.
     */
    @Test
    public void testDeleteEntity() throws UserNotFoundException {
        when(userService.delete(any())).thenReturn(userOne);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.deleteEntity(userOne);
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(userService).delete(any());
    }

    /**
     * Tests the unsuccessful deletion of a d to a database.
     * @throws UserNotFoundException if the user is not found.
     */
    @Test
    public void testDeleteEntityFail() throws UserNotFoundException {
        when(userService.delete(any())).thenThrow(UserNotFoundException.class);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.deleteEntity(userOne);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(userService).delete(any());
    }

    /**
     * Tests the unsuccessful search of a user to a database.
     * @throws UserNotFoundException if the user is not found.
     */
    @Test
    public void testSearchEntityFail() throws UserNotFoundException{
        when(userService.search(anyString())).thenThrow(UserNotFoundException.class);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.searchEntity("This");
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(userService).search(anyString());
    }

    /**
     * Tests the successful search of a user to a database.
     * @throws UserNotFoundException if the user is not found.
     */
    @Test
    public void testSearchEntity() throws UserNotFoundException{
        when(userService.search(anyString())).thenReturn(userOne);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.searchEntity("This");
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(userService).search(anyString());
    }

    /**
     * Tests the successful login of a user to a database.
     * @throws UserNotFoundException if the user is not found.
     */
    @Test
    public void testLoginEntity() throws UserNotFoundException{
        when(userService.loginUser(any())).thenReturn(userOne);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.loginUser(userOne);
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(userService).loginUser(any());
    }

    /**
     * Tests the failed login of a user to a database.
     * @throws UserNotFoundException if the user is not found.
     */
    @Test
    public void testLoginEntityFail() throws UserNotFoundException{
        when(userService.loginUser(any())).thenThrow(UserNotFoundException.class);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.loginUser(userOne);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(userService).loginUser(any());
    }

    /**
     * Tests the successful follow of a user to a database.
     * @throws UserNotFoundException if the user is not found
     */
    @Test
    public void testFollowEntity() throws UserNotFoundException{
        when(userService.follow(anyString(), any())).thenReturn(userOne);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.followUser("Yas", userOne);
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(userService).follow(anyString(), any());
    }

    /**
     * Tests the unsuccessful follow of a user to a database.
     * @throws UserNotFoundException if the user is not found
     */
    @Test
    public void testFollowEntityFail() throws UserNotFoundException{
        when(userService.follow(anyString(), any())).thenThrow(UserNotFoundException.class);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.followUser("Yas", userOne);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(userService).follow(anyString(), any());
    }


    /**
     * Tests the successful getting of followers.
     * @throws ListOfUsersNotFound if the list of users is not found.
     * @throws UserNotFoundException if the user is not found
     */
    @Test
    public void testViewFollowers() throws ListOfUsersNotFound, UserNotFoundException {
        List<User> users = new ArrayList<>();
        when(userService.getFollowers(any())).thenReturn(users);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.viewFollowers("Us");
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(userService).getFollowers(any());
    }

    /**
     * Tests the unsuccessful getting of followers.
     * @throws ListOfUsersNotFound if the list of users isn't found
     * @throws UserNotFoundException if the user is not found.
     */
    @Test
    public void testViewFollowersFail() throws ListOfUsersNotFound, UserNotFoundException {
        when(userService.getFollowers(any())).thenThrow(ListOfUsersNotFound.class);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.viewFollowers("Us");
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(userService).getFollowers(any());
    }

    /**
     * Tests the ununsuccessful getting of followers.
     * @throws ListOfUsersNotFound if the list of users is not found
     * @throws UserNotFoundException if the users aren't found
     */
    @Test
    public void testViewFollowersFailNotFound() throws ListOfUsersNotFound, UserNotFoundException {
        when(userService.getFollowers(any())).thenThrow(UserNotFoundException.class);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.viewFollowers("Us");
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(userService).getFollowers(any());
    }

    /**
     * Tests the successful getting of followees.
     * @throws UserNotFoundException if the user is not found
     * @throws ListOfUsersNotFound if the list of users is not found
     */
    @Test
    public void testViewFollowees() throws UserNotFoundException, ListOfUsersNotFound {
        List<User> users = new ArrayList<>();
        when(userService.getFollowees(any())).thenReturn(users);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.viewFollowees("Us");
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(userService).getFollowees(any());
    }

    /**
     * Tests the unsuccessful getting of followees.
     * @throws UserNotFoundException if user is not found.
     * @throws ListOfUsersNotFound if the list of users isn't found
     */
    @Test
    public void testViewFolloweesFailNotFound() throws UserNotFoundException, ListOfUsersNotFound {
        when(userService.getFollowees(any())).thenThrow(UserNotFoundException.class);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.viewFollowees("Us");
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(userService).getFollowees(any());
    }

    /**
     * Tests the unsuccessful getting of followees.
     * @throws UserNotFoundException if user is not found
     * @throws ListOfUsersNotFound if the list of users isn't found
     */
    @Test
    public void testViewFolloweesFail() throws UserNotFoundException, ListOfUsersNotFound {
        when(userService.getFollowees(any())).thenThrow(ListOfUsersNotFound.class);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.viewFollowees("Us");
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(userService).getFollowees(any());
    }

    /**
     * Tests to ensure the send invite works properly when an exception is thrown.
     * @throws InviteNotFoundException if invite is not found
     * @throws InviteNotAddedException if we can't add invite to DB
     */
    @Test
    public void testSendInviteFail() throws InviteNotFoundException, GroupNotFoundException, UserNotFoundException, InviteNotAddedException {
        Invite invite = new Invite();
        when(userService.sendInvite(any())).thenThrow(InviteNotFoundException.class);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.sendInvite(invite);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(userService).sendInvite(any());
    }

    /**
     * Tests to ensure the send invite will throw the proper exception.
     * @throws InviteNotFoundException if invite is not found
     * @throws GroupNotFoundException if the group is not found
     * @throws UserNotFoundException if the user is not found
     * @throws InviteNotAddedException if the invite is not found
     */
    @Test
    public void testSendInviteFailGroupNotFound() throws InviteNotFoundException, GroupNotFoundException, UserNotFoundException, InviteNotAddedException {
        Invite invite = new Invite();
        when(userService.sendInvite(any())).thenThrow(GroupNotFoundException.class);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.sendInvite(invite);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(userService).sendInvite(any());
    }


    /**
     * Tests to ensure the send invite works properly when an exception is thrown.
     * @throws InviteNotFoundException if invite is not found
     * @throws InviteNotAddedException if we can't add invite to DB
     */
    @Test
    public void testSendInviteFailNotAdded() throws InviteNotFoundException, InviteNotAddedException, UserNotFoundException, GroupNotFoundException {
        Invite invite = new Invite();
        when(userService.sendInvite(any())).thenThrow(InviteNotAddedException.class);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.sendInvite(invite);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(userService).sendInvite(any());
    }

    /**
     * Tests to ensure the send invite works properly.
     * @throws InviteNotFoundException if invite is not found
     * @throws InviteNotAddedException if we can't add invite to DB
     */
    @Test
    public void testSendInvite() throws InviteNotFoundException, InviteNotAddedException, UserNotFoundException, GroupNotFoundException {
        Invite invite = new Invite();
        when(userService.sendInvite(any())).thenReturn(invite);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.sendInvite(invite);
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(userService).sendInvite(any());
    }

    /**
     * Tests to ensure the send invite works properly when an exception is thrown.
     * @throws InviteNotDeletedException if we can't delete invite to DB
     */
    @Test
    public void testDeleteInviteFailNotAdded() throws InviteNotDeletedException {
        Invite invite = new Invite();
        when(userService.deleteInvite(any())).thenThrow(InviteNotDeletedException.class);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.deleteInvite(invite);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(userService).deleteInvite(any());
    }

    /**
     * Tests to ensure the send invite works properly.
     * @throws InviteNotDeletedException if the invite wasn't deleted.
     */
    @Test
    public void testDeleteInvite() throws InviteNotDeletedException {
        Invite invite = new Invite();
        when(userService.deleteInvite(any())).thenReturn(invite);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.deleteInvite(invite);
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(userService).deleteInvite(any());
    }

    /**
     * Tests to ensure the update invite works properly when an exception is thrown.
     * @throws InviteNotFoundException if invite is not found
     * @throws InviteNotUpdatedException if we can't update invite in DB
     */
    @Test
    public void testUpdateInviteFail() throws InviteNotFoundException, InviteNotUpdatedException{
        Invite invite = new Invite();
        when(userService.updateInvite(any())).thenThrow(InviteNotUpdatedException.class);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.updateInvite(invite);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(userService).updateInvite(any());
    }

    /**
     * Tests to ensure the update invite works properly when an exception is thrown.
     * @throws InviteNotFoundException if invite is not found
     * @throws InviteNotUpdatedException if we can't update the invite in the DB
     */
    @Test
    public void testUpdateFailNotAdded() throws InviteNotFoundException, InviteNotUpdatedException {
        Invite invite = new Invite();
        when(userService.updateInvite(any())).thenThrow(InviteNotFoundException.class);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.updateInvite(invite);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(userService).updateInvite(any());
    }

    /**
     * Tests to ensure the update invite works properly.
     * @throws InviteNotFoundException if invite is not found
     * @throws InviteNotUpdatedException if we can't update the invite in the DB
     */
    @Test
    public void testUpdateInvite() throws InviteNotFoundException, InviteNotUpdatedException {
        Invite invite = new Invite();
        when(userService.updateInvite(any())).thenReturn(invite);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.updateInvite(invite);
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(userService).updateInvite(any());
    }

    /**
     * Tests to ensure the search invite works properly when an exception is thrown.
     * @throws InviteNotFoundException if invite is not found
     * @throws GroupNotFoundException if we can't find the group in the DB.
     * @throws UserNotFoundException if we can't find the user in the DB.
     */
    @Test
    public void testSearchInviteFail() throws InviteNotFoundException, GroupNotFoundException,
            UserNotFoundException{
        String groupCode = "Group";
        String username = "Username";
        when(userService.searchInviteByGroupCode(anyString(), anyString())).thenThrow(GroupNotFoundException.class);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.searchInviteByGroupCode(groupCode, username);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(userService).searchInviteByGroupCode(any(), anyString());
    }

    /**
     * Tests to ensure the search invite works properly when an exception is thrown.
     * @throws InviteNotFoundException if invite is not found
     * @throws GroupNotFoundException if we can't find the group in the DB.
     * @throws UserNotFoundException if we can't find the user in the DB.
     */
    @Test
    public void testSearchInviteFailUserNotFound() throws InviteNotFoundException, GroupNotFoundException, UserNotFoundException{
        String groupCode = "Group";
        String username = "Username";
        when(userService.searchInviteByGroupCode(anyString(), anyString())).thenThrow(UserNotFoundException.class);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.searchInviteByGroupCode(groupCode, username);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(userService).searchInviteByGroupCode(any(), anyString());
    }

    /**
     * Tests to ensure the search invite works properly when an exception is thrown.
     * @throws InviteNotFoundException if invite is not found
     * @throws GroupNotFoundException if we can't find the group in the DB.
     * @throws UserNotFoundException if we can't find the user in the DB.
     */
    @Test
    public void testSearchInviteFailInviteNotFound() throws InviteNotFoundException, GroupNotFoundException, UserNotFoundException{
        String groupCode = "Group";
        String username = "Username";
        when(userService.searchInviteByGroupCode(anyString(), anyString())).thenThrow(InviteNotFoundException.class);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.searchInviteByGroupCode(groupCode, username);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(userService).searchInviteByGroupCode(any(), anyString());
    }

    /**
     * Tests to ensure the search invite works properly.
     * @throws InviteNotFoundException if invite is not found
     * @throws GroupNotFoundException if we can't find the group in the DB.
     * @throws UserNotFoundException if we can't find the user in the DB.
     */
    @Test
    public void testSearchInvite() throws InviteNotFoundException, GroupNotFoundException,
            UserNotFoundException{
        String groupCode = "Group";
        String username = "Username";
        List<Invite> invites = new ArrayList<>();
        when(userService.searchInviteByGroupCode(anyString(), anyString())).thenReturn(invites);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.searchInviteByGroupCode(groupCode, username);
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(userService).searchInviteByGroupCode(anyString(), anyString());
    }

    /**
     * Tests to ensure the unfollow user works properly when an exception is thrown.
     * @throws UserNotFoundException if we can't find the user in the DB.
     */
    @Test
    public void testUnfollowFail() throws UserNotFoundException,UnfollowNotFollowingUserException {
        String username = "user";
        User user = new User();
        user.setUsername(username);
        when(userService.unfollow(anyString(), any())).thenThrow(UserNotFoundException.class);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.unfollowUser(user.getUsername(), user);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(userService).unfollow(anyString(), any());
    }

    /**
     * Tests to ensure the unfollow user works properly when an exception is thrown.
     * @throws UserNotFoundException if we can't find the user in the DB.
     */
    @Test
    public void testUnfollowFailTwo() throws UserNotFoundException,UnfollowNotFollowingUserException {
        String username = "user";
        User user = new User();
        user.setUsername(username);
        when(userService.unfollow(anyString(), any())).thenThrow(UnfollowNotFollowingUserException.class);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.unfollowUser(user.getUsername(), user);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(userService).unfollow(anyString(), any());
    }

    /**
     * Tests to ensure the unfollow user works properly.
     * @throws UserNotFoundException if invite is not found
     */
    @Test
    public void testUnfollow() throws UserNotFoundException,UnfollowNotFollowingUserException {
        String username = "user";
        User user = new User();
        user.setUsername(username);
        when(userService.unfollow(anyString(), any())).thenReturn(user);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.unfollowUser(user.getUsername(), user);
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(userService).unfollow(anyString(), any());
    }

    /**
     * Tests to ensure if the username is too small
     * @throws UsernameInvalidException username does not contain lowercase
     * @throws PasswordInvalidException password too small
     * @throws UserNotPersistedException User not persisted
     * @throws UserNotFoundException user not found exception
     */
    @Test
    public void testUsernameTooSmall() throws UsernameInvalidException, PasswordInvalidException, UserNotPersistedException, UserNotFoundException {
        when(userService.addUser(any())).thenThrow(UsernameInvalidException.class);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.addEntity(userOne);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(userService).addUser(any());
    }

    /**
     * Tests to ensure if the username throws if there are no lowercase letters
     * @throws UsernameInvalidException username does not contain lowercase
     * @throws PasswordInvalidException password too small
     * @throws UserNotPersistedException User not persisted
     * @throws UserNotFoundException user not found exception
     */
    @Test
    public void testUsernameNoLower() throws UsernameInvalidException, PasswordInvalidException,
            UserNotPersistedException, UserNotFoundException {
        when(userService.addUser(any())).thenThrow(UsernameInvalidException.class);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.addEntity(userOne);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(userService).addUser(any());
    }

    /**
     * Tests to ensure username uppercase error throws
     * @throws UsernameInvalidException username does not contain lowercase
     * @throws PasswordInvalidException password too small
     * @throws UserNotPersistedException User not persisted
     * @throws UserNotFoundException user not found exception
     */
    @Test
    public void testUsernameNoUpper() throws UsernameInvalidException, PasswordInvalidException, UserNotPersistedException, UserNotFoundException {
        when(userService.addUser(any())).thenThrow(UsernameInvalidException.class);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.addEntity(userOne);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(userService).addUser(any());
    }

    /**
     * Tests to ensure username throws the no number error.
     * @throws UsernameInvalidException username does not contain lowercase
     * @throws PasswordInvalidException password too small
     * @throws UserNotPersistedException User not persisted
     * @throws UserNotFoundException user not found exception
     */
    @Test
    public void testUsernameNoNumber() throws UsernameInvalidException, PasswordInvalidException, UserNotPersistedException, UserNotFoundException{
        when(userService.addUser(any())).thenThrow(UsernameInvalidException.class);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.addEntity(userOne);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(userService).addUser(any());
    }

    /**
     * Tests to ensure if the password is too small
     * @throws UsernameInvalidException username does not contain lowercase
     * @throws PasswordInvalidException password too small
     * @throws UserNotPersistedException User not persisted
     * @throws UserNotFoundException user not found exception
     */
    @Test
    public void testPasswordTooSmall() throws UsernameInvalidException, PasswordInvalidException, UserNotPersistedException, UserNotFoundException {
        when(userService.addUser(any())).thenThrow(PasswordInvalidException.class);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.addEntity(userOne);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(userService).addUser(any());
    }

    /**
     * Tests to ensure password no uppercase error throws.
     * @throws UsernameInvalidException username does not contain lowercase
     * @throws PasswordInvalidException password too small
     * @throws UserNotPersistedException User not persisted
     * @throws UserNotFoundException user not found exception
     */
    @Test
    public void testPasswordNoUppercase() throws UsernameInvalidException, PasswordInvalidException,
            UserNotPersistedException, UserNotFoundException {
        when(userService.addUser(any())).thenThrow(PasswordInvalidException.class);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.addEntity(userOne);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(userService).addUser(any());
    }
    /**
     * Tests to ensure the password contains a lower case letter
     * @throws UsernameInvalidException username does not contain lowercase
     * @throws PasswordInvalidException password too small
     * @throws UserNotPersistedException User not persisted
     * @throws UserNotFoundException user not found exception
     */
    @Test
    public void testPasswordNoLower() throws UsernameInvalidException, PasswordInvalidException,
            UserNotPersistedException, UserNotFoundException {
        when(userService.addUser(any())).thenThrow(PasswordInvalidException.class);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.addEntity(userOne);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(userService).addUser(any());
    }

    /**
     * Tests to ensure password contains a number.
     * @throws UsernameInvalidException username does not contain lowercase
     * @throws PasswordInvalidException password too small
     * @throws UserNotPersistedException User not persisted
     * @throws UserNotFoundException user not found exception
     */
    @Test
    public void testPasswordNoNumber() throws UsernameInvalidException, PasswordInvalidException, UserNotPersistedException, UserNotFoundException {
        when(userService.addUser(any())).thenThrow(PasswordInvalidException.class);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.addEntity(userOne);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(userService).addUser(any());
    }

    /**
     * Tests to ensure password can't be too long.
     * @throws UsernameInvalidException username does not contain lowercase
]     * @throws PasswordInvalidException password too small
     * @throws UserNotPersistedException User not persisted
     * @throws UserNotFoundException user not found exception
     */
    @Test
    public void testPasswordTooLong() throws UsernameInvalidException, PasswordInvalidException, UserNotPersistedException, UserNotFoundException {
        when(userService.addUser(any())).thenThrow(PasswordInvalidException.class);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.addEntity(userOne);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(userService).addUser(any());
    }

    /**
     * Tests to ensure username can't be too long.
     * @throws UsernameInvalidException username does not contain lowercase
     * @throws PasswordInvalidException password too small
     * @throws UserNotPersistedException User not persisted
     * @throws UserNotFoundException user not found exception
     */
    @Test
    public void testUsernameTooLong() throws UsernameInvalidException, PasswordInvalidException,
            UserNotPersistedException, UserNotFoundException {
        when(userService.addUser(any())).thenThrow(UsernameInvalidException.class);
        userController.setUserService(userService);
        NetworkResponse networkResponse = userController.addEntity(userOne);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(userService).addUser(any());
    }

    /**
     * A test for exit chat room in user controller.
     */
    @Test
    public void testExitChatRoom() {
        userController.setUserService(userService);
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, userController.exitChatRoom("123", "banjo").status());
    }



}