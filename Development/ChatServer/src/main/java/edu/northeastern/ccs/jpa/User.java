package edu.northeastern.ccs.jpa;

import edu.northeastern.ccs.im.userGroup.IGroup;
import edu.northeastern.ccs.im.userGroup.IUser;
import edu.northeastern.ccs.im.userGroup.IUserGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

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
    private List<IGroup> groups = new ArrayList<>();

    /**
     * The list of people following this user.
     */
    @ManyToOne
    private List<IUser> followers = new ArrayList<>();

    /**
     * The list of people this user follows.
     */
    @OneToMany
    private List<IUser> following = new ArrayList<>();

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
    public List<IGroup> getGroups() {
        return groups;
    }

    /**
     * Sets the groups.
     *
     * @param groups the new groups
     */
    public void setGroups(List<IGroup> groups) {
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
    public void addGroup(IGroup group) {
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
     * Gets the followers.
     *
     * @return the followers
     */
    public List<IUser> getFollowers() {
        return this.followers;
    }

    /**
     * Sets the followers.
     *
     * @param followers the new profile
     */
    public void setFollowers(List<IUser> followers) {
        this.followers = followers;
    }

    /**
     * Gets the following.
     *
     * @return the following
     */
    public List<IUser> getFollowing() {
        return this.following;
    }

    /**
     * Sets the following.
     *
     * @param following the new profile
     */
    public void setFollowing(List<IUser> following) {
        this.following = following;
    }
}
