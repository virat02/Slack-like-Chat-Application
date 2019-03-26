package edu.northeastern.ccs.im.controller;

import edu.northeastern.ccs.im.communication.CommunicationUtils;
import edu.northeastern.ccs.im.communication.NetworkResponse;
import edu.northeastern.ccs.im.communication.NetworkResponseImpl;
import edu.northeastern.ccs.im.communication.PayloadImpl;
import edu.northeastern.ccs.im.customexceptions.ProfileNotDeletedException;
import edu.northeastern.ccs.im.customexceptions.ProfileNotFoundException;
import edu.northeastern.ccs.im.customexceptions.ProfileNotPersistedException;
import edu.northeastern.ccs.im.service.ProfileService;
import edu.northeastern.ccs.im.user_group.Profile;

/**
 * Class for the profile controller
 */
public class ProfileController implements IController<Profile> {

    private ProfileService profileService = new ProfileService();

    private static final String PROFILE_NOT_PERSISTED_JSON = "{\"exceptionMessage\" : \"Jpa could not persist the profile!\"}";
    private static final String PROFILE_NOT_FOUND_JSON = "{\"exceptionMessage\" : \"Jpa could not find the profile!\"}";
    private static final String PROFILE_NOT_DELETED_JSON = "{\"exceptionMessage\" : \"Jpa could not delete the profile!\"}";

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
