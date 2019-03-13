package edu.northeastern.ccs.im.service;

import edu.northeastern.ccs.im.service.JPAService.UserJPAService;
import edu.northeastern.ccs.im.userGroup.*;

import java.util.Date;

public final class UserService implements IService {
    private UserJPAService userJPAService;
    private UserService() {
        userJPAService = new UserJPAService();
    }


    public static User addUser(Object user) {
        UserJPAService userJPAService = new UserJPAService();
        userJPAService.createUser((User)user);
        return userJPAService.getUser(((User) user).getId());
    }

    /**
     * Searches for a particular user.
     * @param username the name of the user being searched
     * @return the users with the name searched for
     */
    public static User search(String username) {
        UserJPAService userJPAService = new UserJPAService();
        return userJPAService.search(username);
        //return null;
    }

    /**
     * Follow a particular user given their username.
     * @param username of the user we want to follow.
     */
    public static void follow(String username, User currentUser) {
        currentUser.addFollowee(search(username));
    }

    public static User update(Object user) {
        UserJPAService userJPAService = new UserJPAService();
        userJPAService.updateUser((User) user);
        return userJPAService.getUser(((User) user).getId());
    }

    public static User delete(Object user) {
        UserJPAService userJPAService = new UserJPAService();
        userJPAService.deleteUser((User) user);
        return userJPAService.getUser(((User) user).getId());
    }

    /**
     * Check for valid username
     */

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
        this.messages.add(newMessage);
}

    private boolean isValidUsername(String uname) {
        return (uname != null && uname.matches("[A-Za-z0-9_]+"));
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
