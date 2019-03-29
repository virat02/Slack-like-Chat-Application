package edu.northeastern.ccs.im.service;

import edu.northeastern.ccs.im.customexceptions.*;
import edu.northeastern.ccs.im.service.jpa_service.ProfileJPAService;
import edu.northeastern.ccs.im.user_group.Profile;

import java.util.regex.Pattern;

/**
 * Class for all the profile service methods
 */
public class ProfileService {

    private ProfileJPAService profileJPAService;

    public ProfileService(){
        profileJPAService = new ProfileJPAService();
    }

    /**
     * Set a profile JPA Service
     * @param profileJPAService
     */
    public void setProfileJPAService(ProfileJPAService profileJPAService) {
        if(profileJPAService == null) {
            this.profileJPAService = new ProfileJPAService();
        }
        else {
            this.profileJPAService = profileJPAService;
        }
        this.profileJPAService.setEntityManager(null);
    }

    /**
     * Returns true iff the email id is valid
     * @param emailId
     */
    public boolean isValidEmail(String emailId){
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (emailId == null)
            return false;
        return pat.matcher(emailId).matches();
    }

    public boolean isEmailAlreadyInUse(String emailId) throws ProfileNotPersistedException{
        profileJPAService.setEntityManager(null);
        return profileJPAService.checkIfEmailExists(emailId);
    }

    /**
     * Creates a profile if the respective inputs are valid
     */
    public Profile createProfile(Profile pf)
            throws ProfileNotPersistedException, InvalidEmailException {

        //Check for validity of email
        if(!isValidEmail(pf.getEmail())){
         throw new InvalidEmailException("Invalid email id entered!");
        }
        if(isEmailAlreadyInUse(pf.getEmail())) {
            throw new InvalidEmailException("The Email id is already in use");
        }

        profileJPAService.setEntityManager(null);
        return profileJPAService.createProfile(pf);
    }

    /**
     * Get the profile
     * @param id
     * @return
     */
    public Profile get(int id) throws ProfileNotFoundException {
        profileJPAService.setEntityManager(null);
        return profileJPAService.getProfile(id);
    }

    /**
     * Updates an existing profile if the respective inputs are valid
     */
    public Boolean updateProfile(Profile pf) throws ProfileNotFoundException {
        profileJPAService.setEntityManager(null);
        return profileJPAService.updateProfile(pf);
    }

    /**
     * Deletes a profile
     */
    public Boolean deleteProfile(Profile pf) throws ProfileNotDeletedException {
        profileJPAService.setEntityManager(null);
        return profileJPAService.deleteProfile(pf) != -1;
    }
}
