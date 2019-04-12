package edu.northeastern.ccs.im.service;

import edu.northeastern.ccs.im.customexceptions.*;
import edu.northeastern.ccs.im.service.jpa_service.AllJPAService;
import edu.northeastern.ccs.im.service.jpa_service.ProfileJPAService;
import edu.northeastern.ccs.im.user_group.Profile;

import javax.persistence.NoResultException;
import java.net.URL;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Class for all the profile service methods
 */
public class ProfileService {

    private static final Logger LOGGER = Logger.getLogger(ProfileService.class.getName());
    private static final ProfileService profileServiceInstance = new ProfileService();

    private ProfileJPAService profileJPAService;
    private AllJPAService jpaService;

    /**
     * Constructor for ProfileService
     */
    private ProfileService(){
        profileJPAService = ProfileJPAService.getInstance();
        jpaService = AllJPAService.getInstance();
    }

    /**
     * Singleton instance for Profile Service
     */
    public static ProfileService getInstance(){
        return profileServiceInstance;
    }

    /**
     * Set a profile JPA Service
     * @param profileJPAService
     */
    public void setProfileJPAService(ProfileJPAService profileJPAService) {
        if(profileJPAService == null) {
            this.profileJPAService = ProfileJPAService.getInstance();
        }
        else {
            this.profileJPAService = profileJPAService;
        }
    }

    /**
     * Set a profile JPA Service
     * @param jpaService
     */
    public void setAllJPAService(AllJPAService jpaService) {
        if(jpaService == null) {
            this.jpaService = AllJPAService.getInstance();
        }
        else {
            this.jpaService = jpaService;
        }
    }

    /**
     * Returns true iff the email id is valid
     * @param emailId
     */
    public boolean isValidEmail(String emailId){

        if (emailId == null)
            return false;

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);

        return pat.matcher(emailId).matches();
    }

    /**
     * Returns true iff an email id is already in use by some other user in the DB
     * @param emailId
     * @return
     */
    public boolean isEmailAlreadyInUse(String emailId){
        return profileJPAService.ifEmailExists(emailId);
    }

    /**
     * Returns true iff an image URL is valid
     * @param imageURL
     * @return
     */
    public boolean isValidImageURL(String imageURL) {
        /* Try creating a valid URL */
        try {
            new URL(imageURL).toURI();
            return true;
        }

        // If there was an Exception
        // while creating URL object
        catch (Exception e) {
            return false;
        }
    }

    /**
     * Creates a profile if the respective inputs are valid
     */
    public boolean createProfile(Profile pf)
            throws InvalidEmailException, InvalidImageURLException {

        //Check for validity of email and Image URL
        if(!isValidEmail(pf.getEmail()) || !isValidImageURL(pf.getImageUrl())){

            //check for valid email
            if(!isValidEmail(pf.getEmail())) {
                throw new InvalidEmailException("Invalid email id entered!");
            }
            
            //check for valid image URL
            else if(!isValidImageURL(pf.getImageUrl())){
                throw new InvalidImageURLException("Invalid image URL entered!");
            }
        }
        if(isEmailAlreadyInUse(pf.getEmail())) {
            throw new InvalidEmailException("The Email id is already in use");
        }

        try {
            return jpaService.createEntity(pf);
        }
        catch (Exception e) {
            LOGGER.info("Could not persist profile with profile id: "+pf.getId());
            LOGGER.info(e.getMessage());
            return false;
        }

    }

    /**
     * Get the profile
     * @param id
     * @return
     */
    public Profile get(int id) throws ProfileNotFoundException {
        try {
            return (Profile) jpaService.getEntity("Profile", id);
        }
        catch (NoResultException e) {
            LOGGER.info("Could not find profile for profile id: "+id);
            throw new ProfileNotFoundException("Could not find profile for profile id: "+id);
        }
    }

    /**
     * Updates an existing profile if the respective inputs are valid
     */
    public Boolean updateProfile(Profile pf) throws ProfileNotFoundException {
        return profileJPAService.updateProfile(get(pf.getId()));
    }

    /**
     * Deletes a profile
     */
    public Boolean deleteProfile(Profile pf) throws ProfileNotFoundException {
        return jpaService.deleteEntity(get(pf.getId()));
    }
}
