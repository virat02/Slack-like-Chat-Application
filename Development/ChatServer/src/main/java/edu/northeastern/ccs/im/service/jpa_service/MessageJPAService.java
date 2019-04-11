package edu.northeastern.ccs.im.service.jpa_service;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.customexceptions.GroupNotFoundException;
import edu.northeastern.ccs.im.customexceptions.MessageNotFoundException;
import edu.northeastern.ccs.im.service.EntityManagerUtil;
import edu.northeastern.ccs.im.service.GroupService;
import edu.northeastern.ccs.im.user_group.Group;
import edu.northeastern.ccs.im.user_group.Message;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * All the JPA services for a Message
 */
public class MessageJPAService {

    private static final Logger LOGGER = Logger.getLogger(MessageJPAService.class.getName());
    private EntityManagerUtil entityManagerUtil = new EntityManagerUtil();
    private GroupService groupService = new GroupService();

    /**
     * Set a group service
     *
     * @param groupService
     */
    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }

    /**
     * Set the entityManagerUtil
     * @param entityManagerUtil
     */
    public void setEntityManagerUtil(EntityManagerUtil entityManagerUtil) {
        this.entityManagerUtil = entityManagerUtil;
    }

    /**
     * Updates a message with the given message object
     *
     * @param message
     */
    public boolean updateMessage(Message message) throws MessageNotFoundException {

        //Begin Transaction
        EntityManager em = entityManagerUtil.getEntityManager();
        em.getTransaction().begin();

        Message thisMessage = em.find(Message.class, message.getId());
        if (thisMessage == null) {
            LOGGER.info("Can't find Message for ID:" + message.getId());
            throw new MessageNotFoundException("Can't find Message for ID:" + message.getId());
        }

        thisMessage.setSender(message.getSender());
        thisMessage.setReceiver(message.getReceiver());
        thisMessage.setDeleted(message.isDeleted());
        thisMessage.setMessage(message.getMessage());
        thisMessage.setTimestamp(message.getTimestamp());
        thisMessage.setExpiration(message.getExpiration());

        //End Transaction
        em.getTransaction().commit();
        em.close();

        if (thisMessage.toString().equals(message.toString())) {
            LOGGER.info("Updated message with message id : " + message.getId());
            return true;
        } else {
            LOGGER.info("Could not update message with message id : " + message.getId());
            return false;
        }

    }

    /**
     * Gets the recent-most 15 messages given a group unique key
     *
     * @param groupUniqueKey
     */
    public List<Message> getTop15Messages(String groupUniqueKey) throws GroupNotFoundException {

        //Get the group based on the group unique key
        Group g = groupService.searchUsingCode(groupUniqueKey);
        String queryString = "SELECT m FROM Message m WHERE m.receiver.id = " + g.getId() + " ORDER BY m.timestamp DESC";

        //Begin Transaction
        EntityManager em = entityManagerUtil.getEntityManager();
        em.getTransaction().begin();

        TypedQuery<Message> query = em.createQuery(queryString, Message.class);
        List<Message> msgList = query.setMaxResults(15).getResultList();
        msgList.sort(Comparator.comparing(Message::getId));
        ChatLogger.info("Top 15 messages for a group: " + groupUniqueKey + " retrieved!");
        return msgList;
    }

    /***
     * Returns a list of messages after the timestamp.
     *
     * Considering the usage of this function, it doesn't check for any exception handling as it safely assumes that the method
     * is called for an group which does exists.
     * @param loggedOut -> The time at which the user has exited the chatroom. Any messages sent through this
     *                  group after the timestamp will be delivered to the user.
     * @param groupId -> The id of the group.
     * @return -> A list of Messages.
     */
    public List<Message> getMessagesAfterThisTimestamp(Date loggedOut, int groupId) {
        EntityManager em = entityManagerUtil.getEntityManager();
        em.getTransaction().begin();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Message> criteriaQuery = criteriaBuilder.createQuery(Message.class);
        Root<Message> root = criteriaQuery.from(Message.class);
        Predicate predicate = criteriaBuilder.and(criteriaBuilder.equal(root.get("receiver").get("id"), groupId),
                criteriaBuilder.greaterThanOrEqualTo(root.get("timestamp"), loggedOut));
        criteriaQuery.where(predicate);
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get("timestamp")));
        TypedQuery<Message> typedQuery = em.createQuery(criteriaQuery);
        List<Message> messages = typedQuery.getResultList();
        em.close();
        return messages;
    }

    /**
     * Gets all the messages given a group unique key.
     *
     * @param groupUniqueKey the group id for which the messages must be fetched
     * @return list of messages that are fetched
     * @throws GroupNotFoundException thrown when the given group does not exist
     */
    public List<Message> getAllMessages(String groupUniqueKey) throws GroupNotFoundException {

        //Get the group based on the group unique key
        Group g = groupService.searchUsingCode(groupUniqueKey);

        String queryString = "SELECT m FROM Message m WHERE m.receiver.id = " + g.getId() + " ORDER BY m.timestamp DESC";
        //Begin Transaction
        EntityManager em = entityManagerUtil.getEntityManager();
        em.getTransaction().begin();
        TypedQuery<Message> query = em.createQuery(queryString, Message.class);
        List<Message> msgList = query.getResultList();
        msgList.sort(Comparator.comparing(Message::getId));
        ChatLogger.info("All messages for a group: " + groupUniqueKey + " retrieved!");
        return msgList;
    }
}