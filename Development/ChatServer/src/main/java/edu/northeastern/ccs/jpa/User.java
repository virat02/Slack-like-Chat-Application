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

    /**
     * Searches for a particular user.
     * @param name the name of the user being searched
     * @return the users with the name searched for
     */
    public List<IUser> search(String name) {
        List<IUser> users= new ArrayList<>();
        for(IUser user: this.following) {
            if(user.getProfile().getName().equals(name)) {
                users.add(user);
            }
        }
        return users;
    }

    /**
     * Follow a particular user given their id.
     * @param user of the user we want to follow.
     */
    @Override
    public void follow(IUser user) {
        if(!following.contains(user)) {
            following.add(user);
        }
    }

    /**
     * Sends a message to the specified IUsergroup.
     * @param messageText the text of the message
     * @param iGroupId the Id of the IUserGroup
     */
    @Override
    public void sendMessage(String messageText, int iGroupId) {
        Message newMessage = new Message();
        newMessage.setMessage(messageText);
        newMessage.setDeleted(false);
        newMessage.setSender(this);
        Date timeStamp = new Date();
        timeStamp.setTime(timeStamp.getTime());
        newMessage.setTimestamp(timeStamp);
        newMessage.setId((int)timeStamp.getTime());
        for(IGroup group: this.groups) {
            if(group.getId() == iGroupId) {
                newMessage.setGroup(group);
            }
        }
        this.messages.add(newMessage);

    }

    /**
     * Sets the expiration time of a message
     * @param messageId of the Message we are looking to set the expiration for
     * @param date when the message will expire
     */
    @Override
    public void setExpiration(int messageId, Date date) {
        for(Message message: this.messages) {
            if(message.getId() == messageId) {
                message.setExpiration(date);
            }
        }
    }

    /**
     * Deletes a group.
     * @param groupId the id of the group we are looking to delete.
     */
    @Override
    public void deleteGroup(int groupId) {
        for (IGroup group : this.groups) {
            if (group.getId() == groupId) {
                this.groups.remove(group);
            }
        }
    }

    /**
     * Creates a new Group instance and adds the Group to the list of groups.
     * @param iGroupId the id of the group we are adding
     */
    @Override
    public void createIGroup(int iGroupId) {
        Group newGroup = new Group();
        newGroup.setId(iGroupId);
        this.groups.add(newGroup);
    }
}
