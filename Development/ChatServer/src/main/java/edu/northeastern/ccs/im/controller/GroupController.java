package edu.northeastern.ccs.im.controller;

import edu.northeastern.ccs.im.communication.CommunicationUtils;
import edu.northeastern.ccs.im.communication.NetworkResponse;
import edu.northeastern.ccs.im.communication.NetworkResponseImpl;
import edu.northeastern.ccs.im.communication.PayloadImpl;
import edu.northeastern.ccs.im.service.GroupService;
import edu.northeastern.ccs.im.user_group.Group;

public class GroupController implements IController<Group>{
	private GroupService groupService = new GroupService();

	/**
	 * Sets the user service for the controller.
	 * @param groupService the user service the controller will be using to load on the payload.
	 */
	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

	@Override
	public NetworkResponse addEntity(Group group) {
		try {
			return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
					new PayloadImpl(CommunicationUtils.toJson(groupService.create(group))));
		} catch (IllegalArgumentException e) {
			return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
					new PayloadImpl(null));
		}

	}

	public NetworkResponse getEntity(String groupCode) {
		try {
			return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
					new PayloadImpl(CommunicationUtils.toJson(groupService.searchUsingCode(groupCode))));
		} catch (IllegalArgumentException e) {
			return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
					new PayloadImpl(null));
		}
	}

	@Override
	public NetworkResponse updateEntity(Group group) {
		try {
			return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
					new PayloadImpl(CommunicationUtils.toJson(groupService.update(group))));
		} catch (IllegalArgumentException e) {
			return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
					new PayloadImpl(null));
		}

	}

	@Override
	public NetworkResponse deleteEntity(Group group) {
		try {
			return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
					new PayloadImpl(CommunicationUtils.toJson(groupService.delete(group))));
		} catch (IllegalArgumentException e) {
			return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
					new PayloadImpl(null));
		}

	}

	@Override
	public NetworkResponse searchEntity(String groupCode) {
		try {
			return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
					new PayloadImpl(CommunicationUtils.toJson(groupService.searchUsingCode(groupCode))));
		} catch (Exception e) {
			return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
					new PayloadImpl(null));
		}
	}


	public NetworkResponse searchAllGroup(String groupName) {
		try {
			return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
					new PayloadImpl(CommunicationUtils.toJsonArray(groupService.searchUsingName(groupName))));
		} catch (IllegalArgumentException e) {
			return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
					new PayloadImpl(null));
		}
	}

	public NetworkResponse joinGroup(Group group) {
		try {
			return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
					new PayloadImpl(CommunicationUtils.toJson(groupService.joinGroup(group))));
		} catch (IllegalArgumentException e) {
			return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
					new PayloadImpl(null));
		}
	}

	public NetworkResponse removeUserFromGroup(String groupCode, int userId) {
		try {
			return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
					new PayloadImpl(CommunicationUtils.toJson(groupService.removeUserFromGroup(groupCode, userId))));
		} catch (IllegalArgumentException e) {
			return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
					new PayloadImpl(null));
		}
	}



}
