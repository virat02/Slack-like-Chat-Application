package edu.northeastern.ccs.im.service;

import edu.northeastern.ccs.im.Message;
import edu.northeastern.ccs.im.server.ClientRunnable;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.List;

public interface BroadCastService {
    void addConnection(SocketChannel socketChannel) throws IOException;
    boolean isClientActive();
    void broadcastMessage(Message msg);
    void removeClient(ClientRunnable clientRunnable);
    List<Message> getRecentMessages();
}
