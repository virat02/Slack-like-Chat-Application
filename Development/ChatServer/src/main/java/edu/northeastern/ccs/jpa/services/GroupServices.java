package edu.northeastern.ccs.jpa.services;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import edu.northeastern.ccs.jpa.Group;
import edu.northeastern.ccs.jpa.User;

public class GroupServices {
	EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "TestPersistence" );
    public void createGroup(int id,String name) {
    	EntityManager entitymanager = emfactory.createEntityManager();
    	entitymanager.getTransaction().begin();
    	Group group = new Group(id, name);
    	entitymanager.persist(group);
        entitymanager.getTransaction().commit();
        entitymanager.close();
    }
    
    public Group getGroup(int id) {
        EntityManager entitymanager = emfactory.createEntityManager( );
        entitymanager.getTransaction().begin();
        // write find query using name
        Group group = entitymanager.find(Group.class, id);
        entitymanager.getTransaction().commit();
        entitymanager.close();
        return group;
    }
    
    public void updateGroupName(int id,String name) {
        EntityManager entitymanager = emfactory.createEntityManager( );
        entitymanager.getTransaction().begin();
        Group group = entitymanager.find(Group.class, id);
        group.setName(name);
        entitymanager.getTransaction().commit();
        entitymanager.close();
    }
    
    public void addUserToGroup(int id, User user) {
        EntityManager entitymanager = emfactory.createEntityManager( );
        entitymanager.getTransaction().begin();
        Group group = entitymanager.find(Group.class, id);
        group.addUser(user);
        entitymanager.getTransaction().commit();
        entitymanager.close();
    }
    
    public void deleteGroup(int id) {
    	 EntityManager entitymanager = emfactory.createEntityManager( );
         entitymanager.getTransaction().begin();
         Group group = entitymanager.find(Group.class, id);
         entitymanager.remove(group);
         entitymanager.getTransaction().commit();
         entitymanager.close();
    }
    
}
