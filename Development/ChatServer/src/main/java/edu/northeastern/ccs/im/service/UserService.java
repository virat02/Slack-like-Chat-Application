package edu.northeastern.ccs.im.service;

import edu.northeastern.ccs.im.customexceptions.*;
import edu.northeastern.ccs.im.service.jpa_service.InviteJPAService;
import edu.northeastern.ccs.im.service.jpa_service.Status;
import edu.northeastern.ccs.im.service.jpa_service.UserJPAService;
import edu.northeastern.ccs.im.user_group.Invite;
import edu.northeastern.ccs.im.user_group.User;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * The class made to delegate tasks to the JPA service and send results back to Service.
 */
public final class UserService implements IService {

    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

    private UserJPAService userJPAService;
    private InviteJPAService inviteJPAService;

    /**
     * Constructor for this class.
     */
    public UserService() {
        userJPAService = new UserJPAService();
        inviteJPAService = new InviteJPAService();
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

    public void setInviteJPAService(InviteJPAService inviteJPAService){
        if(inviteJPAService == null) {
            this.inviteJPAService = new InviteJPAService();
        } else {
            this.inviteJPAService = inviteJPAService;
        }
        this.inviteJPAService.setEntityManager(null);
    }

    /**
     * Add user will add a user to the database.
     * @param user being added to the database.* @return the user which was added to the database.
     */
    public User addUser(User user) throws UserNotFoundException, UserNotPersistedException {
        userJPAService.setEntityManager(null);
        int id = userJPAService.createUser(user);
        if(id == 0) {
            return null;
        }
        userJPAService.setEntityManager(null);
        return userJPAService.getUser(user.getId());
    }

    /**
     * Searches for a particular user.
     * @param username the name of the user being searched
     * @return the users with the name searched for*/
    public User search(String username) throws UserNotFoundException {
        userJPAService.setEntityManager(null);
        return userJPAService.search(username);
    }

    /**
     * Follow a particular user given their username.
     * @param username of the user we want to follow.
     */
    public User follow(String username, User currentUser) throws UserNotFoundException {

        User u = search(username);

        if(currentUser != null && u != null){
            currentUser.addFollowing(u);
            userJPAService.setEntityManager(null);
            userJPAService.updateUser(currentUser);
            return currentUser;
        }
        else{
            LOGGER.info("Could not successfully follow the user!");
            throw new IllegalArgumentException("Could not successfully follow the user with username: "+username);
        }

    }

    /**
     * Get a list of followers for this user
     * @param username
     * @return
     */
    public List<User> getFollowers(String username) throws UserNotFoundException, ListOfUsersNotFound {
        User u = search(username);

        if(u != null) {
            userJPAService.setEntityManager(null);
            return userJPAService.getFollowers(u);
        }
        else {
            return Collections.emptyList();
        }
    }

    /**
     * Get a list of followees for this user
     * @param username
     * @return
     */
    public List<User> getFollowees(String username) throws UserNotFoundException, ListOfUsersNotFound {
        User u = search(username);
        if(u != null) {
            userJPAService.setEntityManager(null);
            return userJPAService.getFollowees(u);
        }
        else{
            return Collections.emptyList();
        }
    }

    /**
     * The update method will update the user object.
     * @param user being updated.
     * @return the updated user.
     */
    public User update(User user) throws UserNotFoundException {
        userJPAService.setEntityManager(null);
        userJPAService.updateUser(user);
        userJPAService.setEntityManager(null);
        return userJPAService.getUser(user.getId());
    }

    /**
     * The delete function to delete a user from the database.
     * @param user being deleted from the database.
     * @return the user which was deleted from the database.
     */
    public User delete(User user) throws UserNotFoundException {
        userJPAService.deleteUser(user);
        return userJPAService.getUser(user.getId());
    }

    /**
     * A function to login the user.
     * @param user trying to login to the server.
     * @return User instance logging into the server.
     */
    public User loginUser(Object user) throws UserNotFoundException {
        userJPAService.setEntityManager(null);
        return userJPAService.loginUser((User) user);
    }

    /**
     * The service for the invite system, it'll create the invite and send it to the JPA service.
     * @param invite the invite to be sent.
     * @return the invite itself back to the controller.
     */
    public Invite sendInvite(Invite invite) throws InviteNotAddedException, InviteNotFoundException {
        inviteJPAService.setEntityManager(null);
        int id =inviteJPAService.createInvite(invite);
        inviteJPAService.setEntityManager(null);
        return inviteJPAService.getInvite(id);
    }

    /**
     * Sets the status of the invite to deleted and sends the invite to the JPA service and gets the invite
     * from the database after the status has been set.
     * @param invite to be deleted.
     * @return invite that was deleted.
     */
    public Invite deleteInvite(Invite invite) throws InviteNotDeletedException, InviteNotFoundException {
        invite.setStatus(Status.DELETED);
        inviteJPAService.setEntityManager(null);
        return inviteJPAService.deleteInvite(invite);
    }

    /**
     * Updates the invite in the JPA service then gets the invite to send back to the controller.
     * @param invite to be updated in the database.
     * @return the invite that was updated in the database.
     */
    public Invite updateInvite(Invite invite) throws InviteNotUpdatedException, InviteNotFoundException {
        inviteJPAService.setEntityManager(null);
        inviteJPAService.updateInvite(invite);
        inviteJPAService.setEntityManager(null);
        return inviteJPAService.getInvite(invite.getId());
    }
}
