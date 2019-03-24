package edu.northeastern.ccs.im.userGroup;

import javax.persistence.*;

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

    /** The email. */
    private String email;

    /** The image url. */
    private String imageUrl;

    /**
     * Instantiates a new profile.
     *
     * @param id the id
     * @param email the email
     * @param imageUrl the image url
     */
    public Profile(int id, String email, String imageUrl) {
        super();
        this.id = id;
        this.email = email;
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

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Id: "+getId()+ "\nEmail: "+getEmail()+"\nImage URL: "+ getImageUrl();
    }

}
