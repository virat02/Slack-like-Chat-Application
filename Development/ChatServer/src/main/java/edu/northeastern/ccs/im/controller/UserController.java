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
                    new PayloadImpl(CommunicationUtils.toJson(userService.addUser(object))));
        } catch (IllegalArgumentException e) {
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(null));
        }
    }

    public NetworkResponse updateEntity(Object object) {
        try {
            return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                    new PayloadImpl(CommunicationUtils.toJson(userService.update(object))));
        } catch (IllegalArgumentException e) {
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(null));
        }
    }

    public NetworkResponse deleteEntity(Object entity) {
        try {
            return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                    new PayloadImpl(CommunicationUtils.toJson(userService.delete(entity))));
        } catch (IllegalArgumentException e) {
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(null));
        }
    }

    public NetworkResponse searchEntity(String username) {
        try {
            return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                    new PayloadImpl(CommunicationUtils.toJson(userService.search(username))));
        } catch (IllegalArgumentException e) {
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(null));
        }
    }

    public void followUser(String username, User currentUser) {
        userService.follow(username, currentUser);
    }
}