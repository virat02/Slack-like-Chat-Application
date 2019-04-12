package edu.northeastern.ccs.im.service;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.customexceptions.*;
import edu.northeastern.ccs.im.service.jpa_service.AllJPAService;
import edu.northeastern.ccs.im.service.jpa_service.MessageJPAService;
import edu.northeastern.ccs.im.service.jpa_service.UserJPAService;
import edu.northeastern.ccs.im.user_group.Group;
import edu.northeastern.ccs.im.user_group.Message;
import edu.northeastern.ccs.im.user_group.User;
import edu.northeastern.ccs.im.user_group.UserChatRoomLogOffEvent;

import javax.persistence.NoResultException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Class for all the Message service methods
 */
public class MessageService implements IService{

    private static final Logger LOGGER = Logger.getLogger(MessageService.class.getName());
    private AllJPAService jpaService;

    private MessageJPAService messageJPAService;
    private UserService userService;
    private GroupService groupService;
    private UserJPAService userJPAService;

    public MessageService(){
        jpaService = new AllJPAService();
        messageJPAService = new MessageJPAService();
        userService = new UserService();
        groupService = new GroupService();
        userJPAService = new UserJPAService();
    }

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
    }

    /**
     * Set a profile JPA Service
     * @param jpaService
     */
    public void setAllJPAService(AllJPAService jpaService) {
        if(jpaService == null) {
            this.jpaService = new AllJPAService();
        }
        else {
            this.jpaService = jpaService;
        }
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
        try{
            return jpaService.createEntity(message);
        }
        catch (Exception e) {
            LOGGER.info("Could not persist message with message id: "+message.getId());
            LOGGER.info(e.getMessage());
            return false;
        }
    }

    /**
     * Generates a message object from the client input and sends the message
     * @param messageBody
     * @param userName
     * @param groupCode
     * @return
     */
    public Boolean createMessage(String messageBody, String userName, String groupCode)
            throws UserNotFoundException, GroupNotFoundException {

        Message message = new Message();
        User user = userService.search(userName);
        Group group = groupService.searchUsingCode(groupCode);
        message.setMessage(messageBody);
        message.setSender(user);
        message.setReceiver(group);
        message.setTimestamp(new Date());
        return createMessage(message);

    }

    /**
     * Get the message
     * @param id
     * @return
     */
    public Message get(int id) throws MessageNotFoundException {
        try {
            return (Message) jpaService.getEntity("Message", id);
        }
        catch (NoResultException e) {
            LOGGER.info("Could not find profile for profile id: "+id);
            throw new MessageNotFoundException("Could not find message for message id: "+id);
        }
    }

    /**
     * Updates the message
     * @param msg
     */
    public boolean updateMessage(Message msg) throws MessageNotFoundException {
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
        return messageJPAService.getTop15Messages(groupUniqueKey);
    }

    public List<Message> getUnreadMessages(String userName, String groupCode) throws UserNotFoundException, GroupNotFoundException {
        userJPAService.setEntityManager(null);
        User user = userJPAService.search(userName);
        Group group = groupService.searchUsingCode(groupCode);
        UserChatRoomLogOffEvent userChatRoomLogOffEvent = null;
        try {
            userChatRoomLogOffEvent = userJPAService.getLogOffEvent(user.getId(), group.getId());
        } catch (FirstTimeUserLoggedInException e) {
            ChatLogger.info(e.getMessage() + " Return the recent 15 messages");
            return getTop15Messages(groupCode);
        }

        return messageJPAService.getMessagesAfterThisTimestamp(userChatRoomLogOffEvent.getLoggedOutTime(), group.getId());
    }

    /**
     * Returns the all the messages given a group unique key.
     * @param groupUniqueKey the unique group for which the messages must be fetched
     * @return the list of fetched messages
     * @throws GroupNotFoundException thrown when the given group is not found
     */
    public List<Message> getAllMessages(String groupUniqueKey) throws GroupNotFoundException {
        return messageJPAService.getAllMessages(groupUniqueKey);
    }
}