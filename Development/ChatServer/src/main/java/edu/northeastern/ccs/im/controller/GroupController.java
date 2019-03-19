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

	/**
	 * addEntity method creates a group object and further
	 * calls service method to persist in db
	 * @param group
	 * @return Network response with a failure or success and a payload
	 */
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

	/**
	 * getEntity method is called by the view to retrieve a group
	 * @param groupCode
	 * @return Network response with a failure or success and a payload
	 */
	public NetworkResponse getEntity(String groupCode) {
		try {
			return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
					new PayloadImpl(CommunicationUtils.toJson(groupService.searchUsingCode(groupCode))));
		} catch (IllegalArgumentException e) {
			return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
					new PayloadImpl(null));
		}
	}

	/**
	 * updateEntity is called by the view to update a group with changed attributes
	 * @param group
	 * @return Network response with a failure or success and a payload
	 */
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

	/**
	 * deleteEntity is called by the view to delete a group
	 * @param group
	 * @return Network response with a failure or success and a payload
	 */
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


	/**
	 * searchEntity returns a network response with the unique group based on groupCode
	 * @param groupCode
	 * @return Network response with a failure or success and a payload
	 */
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

	/**
	 * searchAllGroup returns a network response with the list of groups based on groupName
	 * @param groupName
	 * @return Network response with a failure or success and a payload
	 */
	public NetworkResponse searchAllGroup(String groupName) {
		try {
			return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
					new PayloadImpl(CommunicationUtils.toJsonArray(groupService.searchUsingName(groupName))));
		} catch (IllegalArgumentException e) {
			return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
					new PayloadImpl(null));
		}
	}

	/**
	 * joinGroup method is used to add a user to the group
	 * @param group
	 * @return Network response with a failure or success and a payload
	 */
	public NetworkResponse joinGroup(Group group) {
		try {
			return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
					new PayloadImpl(CommunicationUtils.toJson(groupService.joinGroup(group))));
		} catch (IllegalArgumentException e) {
			return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
					new PayloadImpl(null));
		}
	}

	/**
	 * removeUserFromGroup removes a user from a group by passing in the groupCode and userId
	 * @param groupCode
	 * @param userId
	 * @return Network response with a failure or success and a payload
	 */
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
