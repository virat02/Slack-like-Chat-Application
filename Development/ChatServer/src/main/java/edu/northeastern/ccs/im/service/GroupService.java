package edu.northeastern.ccs.im.service;

import edu.northeastern.ccs.im.userGroup.IGroup;

import java.util.List;

import edu.northeastern.ccs.im.service.JPAService.GroupJPAService;

public class GroupService implements IService{
	
	private GroupJPAService groupJPA = new GroupJPAService();
	
	public IGroup create(IGroup group) {
		groupJPA.createGroup(group);
		return groupJPA.getGroup(group.getName());
	}
	
	public IGroup get(String name) {
		return groupJPA.getGroup(name);
	}
	
	public IGroup update(IGroup group) {
		groupJPA.updateGroup(group);
		return groupJPA.getGroup(group.getName());
	}
	
	public IGroup delete(IGroup group) {
		groupJPA.deleteGroup(group);
		return groupJPA.getGroup(group.getName());
	}
	
	public IGroup searchUsingCode(String groupCode){
		return groupJPA.searchUsingCode(groupCode);
	}
	
	public List<IGroup> searchUsingName(String groupName){
		return groupJPA.searchUsingName(groupName);
	}

}
