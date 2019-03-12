package edu.northeastern.ccs.im.controller;

import edu.northeastern.ccs.im.service.UserService;
import edu.northeastern.ccs.im.userGroup.User;
import edu.northeastern.ccs.im.view.View;

public final class UserController implements IController {
    private UserService userService;
    private View view;

    public User addEntity(Object User) {
        try {
            return userService.addUser(User);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Can't add User");
        }
        /* TODO: Make a show method
         */
    }

    public User updateEntity(Object user) {
        return userService.update(user);
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
