package edu.northeastern.ccs.im.controller;

import edu.northeastern.ccs.im.communication.CommunicationUtils;
import edu.northeastern.ccs.im.communication.NetworkResponse;
import edu.northeastern.ccs.im.communication.NetworkResponseImpl;
import edu.northeastern.ccs.im.communication.PayloadImpl;
import edu.northeastern.ccs.im.customexceptions.*;
import edu.northeastern.ccs.im.service.ProfileService;
import edu.northeastern.ccs.im.user_group.Profile;

/**
 * Class for the profile controller
 */
public class ProfileController implements IController<Profile> {

    private ProfileService profileService = new ProfileService();

    private static final String PROFILE_NOT_PERSISTED_JSON = "{\"message\" : \"Sorry, could not create your profile!\"}";
    private static final String PROFILE_NOT_FOUND_JSON = "{\"message\" : \"The profile you are trying to find does not exist!\"}";
    private static final String PROFILE_NOT_DELETED_JSON = "{\"message\" : \"Sorry, could not delete the profile!\"}";
    private static final String INVALID_EMAIL_JSON = "{\"message\" : \"The email id you entered is invalid. Please try again! (Eg. youremailaddress@xyz.com)\"}";
    private static final String EMAIL_ALREADY_IN_USE_JSON = "{\"message\" : \"The email id is already in use. Please try again with different email id!\"}";
    private static final String INVALID_IMAGEURL_JSON = "{\"message\" : \"The imageURL you entered is invalid. Please try again! (Eg. http://* or https://* )\"}";

    /**
     * Sets the user service for the controller.
     * @param profileService the user service the controller will be using to load on the payload.
     */
    public void setProfileService(ProfileService profileService) {
        this.profileService = profileService;
    }

    /**
     * Controller to add a Profile
     * @param pf
     * @return a NetworkResponse
     */
    public NetworkResponse addEntity(Profile pf) {
        try {
            return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                    new PayloadImpl(CommunicationUtils.toJson(profileService.createProfile(pf))));
        }
        catch (ProfileNotPersistedException e) {
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(PROFILE_NOT_PERSISTED_JSON));
        }
        catch (InvalidEmailException e){
            if(e.getMessage()=="The Email id is already in use"){
                return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                        new PayloadImpl(EMAIL_ALREADY_IN_USE_JSON));
            }
            else {
                return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                        new PayloadImpl(INVALID_EMAIL_JSON));
            }
        }
        catch (InvalidImageURLException e){
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(INVALID_IMAGEURL_JSON));
        }
    }

    /**
     * Controller to get a profile
     * @param id
     * @return a NetworkResponse
     */
    public NetworkResponse getEntity(int id) {
        try {
            return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                    new PayloadImpl(CommunicationUtils.toJson(profileService.get(id))));
        }
        catch (ProfileNotFoundException e) {
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(PROFILE_NOT_FOUND_JSON));
        }
    }

    /**
     * Controller to update a profile
     * @param pf
     * @return a NetworkResponse
     */
    public NetworkResponse updateEntity(Profile pf) {
        try {
            return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                    new PayloadImpl(CommunicationUtils.toJson(profileService.updateProfile(pf))));
        }
        catch (ProfileNotFoundException e) {
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(PROFILE_NOT_FOUND_JSON));
        }
    }

    /**
     * Controller to delete a profile
     * @param pf
     * @return a NetworkResponse
     */
    public NetworkResponse deleteEntity(Profile pf) {
        try {
            return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                    new PayloadImpl(CommunicationUtils.toJson(profileService.deleteProfile(pf))));
        }
        catch (ProfileNotDeletedException e) {
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(PROFILE_NOT_DELETED_JSON));
        }
    }

    /**
     * Controller to search for a profile
     * @param id
     * @return a NetworkResponse
     */
    public NetworkResponse searchEntity(String id) {
        return null;
    }
}
