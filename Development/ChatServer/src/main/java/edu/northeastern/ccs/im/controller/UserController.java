package edu.northeastern.ccs.im.controller;

import edu.northeastern.ccs.im.service.UserService;
import edu.northeastern.ccs.im.userGroup.IUser;
import edu.northeastern.ccs.im.view.View;

public final class UserController implements IController {
    private UserService userService;
    private View view;

    public IUser addEntity(Object iUser) {
        try {
            return userService.addUser(iUser);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Can't add User");
        }
        /* TODO: Make a show method
         */
    }

    public IUser updateEntity(Object user) {
        return userService.update(user);
    }

    public IUser deleteEntity(Object entity) {
        return userService.delete(entity);
    }

    public IUser searchEntity(String username) {
        return userService.search(username);
    }

    public void followUser(String username, IUser currentUser) {
        userService.follow(username, currentUser);
    }
}
