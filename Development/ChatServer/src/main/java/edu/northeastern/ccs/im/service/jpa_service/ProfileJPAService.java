package edu.northeastern.ccs.im.service.jpa_service;

import edu.northeastern.ccs.im.customexceptions.ProfileNotFoundException;
import edu.northeastern.ccs.im.service.EntityManagerUtil;
import edu.northeastern.ccs.im.user_group.Profile;

import javax.persistence.*;
import java.util.logging.Logger;

/**
 * All the jpa service methods for Profile
 */
public class ProfileJPAService {

    private static final Logger LOGGER = Logger.getLogger(ProfileJPAService.class.getName());
    private EntityManagerUtil entityManagerUtil;
    private static final ProfileJPAService profileJpaServiceInstance = new ProfileJPAService();

    /**
     *Constructor for Profile jpa service
     */
    private ProfileJPAService(){
        this.entityManagerUtil = new EntityManagerUtil();
    }

    /**
     * @return Singleton for Profile JPA service
     */
    public static ProfileJPAService getInstance(){
        return profileJpaServiceInstance;
    }

    /**
     * Set the entityManagerUtil
     * @param entityManagerUtil EntityManagerUtil of this class
     */
    public void setEntityManagerUtil(EntityManagerUtil entityManagerUtil) {
        this.entityManagerUtil = entityManagerUtil;
    }

    /**
     * Updates a profile in the database
     * @param p Indicates a profile
     */
    public boolean updateProfile(Profile p) throws ProfileNotFoundException {

        //Begin Transaction
        EntityManager em = entityManagerUtil.getEntityManager();
        em.getTransaction().begin();

        Profile thisProfile = em.find(Profile.class, p.getId());
        if (thisProfile == null) {
            LOGGER.info("Can't find Profile for this ID");
            throw new ProfileNotFoundException("Can't find Profile for ID " + p.getId());
        }

        thisProfile.setImageUrl(p.getImageUrl());
        thisProfile.setEmail(p.getEmail());

        //End Transaction
        em.getTransaction().commit();
        em.close();

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
     * @param email Indicates the email id of the user
     * @return Returns true iff the email id already exists in the db
     */
    public boolean ifEmailExists(String email) {
        try {
            StringBuilder queryString = new StringBuilder("SELECT p FROM Profile p WHERE p.email = ");
            queryString.append("'" + email + "'");

            //Begin Transaction
            EntityManager em = entityManagerUtil.getEntityManager();
            em.getTransaction().begin();

            TypedQuery<Profile> query = em.createQuery(queryString.toString(), Profile.class);
            Profile profile = query.getSingleResult();

            //End Transaction
            em.getTransaction().commit();
            em.close();

            return profile.getId() > -1;
        }
        catch (Exception e){
            LOGGER.info(e.getMessage());
            return false;
        }
    }

}