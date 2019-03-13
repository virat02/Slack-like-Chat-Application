package edu.northeastern.ccs.im.service.JPAService;

import edu.northeastern.ccs.im.userGroup.Message;

import javax.persistence.*;
import java.util.List;
import java.util.logging.Logger;

/**
 * All the JPA services for a Message
 */
public class MessageJPAService {

    private static final Logger LOGGER = Logger.getLogger(MessageJPAService.class.getName());

    /**
     * Begin the entity manager
     * @return
     */
    private EntityManager beginTransaction() {
        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("PrattlePersistance");
        EntityManager entityManager = emFactory.createEntityManager();
        entityManager.getTransaction().begin();
        return entityManager;
    }

    /**
     * Close the entity manager
     * @param entityManager
     */
    private void endTransaction(EntityManager entityManager) {
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    /**
     * Persists a message with the given message object
     * @param message
     * @return true iff the message could be persisted
     */
    public boolean createMessage(Message message) {
        try {
            EntityManager entityManager = beginTransaction();
            entityManager.persist(message);
            endTransaction(entityManager);
            return true;
        }
        catch (Exception e){
            LOGGER.info("JPA Could not persist the message!");
            return false;
        }
    }

    /**
     * Updates a message with the given message object
     * @param message
     */
    public boolean updateMessage(Message message) {
        try {
            EntityManager entityManager = beginTransaction();
            Message thisMessage = entityManager.find(Message.class, message.getId());

            thisMessage.setSender(message.getSender());
            thisMessage.setReceiver(message.getReceiver());
            thisMessage.setDeleted(message.isDeleted());
            thisMessage.setMessage(message.getMessage());
            thisMessage.setTimestamp(message.getTimestamp());
            thisMessage.setExpiration(message.getExpiration());

            endTransaction(entityManager);

            return true;
        }
        catch (Exception e) {
            LOGGER.info("Can't find Message for ID:"+ message.getId());
            return false;
        }

    }

    /**
     * Gets a message given it's id
     * @param id
     * @return
     */
    public Message getMessage(int id) {
        try {
            String queryString = "SELECT m FROM message m WHERE m.id =" + id;
            EntityManager entityManager = beginTransaction();
            TypedQuery<Message> query = entityManager.createQuery(queryString, Message.class);
            return query.getSingleResult();
        }
        catch(Exception e) {
            LOGGER.info("Could not get any message with id: "+id);
            throw new NullPointerException("No message found with id: "+id);
        }
    }

    /**
     * Gets the recent-most 15 messages given a group unique key
     * @param groupUniqueKey
     */
    public List<Message> getTop15Messages(String groupUniqueKey) {
        try {
            String queryString = "SELECT m FROM message m INNER JOIN groupcomposite g WHERE m.receiverid = g.id ORDER BY m.timestamp DESC";
            EntityManager entityManager = beginTransaction();
            TypedQuery<Message> query = entityManager.createQuery(queryString, Message.class);
            return query.setMaxResults(15).getResultList();
        }
        catch(Exception e) {
            LOGGER.info("Could not get messages for group unique key: "+groupUniqueKey);
            throw new NullPointerException("No message found for group unique key: "+groupUniqueKey);
        }

    }
}
