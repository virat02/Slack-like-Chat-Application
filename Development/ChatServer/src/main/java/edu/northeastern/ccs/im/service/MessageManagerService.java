package edu.northeastern.ccs.im.service;

import edu.northeastern.ccs.im.customexceptions.GroupNotFoundException;
import edu.northeastern.ccs.im.customexceptions.GroupNotPersistedException;
import edu.northeastern.ccs.im.customexceptions.UserNotPresentInTheGroup;
import edu.northeastern.ccs.im.customexceptions.UserNotFoundException;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to manage all the message services
 */
public class MessageManagerService {

    //Maps the messageBroadcastService to the unique group identifier
    private Map<String, BroadCastService> hmap = new HashMap<>();

    // static variable instance of type MessageManagerService
    private static MessageManagerService instance = null;

    private GroupService groupService = new GroupService();

    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }
    /**
     * Singleton class MessageManagerService
     *
     * @return the instance of MessageManagerService class
     */
    public static MessageManagerService getInstance() {

        if (instance == null)
            instance = new MessageManagerService();

        return instance;
    }

    /***
     *
     * Gets a particular message service based on client request
     * @param groupUniqueKey - The unique code for the group
     * @param username - The user which needs to initiate conversation
     * @param flag - Whether this conversation is private or not
     * @return An instance of BroadCastService responsible for handling the group
     *          messaging conversations.
     * @throws GroupNotFoundException If the group doesn't exist in the system
     * @throws UserNotFoundException If the user doesn't exist in the system
     * @throws UserNotPresentInTheGroup If the user is not a participant of the system
     * @throws GroupNotPersistedException If group cannot be created( for private groups)
     *
     */
    public BroadCastService getService(String groupUniqueKey, String username, Boolean flag)
            throws GroupNotFoundException, UserNotFoundException, UserNotPresentInTheGroup, GroupNotPersistedException {

        //Check if the group with the given unique identifier exists
        groupService.createIfNotPresent(groupUniqueKey, username, flag);
        if (!hmap.containsKey(groupUniqueKey))
            hmap.put(groupUniqueKey, new MessageBroadCastService(groupUniqueKey));
        return hmap.get(groupUniqueKey);
    }

}
