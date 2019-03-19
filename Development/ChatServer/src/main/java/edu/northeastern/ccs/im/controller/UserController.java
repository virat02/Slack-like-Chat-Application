package edu.northeastern.ccs.im.controller;

import edu.northeastern.ccs.im.communication.CommunicationUtils;
import edu.northeastern.ccs.im.communication.NetworkResponse;
import edu.northeastern.ccs.im.communication.NetworkResponseImpl;
import edu.northeastern.ccs.im.communication.PayloadImpl;
import edu.northeastern.ccs.im.service.UserService;
import edu.northeastern.ccs.im.user_group.User;

/**
 * Controller that calls the service class and loads the Network Response with the status and Payload.
 */
public final class UserController implements IController {
    private UserService userService = new UserService();

    /**
     * Sets the user service for the controller.
     * @param userService the user service the controller will be using to load on the payload.
     */
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Adds an entity to the database.
     * @param object the loaded
     * @return Network response with the new Entity on the payload.
     */
    public NetworkResponse addEntity(Object object) {
        return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                new PayloadImpl(CommunicationUtils.toJson(userService.addUser(object))));
    }

    /**
     * Sense the object to the UserService where we are trying to update the user.
     * @param object being updated.
     * @return The Network Response with the new User instance loaded on the payload.
     */
    public NetworkResponse updateEntity(Object object) {
        return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                new PayloadImpl(CommunicationUtils.toJson(userService.update(object))));
    }

    /**
     * Deletes the User we are passing to this method.
     * @param entity being deleted
     * @return the Network Response with the User deleted loaded onto the payload.
     */
    public NetworkResponse deleteEntity(Object entity) {
        return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                new PayloadImpl(CommunicationUtils.toJson(userService.delete(entity))));
    }

    /**
     * Searches for the user by username and returns the user loaded on the Network Response.
     * @param username the name of the user that is being searched for.
     * @return the Network Response with the User loaded on it if the user was found in the database.
     */
    public NetworkResponse searchEntity(String username) {
        return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                new PayloadImpl(CommunicationUtils.toJson(userService.search(username))));

    }

    /**
     * Trys to login a potential user to the database.
     * @param potentialUser is the User that is trying to login.
     * @return Network Response that we load a payload on and let the implementer know if the response failed or
     * succeeded.
     */
    public NetworkResponse loginUser(Object potentialUser) {
        User newUser = userService.loginUser(potentialUser);
        if (newUser == null) {
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(null));
        }
        return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                new PayloadImpl(CommunicationUtils.toJson(newUser)));

    }

    /**
     * A followUser method made where the current user is trying to follow the user with said Username.
     * @param username of the user being followed.
     * @param currentUser the user trying to follow a new user.
     * @return returns the new updated user object
     */
    public NetworkResponse followUser(String username, User currentUser) {
        User newUser = userService.follow(username, currentUser);
        if(newUser == null) {
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(null));
        }
        return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                new PayloadImpl(CommunicationUtils.toJson(newUser)));
    }

    /**
     * Get the followers for this user
     * @param username of the follower.
     * @return Network response with a status and a payload loaded with a List of Users.
     */
    public NetworkResponse viewFollowers(String username) {
        return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                new PayloadImpl(CommunicationUtils.toJsonArray(userService.getFollowers(username))));

    }

    /**
     * Get the followees for this user.
     * @param username of the person doing the following.
     * @return Network response with a status and a payload loaded with a List of Users.
     */
    public NetworkResponse viewFollowees(String username) {
        return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                new PayloadImpl(CommunicationUtils.toJsonArray(userService.getFollowees(username))));

    }
}
