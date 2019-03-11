package edu.northeastern.ccs.im.service;

import edu.northeastern.ccs.im.service.JPAService.UserJPAService;
import edu.northeastern.ccs.im.userGroup.*;

public final class UserService {
    private UserJPAService userJPAService;
    private UserService() {
        userJPAService = new UserJPAService();
    }


    public void addUser(Object user) {
        userJPAService.createUser((IUser)user);
    }

    /**
     * Searches for a particular user.
     * @param username the name of the user being searched
     * @return the users with the name searched for
     */
    public IUser search(String username) {
        return userJPAService.search(username);
        //return null;
    }

    /**
     * Follow a particular user given their username.
     * @param username of the user we want to follow.
     */
    public void follow(String username, IUser currentUser) {
        currentUser.addFollowee(search(username));
    }

    public void update(Object user) {
        userJPAService.updateUser((IUser) user);
    }

    public void delete(Object user) {
        userJPAService.deleteUser((IUser) user);
    }
}
