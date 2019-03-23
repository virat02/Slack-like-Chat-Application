package edu.northeastern.ccs.im.controller;

import edu.northeastern.ccs.im.communication.CommunicationUtils;
import edu.northeastern.ccs.im.communication.NetworkResponse;
import edu.northeastern.ccs.im.communication.NetworkResponseImpl;
import edu.northeastern.ccs.im.communication.PayloadImpl;
import edu.northeastern.ccs.im.customexceptions.ListOfUsersNotFound;
import edu.northeastern.ccs.im.customexceptions.UserNotFoundException;
import edu.northeastern.ccs.im.customexceptions.UserNotPersistedException;
import edu.northeastern.ccs.im.service.UserService;
import edu.northeastern.ccs.im.service.jpa_service.Status;
import edu.northeastern.ccs.im.user_group.Group;
import edu.northeastern.ccs.im.user_group.User;

/**
 * Controller that calls the service class and loads the Network Response with the status and Payload.
 */
public final class UserController implements IController<User> {

    private static final String USER_NOT_PERSISTED_JSON = "{\"exceptionMessage\" : \"Jpa could not persist the user!\"}";
    private static final String USER_NOT_FOUND_JSON = "{\"exceptionMessage\" : \"Jpa could not find the user!\"}";
    private static final String LIST_OF_USERS_NOT_FOUND_JSON = "{\"exceptionMessage\" : \"Jpa could not find the list of users!\"}";

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
        }
        catch (UserNotPersistedException e){
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(USER_NOT_PERSISTED_JSON));
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
     * @param group the invite is for
     * @param sender the user sending the invite
     * @param receiver the user receiving the invite
     * @param status the status of the invite
     * @param inviteMessage message being sent to the receiver
     * @return NetworkResponse with the status either SUCCESSFUL or FAILED with the invitation loaded on the payload.
     */
    public NetworkResponse sendInvite(Group group, User sender, User receiver, Status status, String inviteMessage) {
        return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                new PayloadImpl(CommunicationUtils.toJson(userService.sendInvite(group, sender, receiver, status,
                        inviteMessage))));
    }
}
