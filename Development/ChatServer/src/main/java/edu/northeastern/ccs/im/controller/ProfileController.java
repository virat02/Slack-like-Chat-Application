package edu.northeastern.ccs.im.controller;

import edu.northeastern.ccs.im.communication.CommunicationUtils;
import edu.northeastern.ccs.im.communication.NetworkResponse;
import edu.northeastern.ccs.im.communication.NetworkResponseImpl;
import edu.northeastern.ccs.im.communication.PayloadImpl;
import edu.northeastern.ccs.im.service.ProfileService;
import edu.northeastern.ccs.im.user_group.Profile;

/**
 * Class for the profile controller
 */
public class ProfileController implements IController<Profile> {

    private ProfileService profileService = new ProfileService();

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
     * @return
     */
    public NetworkResponse addEntity(Profile pf) {
        try {
            return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                    new PayloadImpl(CommunicationUtils.toJson(profileService.createProfile(pf))));
        } catch (IllegalArgumentException e) {
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(null));
        }
    }

    /**
     * Controller to get a profile
     * @param id
     * @return
     */
    public NetworkResponse getEntity(int id) {
        try {
            return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                    new PayloadImpl(CommunicationUtils.toJson(profileService.get(id))));
        } catch (IllegalArgumentException e) {
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(null));
        }
    }

    /**
     * Controller to update a profile
     * @param pf
     * @return
     */
    public NetworkResponse updateEntity(Profile pf) {
        try {
            return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                    new PayloadImpl(CommunicationUtils.toJson(profileService.updateProfile(pf))));
        } catch (IllegalArgumentException e) {
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(null));
        }
    }

    /**
     * Controller to delete a profile
     * @param pf
     * @return
     */
    public NetworkResponse deleteEntity(Profile pf) {
        try {
            return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                    new PayloadImpl(CommunicationUtils.toJson(profileService.deleteProfile(pf))));
        } catch (IllegalArgumentException e) {
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(null));
        }
    }

    /**
     * Controller to search for a profile
     * @param usercode
     * @return
     */
    public NetworkResponse searchEntity(String usercode) {
        return null;
    }
}
