package edu.northeastern.ccs.im.controller;

import edu.northeastern.ccs.im.service.ProfileService;
import edu.northeastern.ccs.im.userGroup.Profile;

public class ProfileController implements IController<Profile> {

    private ProfileService profileService;

    public Profile addEntity(Profile pf) {
        try {
            return profileService.createProfile(pf);
        }
        catch(IllegalArgumentException e) {
            throw new IllegalArgumentException("Cannot create a new group");
        }

    }

    public Profile getEntity(int id) {

        return profileService.get(id);
    }

    public Profile updateEntity(Profile pf) {
        return profileService.updateProfile(pf);

    }

    public Profile deleteEntity(Profile pf) {
        return profileService.deleteProfile(pf);

    }

    public Profile searchEntity(String usercode) {
        return null;
    }
}
