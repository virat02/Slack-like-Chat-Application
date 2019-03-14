package edu.northeastern.ccs.im.userGroup;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class User.
 */
@Entity
@Table(name = "user")
public class User implements IUser {

    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    /** The messages. */
    @OneToMany(targetEntity = Message.class)
    private List<Message> messages = new ArrayList<>();

    /** The groups. */
    @OneToMany(targetEntity = Group.class)
    private List<Group> groups = new ArrayList<>();

    /**
     * The list of people this user follows.
     */
    @OneToMany
    private List<User> following = new ArrayList<>();

    /**
     * The list of people following this user.
     */
    @OneToMany
    private List<User> followee = new ArrayList<>();

    /** The name. */
    private String username;

    /** The password. */
    private String password;

    /** The profile. */
    private Profile profile;

    /**
     * Instantiates a new user.
     */
    public User() {
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
     * Gets the messages.
     *
     * @return the messages
     */
    public List<Message> getMessages() {
        return messages;
    }

    /**
     * Sets the messages.
     *
     * @param messages the new messages
     */
    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    /**
     * Gets the groups.
     *
     * @return the groups
     */
    public List<Group> getGroups() {
        return groups;
    }

    /**
     * Sets the groups.
     *
     * @param groups the new groups
     */
    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    /**
     * Adds the messages.
     *
     * @param msg the msg
     */
    public void addMessages(Message msg) {
        this.messages.add(msg);
    }

    /**
     * Adds the userGroup.
     *
     * @param group the userGroup
     */
    public void addGroup(Group group) {
        this.groups.add(group);
    }

    /**
     * Gets the profile.
     *
     * @return the profile
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * Sets the profile.
     *
     * @param profile the new profile
     */
    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    /**
     * Gets the following.
     *
     * @return the following
     */
    public List<User> getFollowing() {
        return this.following;
    }

    /**
     * Sets the following.
     *
     * @param following the new profile
     */
    public void setFollowing(List<User> following) {
        this.following = following;
    }

    /**
     * Gets the followees.
     *
     * @return the followee
     */
    public List<User> getFollowee() {
        return this.followee;
    }

    /**
     * Sets the following.
     *
     * @param followee the new profile
     */
    public void setFollowee(List<User> followee) {
        this.followee = followee;
    }

    /**
     * Adds a user to the list of people we are following.
     * @param user the person we are following.
     */
    public void addFollowing(User user) {
        if (user != null) {
            this.following.add(user);
        }
        else {
            throw new NullPointerException("Cannot add a non-existing user");
        }
    }

    /**
     * Adds a user to the list of people following this user.
     * @param user the person following this user.
     */
    public void addFollowee(User user) {
        if (user != null) {
            this.followee.add(user);
        }
        else {
            throw new NullPointerException("Cannot add a non-existing user");
        }
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
     * Sets the new username.
     *
     * @param username the new username
     */
    public void setUsername(String username) {
        this.username = username;
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
}
