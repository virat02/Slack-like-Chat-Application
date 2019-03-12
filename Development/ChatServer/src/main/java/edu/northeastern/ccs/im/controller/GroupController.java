package edu.northeastern.ccs.im.controller;

import java.util.List;

import edu.northeastern.ccs.im.userGroup.Group;
import edu.northeastern.ccs.im.service.GroupService;

public class GroupController implements IController<Group>{
	private GroupService groupService;

	@Override
	public Group addEntity(Group group) {
		try {
			 return groupService.create(group);
		}
		catch(IllegalArgumentException e) {
			throw new IllegalArgumentException("Cannot create a new group");
		}
		
	}

	public Group getEntity(String groupName) {
		
		return groupService.get(groupName);
	}

	@Override
	public Group updateEntity(Group group) {
		return groupService.update(group);
		
	}

	@Override
	public Group deleteEntity(Group group) {
		return groupService.delete(group);
		
	}
	
	@Override
	public Group searchEntity(String groupCode) {
		return groupService.searchUsingCode(groupCode);
	}

	
	public List<Group> searchAllGroup(String groupName) {
		return groupService.searchUsingName(groupName);
	}
	
	

}
