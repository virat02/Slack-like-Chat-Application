package edu.northeastern.ccs.im.service;

import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Class to manage all the message services
 */
public class MessageManagerService {

    //Maps the messageBroadcastService to the unique group identifier
    private HashMap<String, MessageBroadCastService> hmap = new HashMap<>();

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
    public MessageBroadCastService getService(String groupUniqueKey) {

        //Check if the group with the given unique identifier exists
        if (groupService.searchUsingCode(groupUniqueKey) != null) {

            if(!hmap.containsKey(groupUniqueKey)){
                hmap.put(groupUniqueKey, new MessageBroadCastService());
            }

            return hmap.get(groupUniqueKey);

        }
        else {
            LOGGER.info("Couldn't get a service since no such group with given unique identifier was found!");
            throw new NullPointerException("No such group found with unique identifier: "+groupUniqueKey);
        }
    }

    /**
     * Creates a broadcast service iff at-least one client is present
     */
    public MessageBroadCastService createService(){

        MessageBroadCastService messageBroadCastService = new MessageBroadCastService();

        //Create a service if one or more clients is present
        if (messageBroadCastService.isClientActive()) {
            return messageBroadCastService;
        }
        else {
            LOGGER.info("Cannot create a service since client is not present!");
            throw new UnsupportedOperationException("Cannot create a service since client is not present!");
        }
    }

}
