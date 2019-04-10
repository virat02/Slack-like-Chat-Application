package edu.northeastern.ccs.im.service;

import edu.northeastern.ccs.im.customexceptions.*;
import edu.northeastern.ccs.im.service.jpa_service.MessageJPAService;
import edu.northeastern.ccs.im.user_group.Group;
import edu.northeastern.ccs.im.user_group.Message;
import edu.northeastern.ccs.im.user_group.User;

import java.util.List;
import java.util.logging.Logger;

/**
 * Class for all the Message service methods
 */
public class MessageService implements IService{

    private static final Logger LOGGER = Logger.getLogger(MessageService.class.getName());

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
    public boolean createMessage(Message message) throws MessageNotPersistedException {
        messageJPAService.setEntityManager(null);
        return messageJPAService.createMessage(message)!= -1;
    }

    /**
     * Generates a message object from the client input and sends the message
     * @param messageBody
     * @param userName
     * @param groupCode
     * @return
     */
    public Boolean createMessage(String messageBody, String userName, String groupCode)
            throws MessageNotPersistedException, UserNotFoundException, GroupNotFoundException {

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
    public Message get(int id) throws MessageNotFoundException{
        messageJPAService.setEntityManager(null);
        return messageJPAService.getMessage(id);
    }

    /**
     * Updates the message
     * @param msg
     */
    public boolean updateMessage(Message msg) throws MessageNotFoundException {
        messageJPAService.setEntityManager(null);
        return messageJPAService.updateMessage(msg);
    }

    /**
     * Deletes a message
     */
    public Boolean deleteMessage(Message msg) throws MessageNotFoundException {

        msg.setDeleted(true);
        if (updateMessage(msg)){
            LOGGER.info("Successfully deleted message: "+msg.getId());
        } else {
            LOGGER.info("Could not delete message: "+msg.getId());
        }
        return updateMessage(msg);
    }

    /**
     * Returns the recent-most 15 messages given a group unique key
     * @param groupUniqueKey
     */
    public List<Message> getTop15Messages(String groupUniqueKey) throws GroupNotFoundException{
        messageJPAService.setEntityManager(null);
        return messageJPAService.getTop15Messages(groupUniqueKey);
    }

    /**
     * Returns the all the messages given a group unique key.
     * @param groupUniqueKey the unique group for which the messages must be fetched
     * @return the list of fetched messages
     * @throws GroupNotFoundException thrown when the given group is not found
     */
    public List<Message> getAllMessages(String groupUniqueKey) throws GroupNotFoundException {
        messageJPAService.setEntityManager(null);
        return messageJPAService.getAllMessages(groupUniqueKey);
    }
}
