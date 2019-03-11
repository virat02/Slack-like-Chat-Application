package edu.northeastern.ccs.im.service;

import edu.northeastern.ccs.im.service.JPAService.MessageJPAService;
import edu.northeastern.ccs.im.userGroup.IGroup;
import edu.northeastern.ccs.im.userGroup.IUserGroup;
import edu.northeastern.ccs.im.userGroup.Message;

import java.util.Date;

public class MessageService {

    private Message m;
    private MessageJPAService messageJPAService;
    public MessageService(Message msg) {
        this.m = msg;
    }

    /**
     * Create a message object
     * @param message
     * @param timestamp
     */
    public Message createMessage(String message, Date timestamp, int expiration) {
        if(message.length() != 0) {
            Message message1 = new Message(m.getId(), message, timestamp, expiration);
            //messageJPAService.createMessage(message1);
            return message1;
        }
        else {
            throw new NullPointerException();
        }

    }

    /**
     * Sends a message to a receiver
     * @param msg
     * @param sender
     * @param receiver
     * @param deleted
     * @return
     */
    public Message sendMessage(Message msg, IUserGroup sender, IGroup receiver, Boolean deleted) {

        this.m = msg;
        Message message2 = new Message(msg.getId(), msg.getMessage(), msg.getTimestamp(), msg.getExpiration(), sender, receiver, deleted);
        //messageJPAService.createMessage(message2);
        return message2;
    }

    /**
     * Updates the message
     * @param msg
     */
    public void updateMessage(Message msg) {
        m.setMessage(msg.getMessage());
        m.setExpiration(msg.getExpiration());
        m.setTimestamp(msg.getTimestamp());
        m.setSender(msg.getSender());
        m.setReceiver(msg.getReceiver());
        m.setDeleted(msg.isDeleted());

        //messageJPAService.updateMessage(msg);

    }

    /**
     * Deletes a message
     */
    public void deleteMessage() {

        m.setDeleted(true);
    }
}
