package edu.northeastern.ccs.im.service.jpa_service;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.customexceptions.ProfileNotFoundException;
import edu.northeastern.ccs.im.user_group.Profile;

import javax.persistence.*;
import java.util.logging.Logger;

/**
 * All the jpa service methods for Profile
 */
public class ProfileJPAService {

    private static final Logger LOGGER = Logger.getLogger(ProfileJPAService.class.getName());

    private EntityManager entityManager;

    /**
     * Set an entity manager
     * @param entityManager
     */
    public void setEntityManager(EntityManager entityManager) {
        if(entityManager == null) {
            EntityManagerFactory emFactory;
            emFactory = Persistence.createEntityManagerFactory("PrattlePersistance");
            this.entityManager = emFactory.createEntityManager();
        }
        else{
            this.entityManager = entityManager;
        }
    }

    /**
     * Begin the entity manager
     *
     * @return
     */
    private void beginTransaction() {
        entityManager.getTransaction().begin();
    }

    /**
     * Creates a profile in the database
     * @param p
     */
    public Profile createProfile(Profile p) {
        beginTransaction();
        entityManager.persist(p);
        entityManager.flush();
        int profileId = p.getId();
        endTransaction();

        //If reached here, all went well and profile was persisted in DB
        ChatLogger.info("Created profile with profile id : "+profileId);
        return p;
    }

    /**
     * Deletes a profile in the database
     * @param p
     */
    public int deleteProfile(Profile p) {
        beginTransaction();
        entityManager.remove(p);
        endTransaction();

        //If reached here, all went well and profile was deleted from DB
        LOGGER.info("Deleted profile : "+p.getId());
        return p.getId();
    }

    /**
     * Updates a profile in the database
     * @param p
     */
    public boolean updateProfile(Profile p) throws ProfileNotFoundException {
        beginTransaction();
        Profile thisProfile = entityManager.find(Profile.class, p.getId());
        if (thisProfile == null) {
            LOGGER.info("Can't find Profile for this ID");
            throw new ProfileNotFoundException("Can't find Profile for ID " + p.getId());
        }

        thisProfile.setImageUrl(p.getImageUrl());
        thisProfile.setEmail(p.getEmail());
        endTransaction();

        if(thisProfile.toString().equals(p.toString())){
            LOGGER.info("Updated profile with profile id : "+p.getId());
            return true;
        }
        else {
            LOGGER.info("Could not update profile with profile id : "+p.getId());
            return false;
        }
    }

    /**
     * Gets a profile from the database
     * @param id
     * @return
     */
    public Profile getProfile(int id) throws ProfileNotFoundException {
        try {
            StringBuilder queryString = new StringBuilder("SELECT p FROM Profile p WHERE p.id = ");
            queryString.append(id);
            beginTransaction();
            TypedQuery<Profile> query = entityManager.createQuery(queryString.toString(), Profile.class);
            return query.getSingleResult();
        } catch (Exception e) {
            LOGGER.info("Could not get any profile with id : " + id);
            throw new ProfileNotFoundException("No profile found with id: " + id);
        }
    }

    /**
     * Returns true iff the email id already exists in the db
     * @param email
     * @return
     */
    public boolean ifEmailExists(String email) {
        try {
            StringBuilder queryString = new StringBuilder("SELECT p FROM Profile p WHERE p.email = ");
            queryString.append("'" + email + "'");
            beginTransaction();
            TypedQuery<Profile> query = entityManager.createQuery(queryString.toString(), Profile.class);
            Profile profile = query.getSingleResult();
            endTransaction();
            return profile.getId() > -1;
        }
        catch (Exception e){
            LOGGER.info(e.getMessage());
            return false;
        }
    }

    /**
     * Close the entity manager
     *
     */
    private void endTransaction() {
        entityManager.getTransaction().commit();
        entityManager.close();
    }

}
