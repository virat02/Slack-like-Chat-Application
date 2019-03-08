package edu.northeastern.ccs.im.controller;

import edu.northeastern.ccs.im.service.IService;
import edu.northeastern.ccs.im.service.UserService;
import edu.northeastern.ccs.im.userGroup.IUser;
import edu.northeastern.ccs.im.userGroup.IUserGroup;
import edu.northeastern.ccs.im.view.View;
import edu.northeastern.ccs.jpa.User;

import java.util.List;

public final class UserController implements IController {
    private UserService userService;
    private View view;

    public void addEntity(Object iUser) {
        try {
            userService.addUser(iUser);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Can't add User");
        }
        /* TODO: Make a show method
         */
    }

    public void updateEntity(Object user) {
        userService.update(user);
    }

    public void deleteEntity(Object entity) {
        userService.delete(entity);
    }

    public void searchUser(String username) {
        userService.search(username);
    }

    public void followUser(String username, IUser currentUser) {
        userService.follow(username, currentUser);
    }
}
