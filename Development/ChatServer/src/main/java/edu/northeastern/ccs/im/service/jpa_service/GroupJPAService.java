package edu.northeastern.ccs.im.service.jpa_service;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.*;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.customexceptions.GroupNotDeletedException;
import edu.northeastern.ccs.im.customexceptions.GroupNotFoundException;
import edu.northeastern.ccs.im.customexceptions.GroupNotPersistedException;
import edu.northeastern.ccs.im.customexceptions.UserNotFoundException;
import edu.northeastern.ccs.im.service.EntityManagerUtil;
import edu.northeastern.ccs.im.user_group.Group;
import edu.northeastern.ccs.im.user_group.User;

/**
 * GroupJPAService is the class that contains methods to interact with the db
 */
public class GroupJPAService{

	private static final Logger LOGGER = Logger.getLogger(GroupJPAService.class.getName());
	private static final String ERROR_MESSAGE = "Can't find Group for ID: ";

	private UserJPAService userJPA = new UserJPAService();

	//The entity manager for this class.
	private EntityManager entityManager;

	private EntityManagerUtil entityManagerUtil;

	/**
	 * Constructor for UserJPAService to setup the EntityManagerUtil
	 */
	public GroupJPAService() {
		entityManagerUtil = new EntityManagerUtil();
	}

	/**
	 * A function made to setup the entity manager util for this class to make the class more testable.
	 * @param entityManagerUtil The entity manager for this class.
	 */
	public void setEntityManagerUtil(EntityManagerUtil entityManagerUtil) {
		this.entityManagerUtil = entityManagerUtil;
	}

	/**
	 * A function made to setup the entity manager for this class to make the class more testable.
	 * @param entityManager The entity manager for this class.
	 */
	public void setEntityManager(EntityManager entityManager) {
		if(entityManager == null) {
			EntityManagerFactory emFactory = Persistence.createEntityManagerFactory( "PrattlePersistance" );
			this.entityManager = emFactory.createEntityManager();
		} else {
			this.entityManager = entityManager;
		}
	}

	/**
	 * Set the userJPAService
	 * @param userJPA
	 */
	public void setUserJPAService(UserJPAService userJPA) {
		this.userJPA = userJPA;
	}

    /**
     * A method to begin the transaction.
     */
	private void beginTransaction(EntityManager entityManager) {
		entityManager.getTransaction().begin();
	}

    /**
     * A private method that'll end the transaction.
     */
	private void endTransaction(EntityManager entityManager) {
		entityManager.getTransaction().commit();
		entityManager.close();
	}

    /**
     * Creates a group and persists in database
     * @param group
     * @return id of the newly added group
     */
	public int createGroup(Group group) throws GroupNotPersistedException {
		try {
			EntityManager entityManager = entityManagerUtil.getEntityManager();
			beginTransaction(entityManager);
			entityManager.persist(group);
			entityManager.flush();
			endTransaction(entityManager);
			LOGGER.info("Successfully created a group with group unique key: "+group.getGroupCode());
			return group.getId();
		}
		catch (Exception e) {
			LOGGER.info("Could not create the group!");
			throw new GroupNotPersistedException("Could not create the group");
		}

	}

    /**
     * getGroup returns the group from the database fetched using id
     * @param id
     * @return a group
     */
	public Group getGroup(int id) throws GroupNotFoundException {
		try {
			EntityManager entityManager = entityManagerUtil.getEntityManager();
			beginTransaction(entityManager);
			Group fetchedGroup = entityManager.find(Group.class, id);
			endTransaction(entityManager);
			return fetchedGroup;
		}
		catch (Exception e) {
			LOGGER.info("Could not find a Group with group id: "+id);
			throw new GroupNotFoundException("Could not find a Group with group id: "+id);
		}
	}

    /**
     * updateGroup updates a given group with the changed attribute values
     * @param currentGroup
     */
	public Boolean updateGroup(Group currentGroup) throws GroupNotFoundException{
		EntityManager entityManager = entityManagerUtil.getEntityManager();
		beginTransaction(entityManager);
		Group group = entityManager.find(Group.class, currentGroup.getId());

		if (group == null) {
			LOGGER.info("Could not update group since group not found");
			throw new GroupNotFoundException( ERROR_MESSAGE + currentGroup.getId());
		}

		group.setFollowees(currentGroup.getFollowees());
		group.setFollowers(currentGroup.getFollowers());
		group.setGroupCode(currentGroup.getGroupCode());
		group.setGroups(currentGroup.getGroups());
		group.setModerators(currentGroup.getModerators());
		group.setUsers(currentGroup.getUsers());
		group.setName(currentGroup.getName());
		group.setCreatedOn(currentGroup.getCreatedOn());
		group.setMsgs(currentGroup.getMsgs());
		group.setGroupPassword(currentGroup.getGroupPassword());
		endTransaction(entityManager);

		if(group.toString().equals(currentGroup.toString())){
			LOGGER.info("Updated Group : "+currentGroup.getId());
			return true;
		}
		else {
			LOGGER.info("Could not update group : "+currentGroup.getId());
			return false;
		}
	}

    /**
     * addUserToGroup adds a user to the group and persists
     * their relationship in the join table
     * @param id
     * @param user
     */
	public void addUserToGroup(int id, User user) throws GroupNotFoundException{
		EntityManager entityManager = entityManagerUtil.getEntityManager();
		beginTransaction(entityManager);
		Group group = entityManager.find(Group.class, id);

		if (group == null) {
			throw new GroupNotFoundException(ERROR_MESSAGE + id);
		}

		group.addUser(user);
		endTransaction(entityManager);
	}

	/**
	 * Updates the givrn group with the sub group
	 * @param currentGroup the current that is being updated
	 * @return boolean value if the update is successful or not
	 * @throws GroupNotFoundException thrown when the group is not found
	 */
	public Boolean addSubGroupToGroup(Group currentGroup) throws GroupNotFoundException{
		EntityManager entityManager = entityManagerUtil.getEntityManager();
		beginTransaction(entityManager);
		Group group = entityManager.find(Group.class, currentGroup.getId());

		if (group == null) {
			LOGGER.info("Could not update group since group not found");
			throw new GroupNotFoundException("Can't find Group for ID: " + currentGroup.getId());
		}

		group.setGroups(currentGroup.getGroups());
		endTransaction(entityManager);

		if(group.toString().equals(currentGroup.toString())){
			LOGGER.info("Updated Group with sub group : "+currentGroup.getId());
			return true;
		}
		else {
			LOGGER.info("Could not update group with sub group: "+currentGroup.getId());
			return false;
		}
	}


    /**
     * searchUsingName method returns the list of groups with a given name
     * @param groupName
     * @return a list of groups
     */
	public List<Group> searchUsingName(String groupName) throws GroupNotFoundException {
		try {
			EntityManager entityManager = entityManagerUtil.getEntityManager();
			String queryString = "SELECT g FROM Group g WHERE g.name = '" + groupName+"' AND g.isDeleted = false";
			beginTransaction(entityManager);
			TypedQuery<Group> query = entityManager.createQuery(queryString,Group.class);
			List<Group> searchedGroups = query.getResultList();
			endTransaction(entityManager);
			return searchedGroups;
		}
		catch (Exception e) {
			LOGGER.info("Can't find Group with name: " + groupName);
			throw new GroupNotFoundException(ERROR_MESSAGE + groupName);
		}

	}

    /**
     * searchUsingCode based on groupCode to retrieve a distinct group
     * @param groupCode
     * @return a group
     */
	public Group searchUsingCode(String groupCode) throws GroupNotFoundException {
		try {
			EntityManager entityManager = entityManagerUtil.getEntityManager();
			String queryString = "SELECT g FROM Group g WHERE g.groupCode = '" + groupCode + "' AND g.isDeleted = false";
			beginTransaction(entityManager);
			TypedQuery<Group> query = entityManager.createQuery(queryString, Group.class);
			Group group = query.getSingleResult();
			endTransaction(entityManager);
			return group;
		}
		catch (Exception e) {
			LOGGER.info("Can't find Group with code: " + groupCode);
			throw new GroupNotFoundException("Can't find Group with code: " + groupCode);
		}
	}

    /**
     * deletes a group from database based on the id of the group
     * @param currentGroup
     */
	public void deleteGroup(Group currentGroup) throws GroupNotDeletedException {
		try {
			EntityManager entityManager = entityManagerUtil.getEntityManager();
			beginTransaction(entityManager);
			Group group = entityManager.find(Group.class, currentGroup.getId());
			group.setIsDeleted(true);
			LOGGER.info("Deleted group with group unique key: "+currentGroup.getGroupCode());
			endTransaction(entityManager);
		}
		catch(Exception e) {
			LOGGER.info("Can't delete Group with code: " + currentGroup.getGroupCode());
			throw new GroupNotDeletedException("Could not delete group with code: "+currentGroup.getGroupCode());
		}

	}

    /**
     * removeUserFromGroup removes the relationship between group and user in the join table
     * @param currentGroup
     * @param username
     * @return 1 for success , 0 for failure
     */
	public int removeUserFromGroup(Group currentGroup, String username) throws UserNotFoundException{
		EntityManager entityManager = entityManagerUtil.getEntityManager();
		beginTransaction(entityManager);
		User u = userJPA.search(username);
		boolean isModerator=false;
		int userId = u.getId();
		List<User> moderators = currentGroup.getModerators();
		for(User user : moderators){
			if(user.getId()==u.getId()){
				isModerator=true;
				break;
			}
		}
		if(!isModerator) {
			int result = entityManager.createNativeQuery("DELETE FROM basegroup_user WHERE user_id=" + userId +
					" AND group_id=" + currentGroup.getId()).executeUpdate();
			int result2 = entityManager.createNativeQuery("DELETE FROM user_basegroup WHERE user_id=" + userId +
					" AND groups_id=" + currentGroup.getId()).executeUpdate();
			endTransaction(entityManager);
			if(result==1 && result2==1) {
				ChatLogger.info("Successfully removed User: "+username+" from Group: "+currentGroup.getGroupCode());
				return 1;
			}
			else{
				ChatLogger.info("Could not remove User: "+username+" from Group: "+currentGroup.getGroupCode());
				return 0;
			}
		}
		else{
			throw new IllegalArgumentException("Cannot remove moderator of a group");
		}
	}
}
