package edu.northeastern.ccs.jpa.services;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import edu.northeastern.ccs.jpa.Message;
import edu.northeastern.ccs.jpa.User;

public class MessageServices {
	EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "TestPersistence" );
    public void createMessage(int id,String message) {
    	EntityManager entitymanager = emfactory.createEntityManager();
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
    
    public Message getMessage(int id) {
        EntityManager entitymanager = emfactory.createEntityManager( );   
        entitymanager.getTransaction().begin();
        Message msg = entitymanager.find(Message.class, id);
        entitymanager.getTransaction().commit();
        entitymanager.close();
        return msg;
    }
    
    public void updateMessageSender(int id,User sender) {
        EntityManager entitymanager = emfactory.createEntityManager( );
        entitymanager.getTransaction().begin();
        // write find query using name
        Message msg = entitymanager.find(Message.class, id);
        msg.setSender(sender);
        entitymanager.getTransaction().commit();
        entitymanager.close();
    }
    
    
}
