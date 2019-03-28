package edu.northeastern.ccs.im.controller;

import edu.northeastern.ccs.im.communication.CommunicationUtils;
import edu.northeastern.ccs.im.communication.NetworkResponse;
import edu.northeastern.ccs.im.communication.NetworkResponseImpl;
import edu.northeastern.ccs.im.communication.PayloadImpl;
import edu.northeastern.ccs.im.customexceptions.*;
import edu.northeastern.ccs.im.service.UserService;
import edu.northeastern.ccs.im.user_group.Invite;
import edu.northeastern.ccs.im.user_group.User;

/**
 * Controller that calls the service class and loads the Network Response with the status and Payload.
 */
public final class UserController implements IController<User> {

    private static final String USER_NOT_PERSISTED_JSON = "{\"message\" : \"Username already taken\"}";
    private static final String USER_NOT_FOUND_JSON = "{\"message\" : \"Invalid Username\"}";
    private static final String LIST_OF_USERS_NOT_FOUND_JSON = "{\"message\" : \"Invalid Username\"}";
    private static final String INVITE_NOT_UPDATED = "{\"message\" : \"Invite not updated\"}";
    private static final String INVITE_NOT_DELETED = "{\"message\" : \"Invite could not be deleted\"}";
    private static final String INVITE_NOT_FOUND = "{\"message\" : \"Invalid Invite\"}";
    private static final String INVITE_NOT_ADDED =  "{\"message\" : \"Unable to make invite!\"}";
    private static final String GROUP_NOT_FOUND_JSON = "{\"message\" : \"Invalid Group\"}";
    private static final String USER_NOT_MODERATOR_JSON = "{\"message\" : \"User is not the moderator of the group\"}";
    private static final String USERNAME_TOO_SMALL = "{\"message\" : \"Username needs to be at least 4 digits long.\"}";
    private static final String USERNAME_NO_LOWER = "{\"message\" : \"Username needs to contain at least one lower case letter.\"}";
    private static final String USERNAME_NO_UPPER = "{\"message\" : \"Username needs to contain at least one upper case letter.\"}";
    private static final String USERNAME_NO_NUMBER = "{\"message\" : \"Username needs to contain at least one number.\"}";
    private static final String PASS_INCORRECT = "{\"message\" : \"Password must be between 4 and 20 digits long, contain " +
            "at least one number, one uppercase letter and one lowercase letter.\"}";
    private static final String USERNAME_TOO_LONG = "{\"message\" : \"Username can't be more than 20 digits long.\"}";







    private UserService userService = new UserService();

    /**
     * Sets the user service for the controller.
     * @param userService the user service the controller will be using to load on the payload.
     */
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Adds a user to the database.
     * @param user the loaded
     * @return Network response with the new User on the payload.
     */
    public NetworkResponse addEntity(User user) {
        try {
            return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                    new PayloadImpl(CommunicationUtils.toJson(userService.addUser(user))));
        }
        catch(UserNotFoundException e){
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(USER_NOT_FOUND_JSON));
        } catch (UserNotPersistedException e){
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(USER_NOT_PERSISTED_JSON));
        } catch (UsernameTooSmallException e) {
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(USERNAME_TOO_SMALL));
        } catch (UsernameDoesNotContainLowercaseException e) {
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(USERNAME_NO_LOWER));
        } catch (UsernameDoesNotContainNumberException e) {
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(USERNAME_NO_NUMBER));
        } catch (UsernameDoesNotContainUppercaseException e) {
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(USERNAME_NO_UPPER));
        } catch (PasswordTooSmallException | PasswordDoesNotContainLowercaseException
                | PasswordDoesNotContainUppercaseException | PasswordDoesNotContainNumberException | PasswordTooLargeException e) {
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(PASS_INCORRECT));
        } catch (UsernameTooLongException e) {
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(USERNAME_TOO_LONG));
        }
    }

    /**
     * Sends the user to the UserService where we are trying to update.
     * @param user being updated.
     * @return The Network Response with the new User instance loaded on the payload.
     */
    public NetworkResponse updateEntity(User user) {
        try {
            return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                    new PayloadImpl(CommunicationUtils.toJson(userService.update(user))));
        }
        catch(UserNotFoundException e){
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(USER_NOT_FOUND_JSON));
        }
    }

    /**
     * Deletes the User we are passing to this method.
     * @param user being deleted
     * @return the Network Response with the User deleted loaded onto the payload.
     */
    public NetworkResponse deleteEntity(User user) {
        try {
            return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                    new PayloadImpl(CommunicationUtils.toJson(userService.delete(user))));
        }
        catch(UserNotFoundException e){
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(USER_NOT_FOUND_JSON));
        }
    }

    /**
     * Searches for the user by username and returns the user loaded on the Network Response.
     * @param username the name of the user that is being searched for.
     * @return the Network Response with the User loaded on it if the user was found in the database.
     */
    public NetworkResponse searchEntity(String username) {
        try {
            return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                    new PayloadImpl(CommunicationUtils.toJson(userService.search(username))));
        }
        catch(UserNotFoundException e){
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(USER_NOT_FOUND_JSON));
        }

    }

    /**
     * Tries to login a potential user to the database.
     * @param potentialUser is the User that is trying to login.
     * @return Network Response that we load a payload on and let the implementer know if the response failed or
     * succeeded.
     */
    public NetworkResponse loginUser(User potentialUser) {
        try {
            User newUser = userService.loginUser(potentialUser);
            return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                    new PayloadImpl(CommunicationUtils.toJson(newUser)));
        }
        catch(UserNotFoundException e){
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(USER_NOT_FOUND_JSON));
        }
    }

    /**
     * A followUser method made where the current user is trying to follow the user with said Username.
     * @param username of the user being followed.
     * @param currentUser the user trying to follow a new user.
     * @return returns the new updated user object
     */
    public NetworkResponse followUser(String username, User currentUser) {
        try {
            User newUser = userService.follow(username, currentUser);
            return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                    new PayloadImpl(CommunicationUtils.toJson(newUser)));
        }
        catch(UserNotFoundException e){
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(USER_NOT_FOUND_JSON));
        }
    }

    /**
     * A unfollowUser method made where the current user is trying to unfollow the user with said
     * Username.
     * @param username of the user being followed.
     * @param currentUser the user trying to follow a new user.
     * @return returns the new updated user object
     */
    public NetworkResponse unfollowUser(String username, User currentUser) {
        try {
            User newUser = userService.unfollow(username, currentUser);
            return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                    new PayloadImpl(CommunicationUtils.toJson(newUser)));
        }
        catch(UserNotFoundException e){
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(USER_NOT_FOUND_JSON));
        }
    }

    /**
     * Get the followers for this user
     * @param username of the follower.
     * @return Network response with a status and a payload loaded with a List of Users.
     */
    public NetworkResponse viewFollowers(String username) {
        try {
            return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                    new PayloadImpl(CommunicationUtils.toJsonArray(userService.getFollowers(username))));
        }
        catch(ListOfUsersNotFound e){
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(LIST_OF_USERS_NOT_FOUND_JSON));
        }
        catch(UserNotFoundException e){
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(USER_NOT_FOUND_JSON));
        }
    }

    /**
     * Get the followees for this user.
     * @param username of the person doing the following.
     * @return Network response with a status and a payload loaded with a List of Users.
     */
    public NetworkResponse viewFollowees(String username) {
        try {
            return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                    new PayloadImpl(CommunicationUtils.toJsonArray(userService.getFollowees(username))));
        }
        catch(ListOfUsersNotFound e){
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(LIST_OF_USERS_NOT_FOUND_JSON));
        }
        catch(UserNotFoundException e){
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(USER_NOT_FOUND_JSON));
        }
    }

    /**
     * Sends an invite to the receiver from the sender for a specific group using the invite message
     * @param invite to be sent to a new user.
     * @return NetworkResponse with the status either SUCCESSFUL or FAILED with the invitation loaded on the payload.
     */
    public NetworkResponse sendInvite(Invite invite) {
        try {
            userService.sendInvite(invite);
            return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL, () -> "{\"message\": \"Invitation succesfully created\"}");
        } catch (InviteNotAddedException e) {
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(CommunicationUtils.toJson(INVITE_NOT_ADDED)));
        } catch (InviteNotFoundException | UserNotFoundException e) {
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(CommunicationUtils.toJson(INVITE_NOT_FOUND)));
        } catch (GroupNotFoundException e) {
            e.printStackTrace();
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(CommunicationUtils.toJson(INVITE_NOT_FOUND)));
        }
    }

    /**
     * Deletes the current invite, not from the database, but sets the invite's status to deleted.
     * @param invite to be sent.
     * @return the Network response loaded with the deleted invite, or the error message found.
     */
    public NetworkResponse deleteInvite(Invite invite) {
        try {
            return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                    new PayloadImpl(CommunicationUtils.toJson(userService.deleteInvite(invite))));
        } catch (InviteNotDeletedException e) {
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(CommunicationUtils.toJson(INVITE_NOT_DELETED)));
        }
    }

    /**
     * Updates the invite by sending it to the service to be updated.
     * @param invite to be updated.
     * @return the Network response with either the invite loaded on or the error message if something went
     * wrong during the update process.
     */
    public NetworkResponse updateInvite(Invite invite) {
        try {
            return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                    new PayloadImpl(CommunicationUtils.toJson(userService.updateInvite(invite))));
        } catch (InviteNotFoundException e) {
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(CommunicationUtils.toJson(INVITE_NOT_FOUND)));
        } catch (InviteNotUpdatedException e) {
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(CommunicationUtils.toJson(INVITE_NOT_UPDATED)));
        }
    }

    /**
     * Method to lead the Network Response with a Payload filled with a JSON object and a status of either
     * SUCCESSFUL or FAILED dependent on a successful of failed operation
     * @param groupCode of the group we are trying to find the invites for
     * @return NetworkResponse with the invite JSON loaded on.
     */
    public NetworkResponse searchInviteByGroupCode(String groupCode, String username) {
        try {
            return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                    new PayloadImpl(CommunicationUtils.toJsonArray(userService.searchInviteByGroupCode(groupCode,username))));
        } catch (GroupNotFoundException e) {
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(GROUP_NOT_FOUND_JSON));
        } catch (InviteNotFoundException e) {
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(INVITE_NOT_FOUND));
        }
        catch (IllegalAccessException e){
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(USER_NOT_MODERATOR_JSON));
        }
        catch (UserNotFoundException e){
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(USER_NOT_FOUND_JSON));
        }
    }
}
