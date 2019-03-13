package edu.northeastern.ccs.im.controller;

import edu.northeastern.ccs.im.communication.CommunicationUtils;
import edu.northeastern.ccs.im.communication.NetworkResponse;
import edu.northeastern.ccs.im.communication.NetworkResponseImpl;
import edu.northeastern.ccs.im.communication.PayloadImpl;
import edu.northeastern.ccs.im.service.ProfileService;
import edu.northeastern.ccs.im.userGroup.Profile;

public class ProfileController implements IController<Profile> {

    private ProfileService profileService;

    public NetworkResponse addEntity(Profile pf) {
        try {
            return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                    new PayloadImpl(CommunicationUtils.toJson(profileService.createProfile(pf))));
        } catch (IllegalArgumentException e) {
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(null));
        }
    }

    public NetworkResponse getEntity(int id) {
        try {
            return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                    new PayloadImpl(CommunicationUtils.toJson(profileService.get(id))));
        } catch (IllegalArgumentException e) {
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(null));
        }
    }

    public NetworkResponse updateEntity(Profile pf) {
        try {
            return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                    new PayloadImpl(CommunicationUtils.toJson(profileService.updateProfile(pf))));
        } catch (IllegalArgumentException e) {
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(null));
        }
    }

    public NetworkResponse deleteEntity(Profile pf) {
        try {
            return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                    new PayloadImpl(CommunicationUtils.toJson(profileService.deleteProfile(pf))));
        } catch (IllegalArgumentException e) {
            return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
                    new PayloadImpl(null));
        }
    }

    public NetworkResponse searchEntity(String usercode) {
        return null;
    }
}
