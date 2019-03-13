package edu.northeastern.ccs.im.controller;

import edu.northeastern.ccs.im.communication.CommunicationUtils;
import edu.northeastern.ccs.im.communication.NetworkResponse;
import edu.northeastern.ccs.im.communication.NetworkResponseImpl;
import edu.northeastern.ccs.im.communication.PayloadImpl;
import edu.northeastern.ccs.im.service.UserService;
import edu.northeastern.ccs.im.userGroup.User;

public final class UserController implements IController {
    private UserService userService;

    public NetworkResponse addEntity(Object object) {
        try {
            return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                    new PayloadImpl(CommunicationUtils.toJson(userService.addUser(object))));
        } catch (IllegalArgumentException e) {
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(null));
        }
    }

    public User updateEntity(Object object) {
        return userService.update(object);
    }

    public User deleteEntity(Object entity) {
        return userService.delete(entity);
    }

    public User searchEntity(String username) {
        return userService.search(username);
    }

    public void followUser(String username, User currentUser) {
        userService.follow(username, currentUser);
    }
}
