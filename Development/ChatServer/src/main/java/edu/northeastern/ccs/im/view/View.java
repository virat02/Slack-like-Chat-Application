package edu.northeastern.ccs.im.view;

import edu.northeastern.ccs.im.controller.UserController;
import edu.northeastern.ccs.im.userGroup.IUserGroup;
import edu.northeastern.ccs.jpa.User;

import java.util.List;

public class View {
    private UserController userController;

    public void addUser(User user) {
        userController.addIUserGroup(user);
    }

    public void showUsers(List<IUserGroup> iUserGroupList) {

    }
}
