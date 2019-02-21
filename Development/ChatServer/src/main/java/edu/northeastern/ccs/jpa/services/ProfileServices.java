package edu.northeastern.ccs.jpa.services;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import edu.northeastern.ccs.jpa.Profile;

public class ProfileServices {

	EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "Eclipselink_JPA" );
    public void createProfile(int id,String name, String password,String email, String imageUrl) {
    	EntityManager entitymanager = emfactory.createEntityManager();
    	entitymanager.getTransaction().begin();
    	Profile profile = new Profile(id,name, email, password, imageUrl);
    	entitymanager.persist(profile);
        entitymanager.getTransaction().commit();
        entitymanager.close();
    }
    
    public void updateProfileName(int id,String name) {
        EntityManager entitymanager = emfactory.createEntityManager( );
        entitymanager.getTransaction().begin();
        // write find query using name
        Profile profile = entitymanager.find(Profile.class, id);
        profile.setName(name);
        entitymanager.getTransaction().commit();
        entitymanager.close();
    }
    
    public void deleteProfile(int id) {
    	 EntityManager entitymanager = emfactory.createEntityManager( );
         entitymanager.getTransaction().begin();
         Profile profile = entitymanager.find(Profile.class, id);
         entitymanager.remove(profile);
         entitymanager.getTransaction().commit();
         entitymanager.close();
    }
}
