package edu.northeastern.ccs.im.service;

import java.util.Date;

import edu.northeastern.ccs.im.service.JPAService.UserJPAService;
import edu.northeastern.ccs.im.userGroup.*;

public final class UserService implements IService {
    private UserJPAService userJPAService;

    /**
     * Constructor for UserService
     */
    public UserService() {
        userJPAService = new UserJPAService();
    }

    /**
     * Persists a user using the JPA service
     * @param user
     * @return A user object
     */
    public User addUser(Object user) {
        userJPAService.createUser((User)user);
        return userJPAService.getUser(((User) user).getId());
    }

    /**
     * Searches for a particular user.
     * @param username the name of the user being searched
     * @return the users with the name searched for
     */
    public User search(String username) {
        return userJPAService.search(username);
    }

    /**
     * Follow a particular user given their username.
     * @param username of the user we want to follow.
     */
    public void follow(String username, User currentUser) {
        currentUser.addFollowee(search(username));
    }

    public User update(Object user) {
        userJPAService.updateUser((User) user);
        return userJPAService.getUser(((User) user).getId());
    }

    public User delete(Object user) {
        userJPAService.deleteUser((User) user);
        return userJPAService.getUser(((User) user).getId());
    }

    public void sendMessage(String messageText, int iGroupId) {
        Message newMessage = new Message();
        newMessage.setMessage(messageText);
        newMessage.setDeleted(false);
//        newMessage.setSender(this.user);
        Date timeStamp = new Date();
        timeStamp.setTime(timeStamp.getTime());
        newMessage.setTimestamp(timeStamp);
        newMessage.setId((int)timeStamp.getTime());
//        for(IGroup group: this.groups) {
//            if(group.getId() == iGroupId) {
//                newMessage.setGroup(group);
//            }
//        }
//        this.messages.add(newMessage);
}

    /**
     * Updates an existing profile username if the respective input is valid
     */
    public boolean updateUsername(User user, String username) {
        if (user.getUsername() != null && isValidUsername(username)) {
            user.setUsername(username);
            return true;
        }

        return false;
    }

    /**
     * Updates an existing profile password if the user inputs the correct old password and a valid new password
     */
    public boolean updatePassword(User user, String oldPassword, String newPassword) {
        if (user.getPassword() != null
                //Authenticates user by allowing them to set the new password only if they know their current password
                && user.getPassword().equals(oldPassword)
                && isValidPassword(newPassword)) {
            user.setPassword(newPassword);
            return true;
        }

        return false;
    }

    /**
     * Check for valid username
     */
    private boolean isValidUsername(String uname) {
        return (uname != null && uname.matches("[A-Za-z0-9_]+"));
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
}
