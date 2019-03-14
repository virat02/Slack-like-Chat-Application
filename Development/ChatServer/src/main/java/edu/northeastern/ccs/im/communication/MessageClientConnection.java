package edu.northeastern.ccs.im.communication;

import edu.northeastern.ccs.im.Message;

import java.util.List;

public interface MessageClientConnection extends ClientConnection {
    List<Message> readMessages();
    void sendMessage(Message message);
}
