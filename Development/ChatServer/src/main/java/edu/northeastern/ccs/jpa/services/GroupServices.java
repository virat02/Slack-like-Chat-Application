package edu.northeastern.ccs.jpa.services;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import edu.northeastern.ccs.jpa.Group;
import edu.northeastern.ccs.jpa.User;

public class GroupServices {
	
    public void createGroup(EntityManager entitymanager,int id,String name) {
    	entitymanager.getTransaction().begin();
    	Group group = new Group(id, name);
    	entitymanager.persist(group);
        entitymanager.getTransaction().commit();
        entitymanager.close();
    }
    
    public Group getGroup(EntityManager entitymanager,int id) {
        entitymanager.getTransaction().begin();
        // write find query using name
        Group group = entitymanager.find(Group.class, id);
        entitymanager.getTransaction().commit();
        entitymanager.close();
        return group;
    }
    
    public void updateGroupName(EntityManager entitymanager,int id,String name) {
        entitymanager.getTransaction().begin();
        Group group = entitymanager.find(Group.class, id);
        group.setName(name);
        entitymanager.getTransaction().commit();
        entitymanager.close();
    }
    
    public void addUserToGroup(EntityManager entitymanager,int id, User user) {
        entitymanager.getTransaction().begin();
        Group group = entitymanager.find(Group.class, id);
        group.addUser(user);
        entitymanager.getTransaction().commit();
        entitymanager.close();
    }
    
    public void deleteGroup(EntityManager entitymanager,int id) {
         entitymanager.getTransaction().begin();
         Group group = entitymanager.find(Group.class, id);
         entitymanager.remove(group);
         entitymanager.getTransaction().commit();
         entitymanager.close();
    }
    
}
