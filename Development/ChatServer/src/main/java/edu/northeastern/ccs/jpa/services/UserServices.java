package edu.northeastern.ccs.jpa.services;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import edu.northeastern.ccs.jpa.Group;
import edu.northeastern.ccs.jpa.Profile;
import edu.northeastern.ccs.jpa.User;

public class UserServices {
	EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "TestPersistence" );
    public void createUser(int id, Profile profile) {
    	EntityManager entitymanager = emfactory.createEntityManager();
    	entitymanager.getTransaction().begin();
    	User user = new User();
    	user.setId(id);
    	user.setProfile(profile);
    	entitymanager.persist(user);
        entitymanager.getTransaction().commit();
        entitymanager.close();
    }
    
    public User getUser(int id) {
    	EntityManager entitymanager = emfactory.createEntityManager( );
        entitymanager.getTransaction().begin();  
        User user = entitymanager.find(User.class, id);
        entitymanager.getTransaction().commit();
        entitymanager.close();
        return user;
    }
    
    public void updateUserGroup(int id,Group group) {
        EntityManager entitymanager = emfactory.createEntityManager( );
        entitymanager.getTransaction().begin();  
        User user = entitymanager.find(User.class, id);
        user.addGroup(group);
        entitymanager.getTransaction().commit();
        entitymanager.close();
    }
    
    public void deleteUser(int id) {
    	 EntityManager entitymanager = emfactory.createEntityManager( );
         entitymanager.getTransaction().begin();
         User user = entitymanager.find(User.class, id);
         entitymanager.remove(user);
         entitymanager.getTransaction().commit();
         entitymanager.close();
    }
}
