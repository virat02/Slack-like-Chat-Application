package edu.northeastern.ccs.im.service.jpa_service;

import edu.northeastern.ccs.im.user_group.Profile;

import javax.persistence.*;
import java.util.logging.Logger;

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
     * Close the entity manager
     *
     */
    private void endTransaction() {
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    /**
     * Creates a profile in the database
     * @param p
     */
    public int createProfile(Profile p) {
        try {
            beginTransaction();
            entityManager.persist(p);
            endTransaction();
            return p.getId();
        }
        catch (Exception e) {
            LOGGER.info("JPA Could not persist the profile!");
            return -1;
        }
    }

    /**
     * Deletes a profile in the database
     * @param p
     */
    public int deleteProfile(Profile p) {
        try {
            beginTransaction();
            entityManager.remove(p);
            endTransaction();
            return p.getId();
        }
        catch(Exception e){
            LOGGER.info("This profile could not be deleted!");
            return -1;
        }

    }

    /**
     * Updates a profile in the database
     * @param p
     */
    public boolean updateProfile(Profile p) {
        beginTransaction();
        Profile thisProfile = entityManager.find(Profile.class, p.getId());
        if (thisProfile == null) {
            LOGGER.info("Can't find Profile for this ID");
            throw new EntityNotFoundException("Can't find Profile for ID "
                    + p.getId());
        }

        thisProfile.setImageUrl(p.getImageUrl());
        thisProfile.setEmail(p.getEmail());
        endTransaction();
        return true;
    }

    /**
     * Gets a profile from the database
     * @param id
     * @return
     */
    public Profile getProfile(int id) {
        try {
            StringBuilder queryString = new StringBuilder("SELECT p FROM Profile p WHERE p.id = ");
            queryString.append(id);
            beginTransaction();
            TypedQuery<Profile> query = entityManager.createQuery(queryString.toString(), Profile.class);
            return query.getSingleResult();
        } catch (Exception e) {
            LOGGER.info("Could not get any profile with id: " + id);
            throw new NullPointerException("No profile found with id: " + id);
        }
    }

}
