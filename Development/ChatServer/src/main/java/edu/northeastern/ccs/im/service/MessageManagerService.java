package edu.northeastern.ccs.im.service;

import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import java.util.logging.Logger;

/**
 * Class to manage all the message services
 */
public class MessageManagerService extends TimerTask {

    //Maps the messageBroadcastService to the unique group identifier
    private Map<String, MessageBroadCastService> map = new HashMap<>();

    // static variable instance of type MessageManagerService
    private static MessageManagerService instance = null;

    private static final Logger LOGGER = Logger.getLogger(MessageManagerService.class.getName());
    private GroupService groupService = new GroupService();

    /**
     * Singleton class MessageManagerService
     * @return the instance of MessageManagerService class
     */
    public static MessageManagerService getInstance() {

        if (instance == null)
            instance = new MessageManagerService();

        return instance;
    }

    /**
     * Gets a particular message service based on client request
     * @param groupUniqueKey
     * @return
     */
    public MessageBroadCastService getService(String groupUniqueKey) throws IllegalAccessException {

        //Check if the group with the given unique identifier exists
        if (groupService.searchUsingCode(groupUniqueKey) != null) {
            if(!map.containsKey(groupUniqueKey)){
                map.put(groupUniqueKey, new MessageBroadCastService());
            }

            return map.get(groupUniqueKey);
        }
        else {
            LOGGER.info("Couldn't get a service since no such group with given unique identifier was found!");
            throw new IllegalAccessException("No such group found with unique identifier: "+groupUniqueKey);
        }
    }

    /**
     * Creates a broadcast service iff at-least one client is present
     */
    public void checkForInactivity(MessageBroadCastService messageBroadCastService){

        if (!messageBroadCastService.isClientActive()) {
            for (Map.Entry<String, MessageBroadCastService> entry : map.entrySet()) {
                if (entry.getValue() == messageBroadCastService) {
                    map.remove(entry.getKey());
                }
            }

        }
    }

    @Override
    public void run() {
        //checkForInactivity(mess);
    }
}
