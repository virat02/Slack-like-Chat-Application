package edu.northeastern.ccs.im.service;

import edu.northeastern.ccs.im.customexceptions.*;
import edu.northeastern.ccs.im.service.jpa_service.AllJPAService;
import edu.northeastern.ccs.im.service.jpa_service.GroupJPAService;
import edu.northeastern.ccs.im.service.jpa_service.UserJPAService;
import edu.northeastern.ccs.im.user_group.Group;
import edu.northeastern.ccs.im.user_group.User;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.logging.Logger;

/**
 * GroupService class helps in delegating between the GroupController
 * and the GroupJPA service
 */
public class GroupService implements IService {

    private static final Logger LOGGER = Logger.getLogger(GroupService.class.getName());

    private GroupJPAService groupJPA;
    private UserJPAService userJPA;
    private AllJPAService jpaService;

    private static final GroupService groupServiceinstance = new GroupService();

    /**
     * Constructor for this class.
     */
    private GroupService() {
        groupJPA = GroupJPAService.getInstance();
        userJPA = UserJPAService.getInstance();
        jpaService = AllJPAService.getInstance();
    }

    public static GroupService getInstance(){
        return groupServiceinstance;
    }

    /**
     * A method to set the user JPA Service for this class, makes the class more testable.
     *
     * @param userJPA for this class.
     */
    public void setUserService(UserJPAService userJPA) {
        if (userJPA == null) {
            this.userJPA = UserJPAService.getInstance();
        } else {
            this.userJPA = userJPA;
        }
        this.userJPA.setEntityManager(null);
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
     * A method to set the JPA Service for this class, makes the class more testable.
     *
     * @param groupJPA for this class.
     */
    public void setJPAService(GroupJPAService groupJPA) {
        if (groupJPA == null) {
            this.groupJPA = GroupJPAService.getInstance();
        } else {
            this.groupJPA = groupJPA;
        }
        this.groupJPA.setEntityManager(null);
    }

    /**
     * Create a group iff the group does not already exist
     * @param groupCode
     * @return
     */
    public boolean createIfNotPresent(String groupCode, String username, boolean flag)
            throws GroupNotFoundException, UserNotFoundException, UserNotPresentInTheGroup {

        //Check if request for private group
        if(flag) {
            return createPrivateGroupIfNotPresent(groupCode, username);
        }

        //If request for public group
        return addUserToPublicGroupIfNotPresent(groupCode, username);
    }

    /**
     * Creates a private group iff the group is not already present, else creates a new group
     *
     * @param groupUniqueKey the unique key of the group searched for.
     * @param username       The username which is trying to initiate a private conversation
     * @return Boolean value indicating the outcome of the operations
     * @throws UserNotFoundException      user cannot be found, the user with whom conversation is
     *                                    being initiated.
     */
    private boolean createPrivateGroupIfNotPresent(String groupUniqueKey, String username)
            throws UserNotFoundException {

        try {
            searchUsingCode(groupUniqueKey);
            LOGGER.info("Private group already exists!");
            return true;
        } catch (GroupNotFoundException e) {
            LOGGER.info("Private group has not been created.Trying to create one");
        }

        String[] users = groupUniqueKey.split("_");
        String userToSearch;

        //Fetch the user to be searched if already present in the group
        if (users[0].equals(username)) {
            userToSearch = users[1];
        } else {
            userToSearch = users[0];
        }

        User u1 = userJPA.search(userToSearch);
        User u2 = userJPA.search(username);

        Group group = new Group();
        group.setGroupCode(groupUniqueKey);

        //Make both users : "username" and "userToSearch" the moderator of this group
        group.addModerator(u1);
        group.addModerator(u2);

        //Update the DB
        return create(group);
    }

    /**
     * Adds a user to a group iff the user is not already a part of the group
     *
     * @param groupUniqueKey
     * @param username
     * @return
     * @throws GroupNotFoundException
     * @throws UserNotFoundException
     * @throws UserNotPresentInTheGroup
     */
    private boolean addUserToPublicGroupIfNotPresent(String groupUniqueKey, String username)
            throws GroupNotFoundException, UserNotFoundException, UserNotPresentInTheGroup {

        Group g = groupJPA.searchUsingCode(groupUniqueKey);

        if (!(g.getUsers().contains(userJPA.search(username)))) {
            throw new UserNotPresentInTheGroup("This user is not part of the group!");
        }

        return true;
    }

    /**
     * create method is used to create a group object and help
     * persist in the database
     *
     * @param group object
     * @return a Group with the id after being persisted in the group
     */
    public boolean create(Group group) {
        try{
            return jpaService.createEntity(group);
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * update method to update the group attributes and persist in database
     *
     * @param group object
     * @return Group retrieved from database after persisting the update
     */
    public Group update(Group group) throws GroupNotFoundException{
        Group groupUpdating = groupJPA.searchUsingCode(group.getGroupCode());

        if (groupUpdating.getGroups().size() != group.getGroups().size()) {
            groupJPA.addSubGroupToGroup(group);
        }
        else {
            groupJPA.updateGroup(group);
        }
        return (Group) jpaService.getEntity("Group", group.getId());
    }

    /**
     * delete method to delete a group
     *
     * @param group object
     * @return Group that was deleted
     */
    public boolean delete(Group group) throws GroupNotFoundException {
        Group currentGroup = groupJPA.searchUsingCode(group.getGroupCode());
        try {
            return jpaService.deleteEntity(currentGroup);
        }
        catch (Exception e) {
            LOGGER.info(e.getMessage());
            return false;
        }

    }

    /**
     * Search group based on groupCode to retrieve a distinct group
     *
     * @param groupCode
     * @return Group that was retrieved from database using groupCode
     */
    public Group searchUsingCode(String groupCode) throws GroupNotFoundException {
        return groupJPA.searchUsingCode(groupCode);
    }

    /**
     * searchUsingName method returns the list of groups with a given name
     *
     * @param groupName
     * @return list of groups
     */
    public List<Group> searchUsingName(String groupName) throws GroupNotFoundException {
        return groupJPA.searchUsingName(groupName);
    }

    /**
     * joinGroup method is to add a user to a group
     *
     * @param group that contains the user to be added
     * @return updated group with the user added to it
     */
    public Group joinGroup(Group group) throws GroupNotFoundException, UserNotFoundException {
        Group retrievedGroup = searchUsingCode(group.getGroupCode());
        List<User> userOfGroup = group.getUsers();
        try {
            User retrievedUser = (User) jpaService.getEntity("User", userOfGroup.get(0).getId());
            retrievedGroup.addUser(retrievedUser);
            groupJPA.updateGroup(retrievedGroup);
            return (Group) jpaService.getEntity("Group", retrievedGroup.getId());
        }
        catch (NoResultException e) {
            throw new UserNotFoundException("Could not find the user :"+userOfGroup.get(0).getId());
        }

    }

    /**
     * Remove user from the given group based on the userid and groupcode
     *
     * @param groupCode
     * @param username
     * @return the updated group after the user has been removed from it
     */
    public Group removeUserFromGroup(String groupCode, String username)
            throws GroupNotFoundException, UserNotFoundException{
        Group retrievedGroup = searchUsingCode(groupCode);
        groupJPA.removeUserFromGroup(retrievedGroup, username);
        return (Group) jpaService.getEntity("Group" , retrievedGroup.getId());
    }

}