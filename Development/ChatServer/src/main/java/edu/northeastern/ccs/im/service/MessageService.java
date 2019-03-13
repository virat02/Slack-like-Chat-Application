package edu.northeastern.ccs.im.service;

import edu.northeastern.ccs.im.service.JPAService.MessageJPAService;
import edu.northeastern.ccs.im.userGroup.Group;
import edu.northeastern.ccs.im.userGroup.Message;
import edu.northeastern.ccs.im.userGroup.User;


public class MessageService implements IService{

    private MessageJPAService messageJPAService;
    private UserService userService;
    private GroupService groupService;
    public MessageService() {
        messageJPAService = new MessageJPAService();
        userService = new UserService();
        groupService = new GroupService();
    }

    /**
     * Create and send a message
     * @param message
     * @return
     */
    public Message createMessage(Message message) {
        messageJPAService.createMessage(message);
        return messageJPAService.getMessage(message.getId());

    }

    public Message createMessage(String messageBody, String userName, String groupCode) {
        Message message = new Message();
        User user = userService.search(userName);
        Group group = groupService.searchUsingCode(groupCode);
        message.setMessage(messageBody);
        message.setSender(user);
        message.setReceiver(group);
        return createMessage(message);
    }

    /**
     * Get the message
     * @param id
     * @return
     */
    public Message get(int id) {
        return messageJPAService.getMessage(id);
    }

    public Message updateMessage(Message msg) {
        messageJPAService.updateMessage(msg);
        return messageJPAService.getMessage(msg.getId());

    }

    /**
     * Deletes a message
     */
    public Message deleteMessage(Message msg) {
        return updateMessage(msg);
    }
}
