package edu.northeastern.ccs.im.service;

import edu.northeastern.ccs.im.service.JPAService.UserJPAService;
import edu.northeastern.ccs.im.userGroup.User;

public final class UserService implements IService {
    private UserJPAService userJPAService;
    private UserService() {
        userJPAService = new UserJPAService();
    }


    public static User addUser(Object user) {
        UserJPAService userJPAService = new UserJPAService();
        int id = userJPAService.createUser((User)user);
        if(id == 0) {
            return null;
        }
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

    public static User loginUser(Object user) {
        UserJPAService userJPAService = new UserJPAService();
        return userJPAService.loginUser((User) user);
    }

}
