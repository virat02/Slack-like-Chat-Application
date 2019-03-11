package edu.northeastern.ccs.im.service;

import edu.northeastern.ccs.im.service.JPAService.UserJPAService;
import edu.northeastern.ccs.im.userGroup.*;

public final class UserService implements IService {
    private UserJPAService userJPAService;
    private UserService() {
        userJPAService = new UserJPAService();
    }


    public IUser addUser(Object user) {
        userJPAService.createUser((IUser)user);
        return userJPAService.getUser(((IUser) user).getId());
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

    public IUser update(Object user) {
        userJPAService.updateUser((IUser) user);
        return userJPAService.getUser(((IUser) user).getId());
    }

    public IUser delete(Object user) {
        userJPAService.deleteUser((IUser) user);
        return userJPAService.getUser(((IUser) user).getId());
    }
}
