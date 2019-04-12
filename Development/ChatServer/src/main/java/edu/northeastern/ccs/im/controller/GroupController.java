package edu.northeastern.ccs.im.controller;

import edu.northeastern.ccs.im.communication.CommunicationUtils;
import edu.northeastern.ccs.im.communication.NetworkResponse;
import edu.northeastern.ccs.im.communication.NetworkResponseImpl;
import edu.northeastern.ccs.im.communication.PayloadImpl;
import edu.northeastern.ccs.im.customexceptions.GroupNotDeletedException;
import edu.northeastern.ccs.im.customexceptions.GroupNotFoundException;
import edu.northeastern.ccs.im.customexceptions.GroupNotPersistedException;
import edu.northeastern.ccs.im.customexceptions.UserNotFoundException;
import edu.northeastern.ccs.im.service.GroupService;
import edu.northeastern.ccs.im.user_group.Group;

public class GroupController implements IController<Group>{

	private static final String GROUP_NOT_PERSISTED_JSON = "{\"message\" : \"Sorry, could not create the group!\"}";
	private static final String GROUP_NOT_FOUND_JSON = "{\"message\" : \"The group you are trying to find does not exist!\"}";
	private static final String USER_NOT_FOUND_JSON = "{\"message\" : \"The user you are trying to find does not exist!\"}";
	private static final String GROUP_NOT_DELETED_JSON = "{\"message\" : \"Sorry, could not delete the group!\"}";
	private static final String MODERATOR_CANNOT_BE_DELETED = "{\"message\" : \"Given user is a moderator. Moderator cannot be deleted!\"}";


	private GroupService groupService;

	private static final GroupController groupControllerInstance = new GroupController();

	/**
	 * Constructor for the group controller
	 */
	private GroupController(){
		groupService = GroupService.getInstance();
	}

	/**
	 * Singleton pattern for group controller
	 * @return a singleton instance
	 */
	public static GroupController getInstance(){
		return groupControllerInstance;
	}

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
		} catch (GroupNotFoundException e) {
			return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
					new PayloadImpl(GROUP_NOT_FOUND_JSON));
		} catch (GroupNotPersistedException e) {
			return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
					new PayloadImpl(GROUP_NOT_PERSISTED_JSON));
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
		} catch (GroupNotFoundException e) {
			return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
					new PayloadImpl(GROUP_NOT_FOUND_JSON));
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
		} catch (GroupNotFoundException e) {
			return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
					new PayloadImpl(GROUP_NOT_FOUND_JSON));
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
		} catch (GroupNotDeletedException e) {
			return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
					new PayloadImpl(GROUP_NOT_DELETED_JSON));
		} catch (GroupNotFoundException e) {
			return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
					new PayloadImpl(GROUP_NOT_FOUND_JSON));
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
		} catch (GroupNotFoundException e) {
			return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
					new PayloadImpl(GROUP_NOT_FOUND_JSON));
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
		} catch (GroupNotFoundException e) {
			return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
					new PayloadImpl(GROUP_NOT_FOUND_JSON));
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
		} catch (GroupNotFoundException e) {
			return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
					new PayloadImpl(GROUP_NOT_FOUND_JSON));
		} catch (UserNotFoundException e) {
			return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
					new PayloadImpl(USER_NOT_FOUND_JSON));
		}
	}

	/**
	 * removeUserFromGroup removes a user from a group by passing in the groupCode and username
	 * @param groupCode
	 * @param username
	 * @return Network response with a failure or success and a payload
	 */
	public NetworkResponse removeUserFromGroup(String groupCode, String username) {
		try {
			return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
					new PayloadImpl(CommunicationUtils.toJson(groupService.removeUserFromGroup(groupCode, username))));
		} catch (GroupNotFoundException e) {
			return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
					new PayloadImpl(GROUP_NOT_FOUND_JSON));
		} catch (UserNotFoundException e) {
			return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
					new PayloadImpl(USER_NOT_FOUND_JSON));
		} catch (IllegalArgumentException e){
			return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED,
					new PayloadImpl(MODERATOR_CANNOT_BE_DELETED));
		}
	}
}
