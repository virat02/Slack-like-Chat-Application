package edu.northeastern.ccs.im.service;

import edu.northeastern.ccs.im.service.JPAService.GroupJPAService;
import edu.northeastern.ccs.im.service.JPAService.UserJPAService;
import edu.northeastern.ccs.im.userGroup.Group;
import edu.northeastern.ccs.im.userGroup.User;

import javax.persistence.NoResultException;
import java.util.List;

public class GroupService implements IService{
	
	private GroupJPAService groupJPA = new GroupJPAService();
	private UserJPAService userJPA = new UserJPAService();
	
	public Group create(Group group) {
		groupJPA.createGroup(group);
		return groupJPA.getGroup(group.getId());
	}
	
	public Group get(int id) {
		return groupJPA.getGroup(id);
	}
	
	public Group update(Group group) {
		groupJPA.updateGroup(group);
		return groupJPA.getGroup(group.getId());
	}
	
	public Group delete(Group group) {
		groupJPA.deleteGroup(group);
		return groupJPA.getGroup(group.getId());
	}
	
	public Group searchUsingCode(String groupCode){
		return groupJPA.searchUsingCode(groupCode);
	}

	public boolean createIfNotPresent(String groupCode)	{
		try	{
			groupJPA.searchUsingCode(groupCode);
		} catch (NoResultException noResultException)	{
			Group group = new Group();
			group.setGroupCode(groupCode);
			groupJPA.createGroup(group);
		}

		return true;
	}
	
	public List<Group> searchUsingName(String groupName){
		return groupJPA.searchUsingName(groupName);
	}
	
	public Group joinGroup(Group group) {
		Group retrievedGroup = searchUsingCode(group.getGroupCode());
		List<User> userOfGroup = group.getUsers();
		User retirevedUser = userJPA.getUser(userOfGroup.get(0).getId());
		retrievedGroup.addUser(retirevedUser);
		groupJPA.updateGroup(retrievedGroup);
		return groupJPA.getGroup(retrievedGroup.getId());
	}

}
