package edu.northeastern.ccs.im.service.jpa_service;

import java.util.List;
<<<<<<< HEAD
=======
import java.util.logging.Logger;
>>>>>>> cd5e8a874b3c49e1ecd457c6c34f396f4d01e570

import javax.persistence.*;

import edu.northeastern.ccs.im.user_group.Group;
import edu.northeastern.ccs.im.user_group.User;

/**
 * GroupJPAService is the class that contains methods to interact with the db
 */
public class GroupJPAService{

	/** The emfactory. */
	//The entity manager for this class.
	private EntityManager entityManager;

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
     * A method to begin the transaction.
     */
	private void beginTransaction() {
		entityManager.getTransaction().begin();
	}

    /**
     * A private method that'll end the transaction.
     */
	private void endTransaction() {
		entityManager.getTransaction().commit();
		entityManager.close();
	}

    /**
     * Creates a group and persists in database
     * @param group
     * @return id of the newly added group
     */
	public int createGroup(Group group) {
		beginTransaction();
		entityManager.persist(group);
		entityManager.flush();
		int id= group.getId();
		endTransaction();
		return id;
	}

    /**
     * getGroup returns the group from the database fetched using id
     * @param id
     * @return a group
     */
	public Group getGroup(int id) {
		beginTransaction();
		return entityManager.find(Group.class, id);
	}

    /**
     * updateGroup updates a given group with the changed attribute values
     * @param currentGroup
     */
	public void updateGroup(Group currentGroup) {
		beginTransaction();
		Group group = entityManager.find(Group.class, currentGroup.getId());

		if (group == null) {
			throw new EntityNotFoundException("Can't find Group for ID "
					+ currentGroup.getId());
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
		endTransaction();
	}

    /**
     * addUserToGroup adds a user to the group and persists
     * their relationship in the join table
     * @param id
     * @param user
     */
	public void addUserToGroup(int id, User user) {
		beginTransaction();
		Group group = entityManager.find(Group.class, id);
		group.addUser(user);
		endTransaction();
	}


    /**
     * searchUsingName method returns the list of groups with a given name
     * @param groupName
     * @return a list of groups
     */
	public List<Group> searchUsingName(String groupName) {
<<<<<<< HEAD
		String queryString = "SELECT g FROM Group g WHERE g.name = '" + groupName+"'";
=======
		String queryString = "SELECT g FROM `Group` g WHERE g.name = '" + groupName+"'";
>>>>>>> cd5e8a874b3c49e1ecd457c6c34f396f4d01e570

		beginTransaction();
		TypedQuery<Group> query = entityManager.createQuery(queryString,Group.class);
		return query.getResultList();
	}

    /**
     * searchUsingCode based on groupCode to retrieve a distinct group
     * @param groupCode
     * @return a group
     */
	public Group searchUsingCode(String groupCode) {
<<<<<<< HEAD
		String queryString = "SELECT g FROM Group g WHERE g.groupCode = '" + groupCode + "'";
=======
		String queryString = "SELECT g FROM `Group` g WHERE g.groupCode = '" + groupCode + "'";
>>>>>>> cd5e8a874b3c49e1ecd457c6c34f396f4d01e570
		beginTransaction();
		Query query = entityManager.createQuery(queryString);
		return (Group) query.getSingleResult();
	}


    /**
     * deletes a group from database based on the id of the group
     * @param currentGroup
     */
	public void deleteGroup(Group currentGroup) {
		Group retrievedGroup = searchUsingCode(currentGroup.getGroupCode());
		beginTransaction();
		Group group = entityManager.find(Group.class, retrievedGroup.getId());
		entityManager.remove(group);
		endTransaction();
	}

    /**
     * removeUserFromGroup removes the relationship between group and user in the join table
     * @param currentGroup
     * @param userId
     * @return 1 for success , 0 for failure
     */
	public int removeUserFromGroup(Group currentGroup, int userId) {
		beginTransaction();
		int result = entityManager.createNativeQuery("DELETE FROM basegroup_user WHERE user_id="+ userId+
				" AND group_id="+currentGroup.getId()).executeUpdate();
		int result2 = entityManager.createNativeQuery("DELETE FROM user_basegroup WHERE user_id="+ userId+
				" AND groups_id="+currentGroup.getId()).executeUpdate();
		endTransaction();
		if(result==1 && result2==1)
			return 1;
		else
			return 0;
	}

}
