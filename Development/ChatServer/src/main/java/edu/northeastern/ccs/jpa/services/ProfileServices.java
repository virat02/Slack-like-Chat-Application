package edu.northeastern.ccs.jpa.services;

import javax.persistence.EntityManager;

import edu.northeastern.ccs.jpa.Profile;

/**
 * The Class ProfileServices.
 */
public class ProfileServices {

    /**
     * Creates the profile.
     *
     * @param entitymanager the entitymanager
     * @param id the id
     * @param name the name
     * @param password the password
     * @param email the email
     * @param imageUrl the image url
     */
    public void createProfile(EntityManager entitymanager,int id, String name, String password, String email, String imageUrl) {
        entitymanager.getTransaction().begin();
        Profile profile = new Profile(id, name, email, password, imageUrl);
        entitymanager.persist(profile);
        entitymanager.getTransaction().commit();
        entitymanager.close();
    }

    /**
     * Gets the profile.
     *
     * @param entitymanager the entitymanager
     * @param id the id
     * @return the profile
     */
    public Profile getProfile(EntityManager entitymanager,int id) {
        entitymanager.getTransaction().begin();
        Profile profile = entitymanager.find(Profile.class, id);
        entitymanager.getTransaction().commit();
        entitymanager.close();
        return profile;
    }

    /**
     * Update profile name.
     *
     * @param entitymanager the entitymanager
     * @param id the id
     * @param name the name
     */
    public void updateProfileName(EntityManager entitymanager,int id, String name) {
        entitymanager.getTransaction().begin();
        // write find query using name
        Profile profile = entitymanager.find(Profile.class, id);
        profile.setName(name);
        entitymanager.getTransaction().commit();
        entitymanager.close();
    }

    /**
     * Delete profile.
     *
     * @param entitymanager the entitymanager
     * @param id the id
     */
    public void deleteProfile(EntityManager entitymanager,int id) {
        entitymanager.getTransaction().begin();
        Profile profile = entitymanager.find(Profile.class, id);
        entitymanager.remove(profile);
        entitymanager.getTransaction().commit();
        entitymanager.close();
    }

}
