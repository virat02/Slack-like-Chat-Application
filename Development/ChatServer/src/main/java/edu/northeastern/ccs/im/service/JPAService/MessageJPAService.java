package edu.northeastern.ccs.im.service.JPAService;

import edu.northeastern.ccs.im.userGroup.Message;

import javax.persistence.*;

public class MessageJPAService {

    private EntityManager beginTransaction() {
        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("PrattlePersistance");
        EntityManager entityManager = emFactory.createEntityManager();
        entityManager.getTransaction().begin();
        return entityManager;
    }

    private void endTransaction(EntityManager entityManager) {
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public void createMessage(Message message) {
        EntityManager entityManager = beginTransaction();
        entityManager.persist(message);
        endTransaction(entityManager);

    }
//
//    public void deleteMessage(Message message) {
//        EntityManager entityManager = beginTransaction();
//        entityManager.remove(message);
//        endTransaction(entityManager);
//    }

    public void updateMessage(Message message) {
        EntityManager entityManager = beginTransaction();
        Message thisMessage = entityManager.find(Message.class, message.getId());
        if (thisMessage == null) {
            throw new EntityNotFoundException("Can't find Message for ID "
                    + message.getId());
        }

        thisMessage.setSender(message.getSender());
        thisMessage.setReceiver(message.getReceiver());
        thisMessage.setDeleted(message.isDeleted());
        thisMessage.setMessage(message.getMessage());
        thisMessage.setTimestamp(message.getTimestamp());
        thisMessage.setExpiration(message.getExpiration());

        endTransaction(entityManager);
    }

    public Message getMessage(int id) {
        String queryString = "SELECT m" + "FROM message m WHERE m.id =" + id;
        EntityManager entityManager = beginTransaction();
        Query query = entityManager.createQuery(queryString);
        return (Message) query.getSingleResult();
    }
}
