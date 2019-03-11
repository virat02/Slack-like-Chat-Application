package edu.northeastern.ccs.im.controller;

import edu.northeastern.ccs.im.service.MessageService;
import edu.northeastern.ccs.im.userGroup.Message;

public class MessageController implements IController<Message>{

    private MessageService messageService;

    public Message addEntity(Message message) {
        try {
            return messageService.createMessage(message);
        }
        catch(IllegalArgumentException e) {
            throw new IllegalArgumentException("Cannot create a new group");
        }

    }

    public Message getEntity(int id) {

        return messageService.get(id);
    }

    public Message updateEntity(Message message) {
        return messageService.updateMessage(message);

    }

    public Message deleteEntity(Message message) {
        return messageService.deleteMessage(message);

    }

    public Message searchEntity(String msg) {
        return null;
    }


}
