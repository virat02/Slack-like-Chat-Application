package edu.northeastern.ccs.im.controller;

import edu.northeastern.ccs.im.communication.CommunicationUtils;
import edu.northeastern.ccs.im.communication.NetworkResponse;
import edu.northeastern.ccs.im.communication.NetworkResponseImpl;
import edu.northeastern.ccs.im.communication.PayloadImpl;
import edu.northeastern.ccs.im.service.UserService;
import edu.northeastern.ccs.im.userGroup.User;

public final class UserController implements IController {

    private UserService userService = new UserService();

    public NetworkResponse addEntity(Object object) {
        try {
            return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                    new PayloadImpl(CommunicationUtils.toJson(UserService.addUser(object))));
        }   catch (IllegalArgumentException e) {
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(null));
        }
    }

    public NetworkResponse updateEntity(Object object) {
        try {
            return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                    new PayloadImpl(CommunicationUtils.toJson(UserService.update(object))));
        } catch (IllegalArgumentException e) {
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(null));
        }
    }

    public NetworkResponse deleteEntity(Object entity) {
        try {
            return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                    new PayloadImpl(CommunicationUtils.toJson(UserService.delete(entity))));
        } catch (IllegalArgumentException e) {
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(null));
        }
    }

    public NetworkResponse searchEntity(String username) {
        try {
            return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                    new PayloadImpl(CommunicationUtils.toJson(UserService.search(username))));
        } catch (IllegalArgumentException e) {
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(null));
        }
    }

    public NetworkResponse loginUser(Object potentialUser) {
        try {
            User newUser = UserService.loginUser(potentialUser);
            if(newUser == null) {
                return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                        new PayloadImpl(null));
            }
            return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                    new PayloadImpl(CommunicationUtils.toJson(newUser)));
        } catch (IllegalArgumentException e) {
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(null));
        }
    }

    public NetworkResponse followUser(String username, User currentUser) {
        try {
            User newUser = UserService.follow(username, currentUser);
            if(newUser == null) {
                return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                        new PayloadImpl(null));
            }
            return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                    new PayloadImpl(CommunicationUtils.toJson(newUser)));
        } catch (IllegalArgumentException e) {
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(null));
        }
    }

    /**
     * Get the followers for this user
     * @param username
     * @return
     */
    public NetworkResponse viewFollowers(String username) {
        try {
            return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                    new PayloadImpl(CommunicationUtils.toJsonArray(userService.getFollowers(username))));
        } catch (IllegalArgumentException e) {
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(null));
        }
    }

    /**
     * Get the followees for this user
     * @param username
     * @return
     */
    public NetworkResponse viewFollowees(String username) {
        try {
            return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                    new PayloadImpl(CommunicationUtils.toJsonArray(userService.getFollowees(username))));
        } catch (IllegalArgumentException e) {
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(null));
        }
    }
}
