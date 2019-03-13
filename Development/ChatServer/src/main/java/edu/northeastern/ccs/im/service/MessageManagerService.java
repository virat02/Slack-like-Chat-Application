package edu.northeastern.ccs.im.service;

import java.util.logging.Logger;

public class MessageManagerService {

    // static variable instance of type MessageManagerService
    private static MessageManagerService instance = null;

    private static final Logger LOGGER = Logger.getLogger(MessageManagerService.class.getName());
    private GroupService groupService = new GroupService();

    /**
     * Singleton class MessageManagerService
     * @return
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

        if (groupService.searchUsingCode(groupUniqueKey) != null) {
            return new MessageBroadCastService();
        }
        else {
            return null;
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
            return null;
        }
    }

}
