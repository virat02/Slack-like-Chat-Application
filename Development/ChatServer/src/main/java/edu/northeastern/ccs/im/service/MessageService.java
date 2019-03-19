package edu.northeastern.ccs.im.service;

import edu.northeastern.ccs.im.service.jpa_service.MessageJPAService;
import edu.northeastern.ccs.im.user_group.Group;
import edu.northeastern.ccs.im.user_group.Message;
import edu.northeastern.ccs.im.user_group.User;

import java.util.List;

/**
 * Class for all the Message service methods
 */
public class MessageService implements IService{

    private MessageJPAService messageJPAService = new MessageJPAService();
    private UserService userService = new UserService();
    private GroupService groupService = new GroupService();

    /**
     * Set a message JPA Service
     * @param messageJPAService
     */
    public void setMessageJPAService(MessageJPAService messageJPAService) {
        if(messageJPAService == null) {
            this.messageJPAService = new MessageJPAService();
        }
        else {
            this.messageJPAService = messageJPAService;
        }
        this.messageJPAService.setEntityManager(null);
    }

    /**
     * set the userService and groupService
     * @param userService
     * @param groupService
     */
    public void setJPAServices(UserService userService, GroupService groupService) {
        this.userService = userService;
        this.groupService = groupService;
    }

    /**
     * Helper method to send a message
     * @param message the message object generated from the client input
     * @return
     */
    public boolean createMessage(Message message) {
        messageJPAService.setEntityManager(null);
        int id = messageJPAService.createMessage(message);
        return id!= -1;
    }

    /**
     * Generates a message object from the client input and sends the message
     * @param messageBody
     * @param userName
     * @param groupCode
     * @return
     */
    public Boolean createMessage(String messageBody, String userName, String groupCode) {

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
        messageJPAService.setEntityManager(null);
        return messageJPAService.getMessage(id);
    }

    /**
     * Updates the message
     * @param msg
     */
    public boolean updateMessage(Message msg) {
        messageJPAService.setEntityManager(null);
        return messageJPAService.updateMessage(msg);
    }

    /**
     * Deletes a message
     */
    public Boolean deleteMessage(Message msg) {

        msg.setDeleted(true);
        return updateMessage(msg);
    }

    /**
     * Returns the recent-most 15 messages given a group unique key
     * @param groupUniqueKey
     */
    public List<Message> getTop15Messages(String groupUniqueKey) {
        messageJPAService.setEntityManager(null);
        return messageJPAService.getTop15Messages(groupUniqueKey);
    }
}
