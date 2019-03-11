package edu.northeastern.ccs.im.userGroup;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * The Class Profile.
 */
@Entity
@Table(name="profile")
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

    /** The profile access. */
    private Boolean profileAccess;

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
    public Profile(int id, String username, String email, String password, String imageUrl, Boolean access) {
        super();
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.imageUrl = imageUrl;
        this.profileAccess = access;
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

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getUsername() {

        return username;
    }

    /**
     * Sets the name.
     *
     * @param username the new name
     */
    public void setUsername(String username) {
        this.username = username;
    }
    /**
     * Gets the email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }
    /**
     * Sets the email.
     *
     * @param email the new email
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }
    /**
     * Sets the new password.
     *
     * @param password the new password
     */
    public void setPassword(String password) {

        this.password = password;
    }
    /**
     * Gets the image url.
     *
     * @return the image url
     */
    public String getImageUrl() {
        return imageUrl;
    }
    /**
     * Sets the image url.
     *
     * @param imageUrl the new image url
     */
    public void setImageUrl(String imageUrl) {
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
     * Gets the profile access
     * @return the profile access
     */
    public Boolean getProfileAccess() {
        return this.profileAccess;
    }

    /**
     * Sets the user profile access
     * @param access
     */
    public void setProfileAccess(boolean access) {
        this.profileAccess = access;
    }

    @Override
    public String toString() {
        return "Id: "+getId()+"\nUsername: "+getUsername() + "\nEmail: "+getEmail()+"\nImage URL: "+getImageUrl()+"\nProfile visible? : "+getProfileAccess();
    }

}
