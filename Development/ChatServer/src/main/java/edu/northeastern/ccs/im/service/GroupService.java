package edu.northeastern.ccs.im.service;

import edu.northeastern.ccs.im.service.JPAService.GroupJPAService;
import edu.northeastern.ccs.im.userGroup.Group;

import java.util.List;

public class GroupService implements IService{
	
	private GroupJPAService groupJPA = new GroupJPAService();
	
	public Group create(Group group) {
		groupJPA.createGroup(group);
		return groupJPA.getGroup(group.getName());
	}
	
	public Group get(String name) {
		return groupJPA.getGroup(name);
	}
	
	public Group update(Group group) {
		groupJPA.updateGroup(group);
		return groupJPA.getGroup(group.getName());
	}
	
	public Group delete(Group group) {
		groupJPA.deleteGroup(group);
		return groupJPA.getGroup(group.getName());
	}
	
	public Group searchUsingCode(String groupCode){
		return groupJPA.searchUsingCode(groupCode);
	}
	
	public List<Group> searchUsingName(String groupName){
		return groupJPA.searchUsingName(groupName);
	}

}
