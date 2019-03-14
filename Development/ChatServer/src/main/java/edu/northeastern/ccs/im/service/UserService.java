package edu.northeastern.ccs.im.service;

import edu.northeastern.ccs.im.service.JPAService.UserJPAService;
import edu.northeastern.ccs.im.userGroup.User;

/**
 * The class made to delegate tasks to the JPA service and send results back to Service.
 */
public final class UserService implements IService {
    private UserJPAService userJPAService;

    /**
     * Constructor for this class.
     */
    public UserService() {
        userJPAService = new UserJPAService();
    }

    /**
     * A method to set the JPA Service for this class, makes the class more testable.
     * @param userJPAService for this class.
     */
    public void setJPAService(UserJPAService userJPAService) {
        if(userJPAService == null) {
            this.userJPAService = new UserJPAService();
        } else {
            this.userJPAService = userJPAService;
        }
        this.userJPAService.setEntityManager(null);
    }

    /**
     * Add user will add a user to the database.
     * @param user being added to the database.* @return the user which was added to the database.
     */
    public User addUser(Object user) {
        int id = userJPAService.createUser((User)user);
        if(id == 0) {
            return null;
        }
        return userJPAService.getUser(((User) user).getId());
    }

    /**
     * Searches for a particular user.
     * @param username the name of the user being searched
     * @return the users with the name searched for*/
    public User search(String username) {
        return userJPAService.search(username);
    }

    /** Follow a particular user given their username.
     * @param username of the user we want to follow.
     */
    public void follow(String username, User currentUser) {
        currentUser.addFollowee(search(username));
        userJPAService.updateUser(currentUser);
    }

    /**
     * The update method will update the user object.
     * @param user being updated.
     * @return the updated user.
     */
    public User update(Object user) {
        userJPAService.updateUser((User) user);
        return userJPAService.getUser(((User) user).getId());
    }

    /**
     * The delete function to delete a user from the database.
     * @param user being deleted from the database.
     * @return the user which was deleted from the database.
     */
    public User delete(Object user) {
        userJPAService.deleteUser((User) user);
        return userJPAService.getUser(((User) user).getId());
    }

    /**
     * A function to login the user.
     * @param user trying to login to the server.
     * @return User instance logging into the server.
     */
    public User loginUser(Object user) {
        return userJPAService.loginUser((User) user);
    }

}
