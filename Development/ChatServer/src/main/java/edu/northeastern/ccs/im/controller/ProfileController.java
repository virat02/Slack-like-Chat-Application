package edu.northeastern.ccs.im.controller;

import edu.northeastern.ccs.im.service.ProfileService;
import edu.northeastern.ccs.im.service.UserService;
import edu.northeastern.ccs.im.userGroup.IUser;
import edu.northeastern.ccs.im.userGroup.Profile;
import edu.northeastern.ccs.im.view.View;

public class ProfileController implements IController {

    private View view;
    private ProfileService profileService;
    private UserService userService;


    public void addEntity(Object pf) {
        Profile profile;
        try {
            //profile = profileService.createProfile(pf);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Can't add Profile");
        }

        //view.showProfile(profile);
    }
//
//    @Override
//    public Profile getProfile(Profile pf) {
//        return null;
//    }

    public void updateEntity(Object pf) {

    }

    public void deleteEntity(Object pf) {

    }

    public IUser searchEntity(String username) {
        return userService.search(username);
    }
}
