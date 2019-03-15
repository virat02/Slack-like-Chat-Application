package edu.northeastern.ccs.im.service.JPAService;

import java.util.List;

import javax.persistence.*;

import edu.northeastern.ccs.im.userGroup.Group;
import edu.northeastern.ccs.im.userGroup.IGroup;
import edu.northeastern.ccs.im.userGroup.Message;
import edu.northeastern.ccs.im.userGroup.User;

public class GroupJPAService{
	/** The emfactory. */
	
	EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "PrattlePersistance" );
	private EntityManager beginTransaction() {
		EntityManager entitymanager = emfactory.createEntityManager();
		entitymanager.getTransaction().begin();
		return entitymanager;
	}
	
	private void endTransaction(EntityManager entitymanager) {
		entitymanager.getTransaction().commit();
		entitymanager.close();
	}

	public void createGroup(Group group) {
		EntityManager entitymanager = emfactory.createEntityManager();
		entitymanager.getTransaction().begin();
		entitymanager.persist(group);
		entitymanager.getTransaction().commit();
		entitymanager.close();
	}

	public Group getGroup(int id) {
		//String queryString = "SELECT g " + "FROM prattle_server.basegroup g WHERE g.name = " + name;
		EntityManager entityManager = beginTransaction();
		Group group = entityManager.find(Group.class, id);
        return group;
	}

	public void updateGroup(Group currentGroup) {
		EntityManager entitymanager = beginTransaction();
		Group group = entitymanager.find(Group.class, currentGroup.getId());

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
		endTransaction(entitymanager);
	}

	public void addUserToGroup(int id, User user) {
		EntityManager entitymanager = beginTransaction();
		Group group = entitymanager.find(Group.class, id);
		group.addUser(user);
		endTransaction(entitymanager);
	}


	public List<Message> retrieveMessage(String name){
//		Group group=getGroup(name);
//		return group.getMsgs();
		return null;
	}
	
	public List<Group> searchUsingName(String groupName) {
        String queryString = "SELECT g FROM Group g WHERE g.name = '" + groupName+"'";
        
        EntityManager entityManager = beginTransaction();
        TypedQuery<Group> query = entityManager.createQuery(queryString,Group.class);
        return query.getResultList();
    }
    
    public Group searchUsingCode(String groupCode) {
				String queryString = "SELECT g FROM Group g WHERE g.groupCode = '" + groupCode + "'";
				EntityManager entityManager = beginTransaction();
				Query query = entityManager.createQuery(queryString);
				return (Group) query.getSingleResult();
    }


	public void deleteGroup(Group currentGroup) {
		Group retrievedGroup = searchUsingCode(currentGroup.getGroupCode());
		EntityManager entitymanager = beginTransaction();
		Group group = entitymanager.find(Group.class, retrievedGroup.getId());
		entitymanager.remove(group);
		endTransaction(entitymanager);
	}
	
}
