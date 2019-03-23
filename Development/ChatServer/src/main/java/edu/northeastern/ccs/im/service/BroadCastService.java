package edu.northeastern.ccs.im.service;

import edu.northeastern.ccs.im.Message;
import edu.northeastern.ccs.im.customexceptions.MessageNotFoundException;
import edu.northeastern.ccs.im.server.ClientRunnable;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.List;

/**
 * The interface Broad cast service.
 * Follows observer pattern to maintain a list
 * of clients connected to a group
 */
public interface BroadCastService {
    /**
     * Adds the socket channel to the list of maintained clients.
     *
     * @param socketChannel the socket channel
     * @throws IOException the io exception
     */
    void addConnection(SocketChannel socketChannel) throws IOException;

    /**
     * If there is any clients active with this service.
     *
     * @return the boolean
     */
    boolean isClientActive();

    /**
     * Broadcast message to all the clients
     * connected to this service
     *
     * @param msg the msg
     */
    void broadcastMessage(Message msg);

    /**
     * Remove client from the list of maintained clients.
     *
     * @param clientRunnable the client runnable
     */
    void removeClient(ClientRunnable clientRunnable);

    /**
     * Gets the list of recent messages sent across this group.
     *
     * @return the recent messages
     */
    List<Message> getRecentMessages();
}
