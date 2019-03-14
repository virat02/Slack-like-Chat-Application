package edu.northeastern.ccs.im.service;

import edu.northeastern.ccs.im.service.JPAService.ProfileJPAService;
import edu.northeastern.ccs.im.userGroup.Profile;

import java.net.URL;

public class ProfileService {

    private ProfileJPAService profileJPAService;

    /**
     * Constructor for profile service class
     */
    public ProfileService() {
        profileJPAService = new ProfileJPAService();
    }


    /**
     * Check for valid email address
     */
    private boolean isValidEmail(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }


    /**
     * Check for valid image URL
     */
    private boolean isValidImageURL(String imageUrl) {
        try
        {
            URL url = new URL(imageUrl);
            url.toURI();
            return true;
        } catch (Exception exception)
        {
            return false;
        }
    }

    /**
     * Creates a profile if the respective inputs are valid
     */
//    public Profile createProfile(String username, String email, String password, String imageUrl, boolean access) {
//        if (isValidUsername(username)
//                    && isValidEmail(email)
//                    && isValidPassword(password)
//                    && isValidImageURL(imageUrl)) {
//
//                Profile pf = new Profile(p.getId(), username, email, password, imageUrl, access);
//                //profileJPAService.createProfile(pf);
//                return pf;
//            }
//            else {
//                throw new IllegalArgumentException();
//            }
//        }

    public Profile createProfile(Profile pf) {
        profileJPAService.createProfile(pf);
        return profileJPAService.getProfile(pf.getId());

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
     * Updates an existing profile email if the respective input is valid
     */
    public boolean updateEmail(Profile p, String email) {
        if (p.getEmail() != null && isValidEmail(email)) {
            p.setEmail(email);
            //profileJPAService.updateProfile(p);
            return true;
        }

        return false;
    }


    /**
     * Updates an existing profile image URL if the respective input is valid
     */
    public boolean updateImageURL(Profile p, String imageURL) {
        if (p.getImageUrl() != null && isValidImageURL(imageURL)) {
            p.setImageUrl(imageURL);
            //profileJPAService.updateProfile(p);
            return true;
        }

        return false;
    }

    /**
     * Updates an existing profile if the respective inputs are valid
     */
//    public void updateProfile(String username, String email, String oldPassword, String newPassword, String imageUrl, boolean access) {
//        if (updateUsername(username)
//                && updateEmail(email)
//                && updatePassword(oldPassword, newPassword)
//                && updateImageURL(imageUrl)) {
//
//            p.setProfileAccess(access);
//
//            //profileJPAService.updateProfile(p);
//        }
//        else {
//            throw new IllegalArgumentException("Invalid input, cannot update profile!");
//        }
//    }

    public Profile updateProfile(Profile pf) {
        profileJPAService.updateProfile(pf);
        return profileJPAService.getProfile(pf.getId());
    }

    /**
     * Deletes a profile
     */
//    public void deleteProfile() {
//        //set id to null??
//        p.setUsername(null);
//        p.setEmail(null);
//        p.setPassword(null);
//        p.setImageUrl(null);
//        p.setProfileAccess(false);
//
//        //profileJPAService.deleteProfile(p);
//    }

    public Profile deleteProfile(Profile pf) {
        profileJPAService.deleteProfile(pf);
        return profileJPAService.getProfile(pf.getId());
    }
}
