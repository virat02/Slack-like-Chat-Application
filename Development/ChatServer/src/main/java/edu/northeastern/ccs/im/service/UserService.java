package edu.northeastern.ccs.im.service;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.customexceptions.*;
import edu.northeastern.ccs.im.service.jpa_service.*;
import edu.northeastern.ccs.im.user_group.Group;
import edu.northeastern.ccs.im.user_group.Invite;
import edu.northeastern.ccs.im.user_group.UserChatRoomLogOffEvent;
import edu.northeastern.ccs.im.user_group.User;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

/**
 * The class made to delegate tasks to the JPA service and send results back to Service.
 */
public final class UserService implements IService {

    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());
    private static final UserService userServiceinstance = new UserService();

    private UserJPAService userJPAService;
    private AllJPAService jpaService;
    private InviteJPAService inviteJPAService;
    private GroupJPAService groupJPAService;

    /**
     * Constructor for this class.
     */
    private UserService() {
        userJPAService = UserJPAService.getInstance();
        inviteJPAService = InviteJPAService.getInstance();
        groupJPAService = GroupJPAService.getInstance();
        jpaService = AllJPAService.getInstance();
    }

    /**
     * @return Singleton instance for UserService
     */
    public static UserService getInstance(){
        return userServiceinstance;
    }

    /**
     * A method to set the JPA Service for this class, makes the class more testable.
     * @param userJPAService for this class.
     */
    public void setJPAService(UserJPAService userJPAService) {
        if(userJPAService == null) {
            this.userJPAService = UserJPAService.getInstance();
        } else {
            this.userJPAService = userJPAService;
        }
        this.userJPAService.setEntityManager(null);
    }

    /**
     * A method to set the user JPA Service for this class, makes the class more testable.
     *
     * @param jpaService for this class.
     */
    public void setAllService(AllJPAService jpaService) {
        if (jpaService == null) {
            this.jpaService = AllJPAService.getInstance();
        } else {
            this.jpaService = jpaService;
        }
    }

    /**
     * Sets the inviteJPA Service, mostly userful in testing.
     * @param inviteJPAService is the jpa service we plan on initializing.
     */
    public void setInviteJPAService(InviteJPAService inviteJPAService){
        if(inviteJPAService == null) {
            this.inviteJPAService = InviteJPAService.getInstance();
        } else {
            this.inviteJPAService = inviteJPAService;
        }
        this.inviteJPAService.setEntityManager(null);
    }

    /**
     * Sets the GroupJPAService, mostly used in testing.
     * @param groupJPAService the group JPA service we plan initializing.
     */
    public void setGroupJPAService(GroupJPAService groupJPAService){
        if(groupJPAService == null) {
            this.groupJPAService = GroupJPAService.getInstance();
        } else {
            this.groupJPAService = groupJPAService;
        }
        this.groupJPAService.setEntityManager(null);
    }

    /**
     * Returns true iff username is valid
     * @param usernameCheck Username hashmap
     * @param user User object
     * @return
     * @throws UsernameInvalidException
     */
    public boolean isValidUsername(HashMap<String, Boolean> usernameCheck, User user) throws UsernameInvalidException {
        if(!usernameCheck.get("low") || !usernameCheck.get("cap") || !usernameCheck.get("num") || user.getUsername().length() > 20 ||
                user.getUsername().length() < 4) {
            throw new UsernameInvalidException("Username must be between 4-20 letters long, and contain one capital letter, " +
                    "one lowercase letter and one number.");
        }

        return true;
    }

    /**
     * Returns true iff password is valid
     * @param passwordCheck Password hashmap
     * @param user user object
     * @return
     * @throws PasswordInvalidException
     */
    public boolean isValidPassword(HashMap<String, Boolean> passwordCheck, User user) throws PasswordInvalidException {
        if(user.getPassword().length() < 4 || user.getPassword().length() > 20
                || !passwordCheck.get("low") || !passwordCheck.get("cap") || !passwordCheck.get("num")) {
            throw new PasswordInvalidException("Password must be between 4-20 letters long, and contain one capital letter, " +
                    "one lowercase letter and one number.");
        }

        return true;
    }

    /**
     * Add user will add a user to the database.
     * @param user being added to the database.* @return the user which was added to the database.
     */
    public boolean addUser(User user)
            throws UsernameInvalidException, PasswordInvalidException {
        HashMap<String, Boolean> usernameCheck = checkString(user.getUsername());
        HashMap<String, Boolean> passwordCheck = checkString(user.getPassword());
        if(isValidUsername(usernameCheck, user) && isValidPassword(passwordCheck, user)){
            return jpaService.createEntity(user);
        }

        return false;
    }

    /**
     * Searches for a particular user.
     * @param username the name of the user being searched
     * @return the users with the name searched for*/
    public User search(String username) throws UserNotFoundException {
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
     * @param username the username of the person we are trying to get their followers for.
     * @return a list of users following a particular user.
     */
    public List<User> getFollowers(String username) throws UserNotFoundException, ListOfUsersNotFound {
        User u = search(username);

        if(u != null) {
            return userJPAService.getFollowers(u);
        }
        else {
            return Collections.emptyList();
        }
    }

    /**
     * Get a list of followees for this user
     * @param username of the user we are trying to get the followees of.
     * @return list of users a particular user is following.
     */
    public List<User> getFollowees(String username) throws UserNotFoundException, ListOfUsersNotFound {
        User u = search(username);
        if(u != null) {
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
        userJPAService.updateUser(user);
        return (User) jpaService.getEntity("User", user.getId());
    }

    /**
     * The delete function to delete a user from the database.
     * @param user being deleted from the database.
     * @return the user which was deleted from the database.
     */
    public boolean delete(User user) throws UserNotFoundException {
        User currentUser = userJPAService.search(user.getUsername());
        try {
            return jpaService.deleteEntity(currentUser);
        }
        catch (Exception e) {
            LOGGER.info(e.getMessage());
            return false;
        }
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

    /***
     * This method is used to log the user logging off times for different groups.
     * @param userName -> The username of the user trying to log off/ exit from the chatroom.
     * @param groupUniqueCode -> The unique code of the group
     */
    public void userGroupEvent(String userName,String groupUniqueCode)   {
        final String errorMsg = "This is an unexpected situation, Generally codeflow should not reach this exception block";
        userJPAService.setEntityManager(null);
        groupJPAService.setEntityManager(null);
        try {
            User user = userJPAService.search(userName);
            Group group = groupJPAService.searchUsingCode(groupUniqueCode);
            UserChatRoomLogOffEvent userChatRoomLogOffEvent = new UserChatRoomLogOffEvent(user.getId(), group.getId(), new Date());
            userJPAService.saveOrUpdateJoinGroupEvent(userChatRoomLogOffEvent);
        } catch (UserNotFoundException | GroupNotFoundException e) {
            // There is something wrong in the control flow of the application
            // for this use case, if the flow reaches here.
            ChatLogger.error(errorMsg);
            throw new UnsupportedOperationException(errorMsg);
        }
    }
}
