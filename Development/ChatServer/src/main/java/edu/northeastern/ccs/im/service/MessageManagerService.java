package edu.northeastern.ccs.im.service;

import edu.northeastern.ccs.im.ChatLogger;

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

    public void setGroupService(GroupService groupService)  {
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

    /**
     * Gets a particular message service based on client request
     *
     * @param groupUniqueKey The group unique code of the group
     * @return BroadCastService
     */
    public BroadCastService getService(String groupUniqueKey) throws IllegalAccessException {
        //Check if the group with the given unique identifier exists
        if (groupService.createIfNotPresent(groupUniqueKey)) {
            if (!hmap.containsKey(groupUniqueKey)) {
                hmap.put(groupUniqueKey, new MessageBroadCastService(groupUniqueKey));
            }

            return hmap.get(groupUniqueKey);
        } else {
            ChatLogger.info("Couldn't get a service since no such group with given unique identifier was found!");
            throw new IllegalAccessException("No such group found with unique identifier: " + groupUniqueKey);
        }
    }

    /**
     * Creates a broadcast service iff at-least one client is present
     */

    public void checkForInactivity(MessageBroadCastService messageBroadCastService){
        if (!messageBroadCastService.isClientActive()) {
            for (Map.Entry<String, BroadCastService> entry : hmap.entrySet()) {
                if (entry.getValue() == messageBroadCastService) {
                    hmap.remove(entry.getKey());
                }
            }
        }
    }

}
