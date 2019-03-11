package edu.northeastern.ccs.im.controller;

import java.util.List;

import edu.northeastern.ccs.im.userGroup.IGroup;
import edu.northeastern.ccs.im.service.GroupService;

public class GroupController implements IController<IGroup>{
	private GroupService groupService;

	@Override
	public IGroup addEntity(IGroup group) {
		try {
			 return groupService.create(group);
		}
		catch(IllegalArgumentException e) {
			throw new IllegalArgumentException("Cannot create a new group");
		}
		
	}

	public IGroup getEntity(String groupName) {
		
		return groupService.get(groupName);
	}

	@Override
	public IGroup updateEntity(IGroup group) {
		return groupService.update(group);
		
	}

	@Override
	public IGroup deleteEntity(IGroup group) {
		return groupService.delete(group);
		
	}
	
	@Override
	public IGroup searchEntity(String groupCode) {
		return groupService.searchUsingCode(groupCode);
	}

	
	public List<IGroup> searchAllGroup(String groupName) {
		return groupService.searchUsingName(groupName);
	}
	
	

}
