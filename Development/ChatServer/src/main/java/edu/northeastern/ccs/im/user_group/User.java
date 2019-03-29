package edu.northeastern.ccs.im.user_group;

import com.fasterxml.jackson.annotation.*;

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

    @OneToMany(targetEntity=User.class)
    @JoinTable
    (
        name="user_follower",
        joinColumns={ @JoinColumn(name="USER_ID", referencedColumnName="ID") },
        inverseJoinColumns={ @JoinColumn(name="FOLLOWER_ID", referencedColumnName="ID") }
    )
    @JsonIgnore
    private List<User> following = new ArrayList<>();

    /** The name. */
    private String username;

    /** The password. */
    private String password;

    /** The profile. */
    private Profile profile;

    /** The profile access. */
    private Boolean profileAccess;

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
     * Adds the user_group.
     *
     * @param group the user_group
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
     * Removes a user to the list of people we are following.
     * @param user the person we are following.
     */
    public void removeFollowing(User user) {
        if (user != null) {

            for (User obj : this.following) {
                if (obj.username.equals(user.username)) {
                    this.following.remove(obj);
                    break;
                }
            }
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

    /**
     * Gets the profile access
     * @return the profile access
     */
    public Boolean getProfileAccess() {
        return this.profileAccess;
    }

    /**
     * Sets the user profile access
     * @param access The access level of the profile
     */
    public void setProfileAccess(boolean access) {
        this.profileAccess = access;
    }

    /**
     * equals method to check equality only on username field of a user
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof User
                && ((User) obj).username.equals(this.username);
    }

    /**
     * Hashcode of a user
     * @return
     */
    @Override
    public int hashCode() {
        return this.username.hashCode();
    }
}
