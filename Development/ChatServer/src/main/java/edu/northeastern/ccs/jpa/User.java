package edu.northeastern.ccs.jpa;

import edu.northeastern.ccs.im.userGroup.IUser;
import edu.northeastern.ccs.im.userGroup.IUserGroup;

import java.util.ArrayList;
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
    private List<Group> groups = new ArrayList<>();

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

}
