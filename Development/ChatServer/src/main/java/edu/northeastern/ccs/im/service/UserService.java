package edu.northeastern.ccs.im.service;

import edu.northeastern.ccs.im.customexceptions.*;
import edu.northeastern.ccs.im.service.jpa_service.GroupJPAService;
import edu.northeastern.ccs.im.service.jpa_service.InviteJPAService;
import edu.northeastern.ccs.im.service.jpa_service.Status;
import edu.northeastern.ccs.im.service.jpa_service.UserJPAService;
import edu.northeastern.ccs.im.user_group.Group;
import edu.northeastern.ccs.im.user_group.Invite;
import edu.northeastern.ccs.im.user_group.User;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

/**
 * The class made to delegate tasks to the JPA service and send results back to Service.
 */
public final class UserService implements IService {

    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

    private UserJPAService userJPAService;
    private InviteJPAService inviteJPAService;
    private GroupJPAService groupJPAService;

    /**
     * Constructor for this class.
     */
    public UserService() {
        userJPAService = new UserJPAService();
        inviteJPAService = new InviteJPAService();
        groupJPAService = new GroupJPAService();
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

    public void setGroupJPAService(GroupJPAService groupJPAService){
        if(groupJPAService == null) {
            this.groupJPAService = new GroupJPAService();
        } else {
            this.groupJPAService = groupJPAService;
        }
        this.groupJPAService.setEntityManager(null);
    }

    /**
     * Add user will add a user to the database.
     * @param user being added to the database.* @return the user which was added to the database.
     */
    public User addUser(User user) throws UserNotFoundException, UserNotPersistedException,
            UsernameTooSmallException, UsernameDoesNotContainNumberException,
            UsernameDoesNotContainUppercaseException, UsernameDoesNotContainLowercaseException,
            PasswordDoesNotContainLowercaseException, PasswordTooSmallException,
            PasswordDoesNotContainUppercaseException, PasswordDoesNotContainNumberException, PasswordTooLargeException, UsernameTooLongException {
        if(user.getUsername().length() < 4) {
            throw new UsernameTooSmallException("Username needs to be at least 4 letters long.");
        }
        if(user.getUsername().length() > 20) {
            throw new UsernameTooLongException("Username can't be more than 20 letters long!");
        }
        HashMap<String, Boolean> usernameCheck = checkString(user.getUsername());
        if(!usernameCheck.get("low")) {
            throw new UsernameDoesNotContainLowercaseException("Username must contain at least one lowercase letter.");
        }
        if(!usernameCheck.get("cap")) {
            throw new UsernameDoesNotContainUppercaseException("Username must contain at least one capital letter!");
        }
        if(!usernameCheck.get("num")) {
            throw new UsernameDoesNotContainNumberException("Username must contain at least one number!");
        }
        if(user.getPassword().length() < 4) {
            throw new PasswordTooSmallException("Password needs to be at least 4 letters long.");
        }
        if(user.getPassword().length() > 20) {
            throw new PasswordTooLargeException("Password can't be more than 20 letters long!");
        }
        HashMap<String, Boolean> passwordCheck = checkString(user.getPassword());
        if(!passwordCheck.get("low")) {
            throw new PasswordDoesNotContainLowercaseException("Password must contain at least one lowercase letter.");
        }
        if(!passwordCheck.get("cap")) {
            throw new PasswordDoesNotContainUppercaseException("Password must contain at least one capital letter!");
        }
        if(!passwordCheck.get("num")) {
            throw new PasswordDoesNotContainNumberException("Password must contain at least one number!");
        }
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

    private HashMap<String, Boolean> checkString(String string) {
        char character;
        HashMap<String, Boolean> booleanHashMap = new HashMap<>();
        booleanHashMap.put("cap", false);
        booleanHashMap.put("low", false);
        booleanHashMap.put("num", false);
        for(int i=0;i < string.length();i++) {
            character = string.charAt(i);
            if( Character.isDigit(character)) {
                booleanHashMap.replace("num", true);
            } else if (Character.isUpperCase(character)) {
                booleanHashMap.replace("cap", true);
            } else if (Character.isLowerCase(character)) {
                booleanHashMap.replace("low", true);
            }
            if(booleanHashMap.get("num") && booleanHashMap.get("cap") && booleanHashMap.get("low")) {
                break;
            }
        }
        return booleanHashMap;
    }


    /**
     * Follow a particular user given their username.
     * @param username of the user we want to follow.
     */
    public User follow(String username, User currentUser) throws UserNotFoundException {

        User u = search(username);
        User fetchedCurrentUser = search(currentUser.getUsername());
        if(fetchedCurrentUser != null && u != null){
            fetchedCurrentUser.addFollowing(u);
            userJPAService.setEntityManager(null);
            userJPAService.updateUser(fetchedCurrentUser);
            return fetchedCurrentUser;
        }
        else{
            LOGGER.info("Could not successfully follow the user!");
            throw new IllegalArgumentException("Could not successfully follow the user with username: "+username);
        }

    }

    /**
     * Unfollow a particular user given their username.
     * @param username of the user we want to unfollow.
     */
    public User unfollow(String username, User currentUser) throws UserNotFoundException, UnfollowNotFollowingUserException {
        User u = search(username);
        User fetchedCurrentUser = search(currentUser.getUsername());
        if(fetchedCurrentUser != null && u != null){
            fetchedCurrentUser.removeFollowing(u);
            userJPAService.setEntityManager(null);
            userJPAService.updateUser(fetchedCurrentUser);
            return fetchedCurrentUser;
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
        User newUser = (User) user;
        return userJPAService.loginUser(newUser);
    }

    /**
     * The service for the invite system, it'll create the invite and send it to the JPA service.
     * @param invite the invite to be sent.
     * @return the invite itself back to the controller.
     */
    public Invite sendInvite(Invite invite) throws InviteNotAddedException, InviteNotFoundException, UserNotFoundException, GroupNotFoundException {
        inviteJPAService.setEntityManager(null);
        userJPAService.setEntityManager(null);
        groupJPAService.setEntityManager(null);
        User sender = userJPAService.search(invite.getSender().getUsername());
        userJPAService.setEntityManager(null);
        User reciever = userJPAService.search(invite.getReceiver().getUsername());
        Group group = groupJPAService.searchUsingCode(invite.getGroup().getGroupCode());
        Invite persistInvite = new Invite();
        persistInvite.setGroup(group);
        persistInvite.setSender(sender);
        persistInvite.setReceiver(reciever);
        persistInvite.setStatus(Status.NOUPDATE);
        int id = inviteJPAService.createInvite(persistInvite);
        inviteJPAService.setEntityManager(null);
        return inviteJPAService.getInvite(id);
    }

    /**
     * Sets the status of the invite to deleted and sends the invite to the JPA service and gets the invite
     * from the database after the status has been set.
     * @param invite to be deleted.
     * @return invite that was deleted.
     */
    public Invite deleteInvite(Invite invite) throws InviteNotDeletedException {
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

    /**
     * Searches for the invite by the group code retrieving the list of invites from the JPA service.
     * @param groupCode the code for the group with the invites.
     * @return List of invites for the group
     * @throws GroupNotFoundException if the group is not found
     * @throws InviteNotFoundException if the invite is not found
     */
    public List<Invite> searchInviteByGroupCode(String groupCode, String username) throws GroupNotFoundException,
            InviteNotFoundException, UserNotFoundException {
        userJPAService.setEntityManager(null);
        User retrievedUser = userJPAService.search(username);

        inviteJPAService.setEntityManager(null);
        return inviteJPAService.searchInviteByGroupCode(groupCode , retrievedUser);
    }
}
