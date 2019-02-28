package edu.northeastern.ccs.im.service;

import edu.northeastern.ccs.im.userGroup.IUser;
import edu.northeastern.ccs.im.userGroup.IUserGroup;
import edu.northeastern.ccs.jpa.Group;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private List<IUserGroup> userList;

    public UserService() {
        userList = new ArrayList<IUserGroup>();
    }
    public List<IUserGroup> addUser(IUserGroup user) {
        Group group = new Group();
        group.addUser(user);
        return userList;
    }
}
