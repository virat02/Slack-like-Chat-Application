package edu.northeastern.ccs.im.service.JPAService;

import java.util.List;

import javax.persistence.*;

import edu.northeastern.ccs.im.userGroup.Group;
import edu.northeastern.ccs.im.userGroup.IGroup;
import edu.northeastern.ccs.im.userGroup.Message;
import edu.northeastern.ccs.im.userGroup.User;

@Entity
@Table(name="groupcomposite")
public class GroupJPAService{
	/** The emfactory. */
	EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "PrattlePersistance" );


	public void createGroup(IGroup group) {
		EntityManager entitymanager = beginTransaction();
		entitymanager.persist(group);
		endTransaction(entitymanager);
	}

	public IGroup getGroup(String name) {	
		String queryString = "SELECT g" + "FROM Groupcomposite g WHERE g.name =" + name;
        EntityManager entityManager = beginTransaction();
        Query query = entityManager.createQuery(queryString);
        return (IGroup) query.getSingleResult();
	}

	public void updateGroup(IGroup currentGroup) {
		EntityManager entitymanager = beginTransaction();
		Group group = entitymanager.find(Group.class, currentGroup.getId());

		if (group == null) {
			throw new EntityNotFoundException("Can't find Group for ID "
					+ currentGroup.getId());
		}

		group.setName(currentGroup.getName());
		group.setCreatedOn(currentGroup.getCreatedOn());
		//add other attributes too;will this work??
		endTransaction(entitymanager);
	}

	public void addUserToGroup(int id, User user) {
		EntityManager entitymanager = beginTransaction();
		Group group = entitymanager.find(Group.class, id);
		group.addUser(user);
		endTransaction(entitymanager);
	}


	public List<Message> retrieveMessage(String name){
		IGroup group=getGroup(name);
		return group.getMsgs();
	}
	
    @SuppressWarnings("unchecked")
	public List<IGroup> searchUsingName(String groupName) {
        String queryString = "SELECT g" + "FROM Groupcomposite g WHERE g.name =" + groupName;
        EntityManager entityManager = beginTransaction();
        Query query = entityManager.createQuery(queryString);
        return (List<IGroup>) query.getResultList();
    }
    
    public IGroup searchUsingCode(String groupCode) {
        String queryString = "SELECT g" + "FROM Groupcomposite g WHERE g.groupcode =" + groupCode;
        EntityManager entityManager = beginTransaction();
        Query query = entityManager.createQuery(queryString);
        return (IGroup) query.getSingleResult();
    }


	public void deleteGroup(IGroup currentGroup) {
		EntityManager entitymanager = beginTransaction();
		Group group = entitymanager.find(Group.class, currentGroup.getId());
		entitymanager.remove(group);
		endTransaction(entitymanager);
	}
	
	private EntityManager beginTransaction() {
		EntityManager entitymanager = emfactory.createEntityManager();
		entitymanager.getTransaction().begin();
		return entitymanager;
	}
	
	private void endTransaction(EntityManager entitymanager) {
		entitymanager.getTransaction().commit();
		entitymanager.close();
	}
}
