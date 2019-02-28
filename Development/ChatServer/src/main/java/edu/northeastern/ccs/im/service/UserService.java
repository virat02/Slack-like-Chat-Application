package edu.northeastern.ccs.im.service;

import edu.northeastern.ccs.im.userGroup.IGroup;
import edu.northeastern.ccs.im.userGroup.IUser;
import edu.northeastern.ccs.im.userGroup.IUserGroup;
import edu.northeastern.ccs.jpa.Group;
import edu.northeastern.ccs.jpa.Message;
import edu.northeastern.ccs.jpa.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserService {
    private List<IGroup> groups;
    private List<IUser> following;
    private List<Message> messages;
    private IUser user;

    public UserService(User user) {
        groups = user.getGroups();
        following = user.getFollowing();
        messages = user.getMessages();
        this.user = user;
    }


    public IUser addUser(IUserGroup user) {
        User thisUser = new User();
        return thisUser;
    }

    /**
     * Searches for a particular user.
     * @param name the name of the user being searched
     * @return the users with the name searched for
     */
    public IUser search(String name) {
        List<IUser> users = new ArrayList<>();
        for(IUser user: this.following) {
            if(user.getProfile().getName().equals(name)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Follow a particular user given their id.
     * @param user of the user we want to follow.
     */
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
    public void sendMessage(String messageText, int iGroupId) {
        Message newMessage = new Message();
        newMessage.setMessage(messageText);
        newMessage.setDeleted(false);
        newMessage.setSender(this.user);
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
    public void createIGroup(int iGroupId) {
        Group newGroup = new Group();
        newGroup.setId(iGroupId);
        this.groups.add(newGroup);
    }
}
