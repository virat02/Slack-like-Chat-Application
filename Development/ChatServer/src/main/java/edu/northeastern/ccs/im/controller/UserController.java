package edu.northeastern.ccs.im.controller;

import edu.northeastern.ccs.im.service.IService;
import edu.northeastern.ccs.im.service.UserService;
import edu.northeastern.ccs.im.userGroup.IUserGroup;
import edu.northeastern.ccs.im.view.View;
import edu.northeastern.ccs.jpa.User;

import java.util.List;

public class UserController implements IController {
    private UserService userService;
    private View view;


    public void addIUserGroup(IUserGroup iUserGroup) {
        List<IUserGroup> userList;
        try {
            userList = userService.addUser(iUserGroup);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Can't add User");
        }
        view.showUsers(userList);
    }

    @Override
    public IUserGroup getIUserGroup(IUserGroup iUserGroup) {
        return null;
    }

    @Override
    public void updateIUserGroup(IUserGroup iUserGroup) {

    }

    @Override
    public void deleteIUserGroup(IUserGroup iUserGroup) {

    }
}
