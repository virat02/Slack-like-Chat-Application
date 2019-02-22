package edu.northeastern.ccs.jpa.services;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import edu.northeastern.ccs.jpa.Profile;

public class ProfileServices {

    public void createProfile(EntityManager entitymanager,int id, String name, String password, String email, String imageUrl) {
        entitymanager.getTransaction().begin();
        Profile profile = new Profile(id, name, email, password, imageUrl);
        entitymanager.persist(profile);
        entitymanager.getTransaction().commit();
        entitymanager.close();
    }

    public Profile getProfile(EntityManager entitymanager,int id) {
        entitymanager.getTransaction().begin();
        Profile profile = entitymanager.find(Profile.class, id);
        entitymanager.getTransaction().commit();
        entitymanager.close();
        return profile;
    }

    public void updateProfileName(EntityManager entitymanager,int id, String name) {
        entitymanager.getTransaction().begin();
        // write find query using name
        Profile profile = entitymanager.find(Profile.class, id);
        profile.setName(name);
        entitymanager.getTransaction().commit();
        entitymanager.close();
    }

    public void deleteProfile(EntityManager entitymanager,int id) {
        entitymanager.getTransaction().begin();
        Profile profile = entitymanager.find(Profile.class, id);
        entitymanager.remove(profile);
        entitymanager.getTransaction().commit();
        entitymanager.close();
    }

}
