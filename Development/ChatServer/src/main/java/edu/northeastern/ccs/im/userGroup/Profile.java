package edu.northeastern.ccs.im.userGroup;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.net.URL;
/**
 * The Class Profile.
 */
@Entity
//@Table(name="profile")
public class Profile {
    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    /** The name. */
    private String username;
    /** The email. */
    private String email;
    /** The password. */
    private String password;
    /** The image url. */
    private String imageUrl;
    /** The user. */
    @OneToOne
    private IUser user;
    /**
     * Instantiates a new profile.
     *
     * @param id the id
     * @param username the name
     * @param email the email
     * @param password the password
     * @param imageUrl the image url
     */
    public Profile(int id, String username, String email, String password, String imageUrl) {
        super();
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.imageUrl = imageUrl;
    }
    /**
     * Instantiates a new profile.
     */
    public Profile() {
        super();
    }
    /**
     * Gets the id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }
//
//    /**
//     * Sets the id.
//     *
//     * @param id the new id
//     */
//    public void setId(int id) {
//        this.id = id;
//    }
    /**
     * Gets the name.
     *
     * @return the name
     */
    private String getUsername() {
        return username;
    }
    /**
     * Sets the name.
     *
     * @param name the new name
     */
    private void setUsername(String name) {
        this.username = username;
    }
    /**
     * Gets the email.
     *
     * @return the email
     */
    private String getEmail() {
        return email;
    }
    /**
     * Sets the email.
     *
     * @param email the new email
     */
    private void setEmail(String email) {
        this.email = email;
    }
    /**
     * Gets the password.
     *
     * @return the password
     */
    private String getPassword() {
        return password;
    }
    /**
     * Sets the new password.
     *
     * @param password the new password
     */
    private void setPassword(String password) {
        this.password = password;
    }
    /**
     * Gets the image url.
     *
     * @return the image url
     */
    private String getImageUrl() {
        return imageUrl;
    }
    /**
     * Sets the image url.
     *
     * @param imageUrl the new image url
     */
    private void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    /**
     * Gets the user.
     *
     * @return the user
     */
    public IUser getUser() {
        return user;
    }
    /**
     * Sets the user.
     *
     * @param user the new user
     */
    public void setUser(IUser user) {
        this.user = user;
    }
    /**
     * Check for valid username
     */
    private boolean isValidUsername(String uname) {
        return (username != null) && username.matches("[A-Za-z0-9_]+");
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
    public Profile createProfile(String username, String email, String password, String imageUrl) {
        try {
            if (this.username == null && isValidUsername(username)) {
                setUsername(username);
            }
            if (this.email == null && isValidEmail(email)) {
                setEmail(email);
            }
            if (this.password == null && isValidPassword(password)) {
                this.password = password;
            }
            if (this.imageUrl == null && isValidImageURL(imageUrl)) {
                setImageUrl(imageUrl);
            }
            return new Profile(id, username, email, password, imageUrl);
        }
        catch(IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid input, cannot create profile!");
        }
    }
    /**
     * Updates an existing profile if the respective inputs are valid
     */
    public void updateProfile(String username, String email, String oldPassword, String newPassword, String imageUrl) {
        try {
            if (this.username != null && isValidUsername(username)) {
                setUsername(username);
            }
            if (this.email != null && isValidEmail(email)) {
                setEmail(email);
            }
            if (this.password != null
                    //Authenticates user by allowing them to set the new password only if they know their current password
                    && this.password.equals(oldPassword)
                    && isValidPassword(newPassword)) {
                setPassword(newPassword);
            }
            if (this.imageUrl != null && isValidImageURL(imageUrl)) {
                setImageUrl(imageUrl);
            }
        }
        catch(IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid input, cannot update profile!");
        }
    }
    /**
     * Deletes a profile
     */
    public void deleteProfile() {
        //set id to null??
        setUsername(null);
        setEmail(null);
        setPassword(null);
        setImageUrl(null);
    }
}
