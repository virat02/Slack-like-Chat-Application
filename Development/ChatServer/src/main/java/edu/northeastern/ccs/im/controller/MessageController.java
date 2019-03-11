package edu.northeastern.ccs.im.controller;

import edu.northeastern.ccs.im.service.MessageService;
import edu.northeastern.ccs.im.service.UserService;
import edu.northeastern.ccs.im.userGroup.IUser;
import edu.northeastern.ccs.im.userGroup.Message;
import edu.northeastern.ccs.im.view.View;

public class MessageController implements IController{

    private View view;
    private MessageService messageService;
    private UserService userService;


    public void addEntity(Object msg) {
        Message message;
        try {
            //message = messageService.createMessage(msg);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Can't add Message");
        }

        //view.showMessage(message);
    }
//
//    @Override
//    public Profile getProfile(Profile pf) {
//        return null;
//    }

    public void updateEntity(Object msg) {

    }

    public void deleteEntity(Object msg) {

    }

    public IUser searchUser(String username) {
        return userService.search(username);
    }
}
