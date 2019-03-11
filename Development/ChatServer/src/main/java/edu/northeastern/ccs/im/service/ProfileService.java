package edu.northeastern.ccs.im.service;

import edu.northeastern.ccs.im.service.JPAService.ProfileJPAService;
import edu.northeastern.ccs.im.userGroup.Profile;

import java.net.URL;

public class ProfileService {

    private Profile p;
    private ProfileJPAService profileJPAService;

    /**
     * Constructor for profile service class
     * @param p
     */
    public ProfileService(Profile p) {
        this.p = p;
    }

    /**
     * Check for valid username
     */
    private boolean isValidUsername(String uname) {
        return (uname != null && uname.matches("[A-Za-z0-9_]+"));
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
     * Check for valid password
     * Returns true if and only if password:
     *         1. have at least eight characters.
     *         2. consists of only letters and digits.
     *         3. must contain at least two digits.
     */
    private static boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        } else {
            char c;
            int count = 1;
            for (int i = 0; i < password.length() - 1; i++) {
                c = password.charAt(i);
                if (!Character.isLetterOrDigit(c)) {
                    return false;
                } else if (Character.isDigit(c)) {
                    count++;
                    if (count < 2)   {
                        return false;
                    }
                }
            }
        }
        return true;
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
    public Profile createProfile(String username, String email, String password, String imageUrl, boolean access) {
        if (isValidUsername(username)
                    && isValidEmail(email)
                    && isValidPassword(password)
                    && isValidImageURL(imageUrl)) {

                Profile pf = new Profile(p.getId(), username, email, password, imageUrl, access);
                //profileJPAService.createProfile(pf);
                return pf;
            }
            else {
                throw new IllegalArgumentException();
            }
        }

    /**
     * Updates an existing profile username if the respective input is valid
     */
    public boolean updateUsername(String username) {
        if (p.getUsername() != null && isValidUsername(username)) {
            p.setUsername(username);
            //profileJPAService.updateProfile(p);
            return true;
        }

        return false;
    }

    /**
     * Updates an existing profile email if the respective input is valid
     */
    public boolean updateEmail(String email) {
        if (p.getEmail() != null && isValidEmail(email)) {
            p.setEmail(email);
            //profileJPAService.updateProfile(p);
            return true;
        }

        return false;
    }

    /**
     * Updates an existing profile password if the user inputs the correct old password and a valid new password
     */
    public boolean updatePassword(String oldPassword, String newPassword) {
        if (p.getPassword() != null
                //Authenticates user by allowing them to set the new password only if they know their current password
                && p.getPassword().equals(oldPassword)
                && isValidPassword(newPassword)) {
            p.setPassword(newPassword);
            //profileJPAService.updateProfile(p);
            return true;
        }

        return false;
    }

    /**
     * Updates an existing profile image URL if the respective input is valid
     */
    public boolean updateImageURL(String imageURL) {
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
    public void updateProfile(String username, String email, String oldPassword, String newPassword, String imageUrl, boolean access) {
        if (updateUsername(username)
                && updateEmail(email)
                && updatePassword(oldPassword, newPassword)
                && updateImageURL(imageUrl)) {

            p.setProfileAccess(access);

            //profileJPAService.updateProfile(p);
        }
        else {
            throw new IllegalArgumentException("Invalid input, cannot update profile!");
        }
    }

    /**
     * Deletes a profile
     */
    public void deleteProfile() {
        //set id to null??
        p.setUsername(null);
        p.setEmail(null);
        p.setPassword(null);
        p.setImageUrl(null);
        p.setProfileAccess(false);

        //profileJPAService.deleteProfile(p);
    }
}
