package edu.northeastern.ccs.im.view;

import edu.northeastern.ccs.im.Message;
import edu.northeastern.ccs.im.communication.MessageClientConnection;

import java.util.List;

public class MessageSocketListener implements Runnable, Listener {

    private final MessageClientConnection clientConnection;
    private boolean isRunning;

    public MessageSocketListener(MessageClientConnection clientConnection) {
        this.clientConnection = clientConnection;
        isRunning = true;
    }

    @Override
    public void run() {
        ViewConstants.getOutputStream().println("Entered");
        while (isRunning) {
            List<Message> messages = clientConnection.readMessages();
            messages.forEach(ViewConstants.getOutputStream()::println);
        }
        ViewConstants.getOutputStream().println("Ended");
    }

    //Listener Methods
    @Override
    public void shouldStopListening() {
        isRunning = false;
    }
}
