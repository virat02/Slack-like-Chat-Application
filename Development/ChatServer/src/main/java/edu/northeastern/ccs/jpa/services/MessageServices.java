package edu.northeastern.ccs.jpa.services;

import java.util.Date;

import javax.persistence.EntityManager;

import edu.northeastern.ccs.jpa.Message;

/**
 * The Class MessageServices.
 */
public class MessageServices {
    
    /**
     * Creates the message.
     *
     * @param entitymanager the entitymanager
     * @param id the id
     * @param message the message
     */
    public void createMessage(EntityManager entitymanager, int id,String message) {
    	entitymanager.getTransaction().begin();
    	Message msg = new Message();
    	msg.setId(id);
    	Date timestamp = new Date();
    	msg.setTimestamp(timestamp);
    	msg.setMessage(message);
    	entitymanager.persist(msg);
        entitymanager.getTransaction().commit();
        entitymanager.close();
    }
    
    /**
     * Gets the message.
     *
     * @param entitymanager the entitymanager
     * @param id the id
     * @return the message
     */
    public Message getMessage(EntityManager entitymanager,int id) {   
        entitymanager.getTransaction().begin();
        Message msg = entitymanager.find(Message.class, id);
        entitymanager.getTransaction().commit();
        entitymanager.close();
        return msg;
    }
    
    /**
     * Update message.
     *
     * @param entitymanager the entitymanager
     * @param id the id
     * @param newMessage the new message
     */
    public void updateMessage(EntityManager entitymanager,int id,String newMessage) {
        entitymanager.getTransaction().begin();
        Message msg = entitymanager.find(Message.class, id);
        msg.setMessage(newMessage);
        entitymanager.getTransaction().commit();
        entitymanager.close();
    }
    
    
    
}
