package edu.northeastern.ccs.im.service.jpa_service;

import edu.northeastern.ccs.im.service.GroupService;
import edu.northeastern.ccs.im.user_group.Group;
import edu.northeastern.ccs.im.user_group.Message;

import javax.persistence.*;
import java.util.List;
import java.util.logging.Logger;

/**
 * All the JPA services for a Message
 */
public class MessageJPAService {

    private static final Logger LOGGER = Logger.getLogger(MessageJPAService.class.getName());

    private EntityManager entityManager;

    private GroupService groupService = new GroupService();

    /**
     * Set a group service
     * @param groupService
     */
    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }

    /**
     * Set an entity manager
     * @param entityManager
     */
    public void setEntityManager(EntityManager entityManager) {
        if(entityManager == null) {
            EntityManagerFactory emFactory;
            emFactory = Persistence.createEntityManagerFactory("PrattlePersistance");
            this.entityManager = emFactory.createEntityManager();
        }
        else{
            this.entityManager = entityManager;
        }
    }

    /**
     * Begin the entity manager
     *
     * @return
     */
    private void beginTransaction() {
        entityManager.getTransaction().begin();
    }

    /**
     * Close the entity manager
     *
     */
    private void endTransaction() {
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    /**
     * Persists a message with the given message object
     *
     * @param message
     * @return true iff the message could be persisted
     */
    public int createMessage(Message message) {
        try {
            beginTransaction();
            entityManager.persist(message);
            entityManager.flush();
            endTransaction();
            return message.getId();
        } catch (Exception e) {
            LOGGER.info("JPA Could not persist the message!");
            return -1;
        }
    }

    /**
     * Updates a message with the given message object
     *
     * @param message
     */
    public boolean updateMessage(Message message) {
        beginTransaction();
        Message thisMessage = entityManager.find(Message.class, message.getId());
        if(thisMessage == null){
            LOGGER.info("Can't find Message for ID:" + message.getId());
            throw new EntityNotFoundException("Can't find Message for ID:" + message.getId());
        }

        thisMessage.setSender(message.getSender());
        thisMessage.setReceiver(message.getReceiver());
        thisMessage.setDeleted(message.isDeleted());
        thisMessage.setMessage(message.getMessage());
        thisMessage.setTimestamp(message.getTimestamp());
        thisMessage.setExpiration(message.getExpiration());
        endTransaction();
        return true;
    }

    /**
     * Gets a message given it's id
     * @param id
     * @return
     */
    public Message getMessage(int id) {
        try {
            String queryString = "SELECT m FROM Message m WHERE m.id =" + id;
            beginTransaction();
            TypedQuery<Message> query = entityManager.createQuery(queryString, Message.class);
            return query.getSingleResult();
        } catch (Exception e) {
            LOGGER.info("Could not get any message with id: " + id);
            throw new NullPointerException("No message found with id: " + id);
        }
    }

    /**
     * Gets the recent-most 15 messages given a group unique key
     *
     * @param groupUniqueKey
     */
    public List<Message> getTop15Messages(String groupUniqueKey) {
        try {

            //Get the group based on the group unique key
            Group g = groupService.searchUsingCode(groupUniqueKey);
            if (g != null) {
                String queryString = "SELECT m FROM Message m, Group g WHERE m.receiver.id = "+g.getId()+" ORDER BY m.timestamp DESC";
                beginTransaction();
                TypedQuery<Message> query = entityManager.createQuery(queryString, Message.class);
                return query.setMaxResults(15).getResultList();
            }
            else {
                LOGGER.info("Group with this groupUniqueKey does not exist!");
                throw new NullPointerException("Group with group unique key: "+groupUniqueKey+" does not exist!");
            }

        } catch (Exception e) {
            LOGGER.info("Could not get messages for group unique key: " + groupUniqueKey);
            throw new NullPointerException("No message found for group unique key: " + groupUniqueKey);
        }
    }
}
