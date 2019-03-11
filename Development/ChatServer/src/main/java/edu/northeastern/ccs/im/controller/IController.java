package edu.northeastern.ccs.im.controller;

import edu.northeastern.ccs.im.userGroup.IUser;
import edu.northeastern.ccs.im.userGroup.IUserGroup;

import java.util.List;


public interface IController<T>{

    void addEntity(T entity);

    IUser searchUser(String username);

    void updateEntity(T entity);

    void deleteEntity(T entity);

}
