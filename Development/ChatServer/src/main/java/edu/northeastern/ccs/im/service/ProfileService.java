package edu.northeastern.ccs.im.service;

import edu.northeastern.ccs.im.service.jpa_service.ProfileJPAService;
import edu.northeastern.ccs.im.user_group.Profile;

/**
 * Class for all the profile service methods
 */
public class ProfileService {

    private ProfileJPAService profileJPAService;

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
     * Creates a profile if the respective inputs are valid
     */
    public Boolean createProfile(Profile pf) {
        return profileJPAService.createProfile(pf) != -1;
    }

    /**
     * Get the profile
     * @param id
     * @return
     */
    public Profile get(int id) {
        return profileJPAService.getProfile(id);
    }

    /**
     * Updates an existing profile if the respective inputs are valid
     */
    public Boolean updateProfile(Profile pf) {
        return profileJPAService.updateProfile(pf);
    }

    /**
     * Deletes a profile
     */
    public Boolean deleteProfile(Profile pf) {
        return profileJPAService.deleteProfile(pf) != -1;
    }
}
