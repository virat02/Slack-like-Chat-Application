package edu.northeastern.ccs.im.controller;

import edu.northeastern.ccs.im.userGroup.IUser;
import edu.northeastern.ccs.im.userGroup.IUserGroup;

import java.util.List;


public interface IController {


    void addIUserGroup(IUserGroup iUserGroup);

    IUserGroup getIUserGroup(IUserGroup iUserGroup);

    void updateIUserGroup(IUserGroup iUserGroup);

    void deleteIUserGroup(IUserGroup iUserGroup);

}
