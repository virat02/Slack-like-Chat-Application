package edu.northeastern.ccs.im.service;

import edu.northeastern.ccs.im.service.jpa_service.GroupJPAService;
import edu.northeastern.ccs.im.service.jpa_service.UserJPAService;
import edu.northeastern.ccs.im.user_group.Group;
import edu.northeastern.ccs.im.user_group.User;

import javax.persistence.NoResultException;
import java.util.List;

/**
 * GroupService class helps in delegating between the GroupController
 * and the GroupJPA service
 */
public class GroupService implements IService {

    private GroupJPAService groupJPA = new GroupJPAService();
    private UserJPAService userJPA = new UserJPAService();

    /**
     * Constructor for this class.
     */
    public GroupService() {
        groupJPA = new GroupJPAService();
    }

    /**
     * A method to set the user JPA Service for this class, makes the class more testable.
     *
     * @param userJPA for this class.
     */
    public void setUserService(UserJPAService userJPA) {
        if (userJPA == null) {
            this.userJPA = new UserJPAService();
        } else {
            this.userJPA = userJPA;
        }
        this.groupJPA.setEntityManager(null);
    }

    /**
     * A method to set the JPA Service for this class, makes the class more testable.
     *
     * @param groupJPA for this class.
     */
    public void setJPAService(GroupJPAService groupJPA) {
        if (groupJPA == null) {
            this.groupJPA = new GroupJPAService();
        } else {
            this.groupJPA = groupJPA;
        }
        this.groupJPA.setEntityManager(null);
    }

    public boolean createIfNotPresent(String groupCode) {
        try {
            groupJPA.searchUsingCode(groupCode);
        } catch (NoResultException noResultException) {
            Group group = new Group();
            group.setGroupCode(groupCode);
            groupJPA.createGroup(group);
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
    public Group create(Group group) {
        groupJPA.setEntityManager(null);
        groupJPA.createGroup(group);
        groupJPA.setEntityManager(null);
        return groupJPA.getGroup(group.getId());
    }

    /**
     * get method to retrieve the Group based on an id
     *
     * @param id int
     * @return a Group with the id retrieved from the database
     */
    public Group get(int id) {
        groupJPA.setEntityManager(null);
        return groupJPA.getGroup(id);
    }

    /**
     * update method to update the group attributes and persist in database
     *
     * @param group object
     * @return Group retrieved from database after persisting the update
     */
    public Group update(Group group) {
        groupJPA.setEntityManager(null);
        groupJPA.updateGroup((Group) group);
        groupJPA.setEntityManager(null);
        return groupJPA.getGroup((group.getId()));
    }

    /**
     * delete method to delete a group
     *
     * @param group object
     * @return Group that was deleted
     */
    public Group delete(Group group) {
        groupJPA.setEntityManager(null);
        groupJPA.deleteGroup(group);
        groupJPA.setEntityManager(null);
        return groupJPA.getGroup(group.getId());
    }

    /**
     * Search group based on groupCode to retrieve a distinct group
     *
     * @param groupCode
     * @return Group that was retrieved from database using groupCode
     */
    public Group searchUsingCode(String groupCode) {
        groupJPA.setEntityManager(null);
        return groupJPA.searchUsingCode(groupCode);
    }

    /**
     * searchUsingName method returns the list of groups with a given name
     *
     * @param groupName
     * @return list of groups
     */
    public List<Group> searchUsingName(String groupName) {
        groupJPA.setEntityManager(null);
        return groupJPA.searchUsingName(groupName);
    }

    /**
     * joinGroup methood is to add a useer to a group
     *
     * @param group that contains the user to be added
     * @return updated group with the user added to it
     */
    public Group joinGroup(Group group) {
        Group retrievedGroup = searchUsingCode(group.getGroupCode());
        List<User> userOfGroup = group.getUsers();
        userJPA.setEntityManager(null);
        User retirevedUser = userJPA.getUser(userOfGroup.get(0).getId());
        retrievedGroup.addUser(retirevedUser);
        groupJPA.setEntityManager(null);
        groupJPA.updateGroup(retrievedGroup);
        groupJPA.setEntityManager(null);
        return groupJPA.getGroup(retrievedGroup.getId());
    }

    /**
     * Remove user from the given group based on the userid and groupcode
     *
     * @param groupCode
     * @param userId
     * @return the updated group after the user has been removed from it
     */
    public Group removeUserFromGroup(String groupCode, int userId) {
        Group retrievedGroup = searchUsingCode(groupCode);
        groupJPA.setEntityManager(null);
        groupJPA.removeUserFromGroup(retrievedGroup, userId);
        groupJPA.setEntityManager(null);
        return groupJPA.getGroup(retrievedGroup.getId());
    }

}
