package edu.northeastern.ccs.im.view;

import edu.northeastern.ccs.im.Message;
import edu.northeastern.ccs.im.MessageWrapper;
import edu.northeastern.ccs.im.communication.MessageClientConnection;

import java.io.IOException;
import java.util.List;

public class MessageSocketListener implements Runnable, Listener {

    private final MessageClientConnection clientConnection;
    private boolean isRunning;
    private MessageWrapper messageWrapper;

    public MessageSocketListener(MessageClientConnection clientConnection, MessageWrapper messageWrapper) {
        this.clientConnection = clientConnection;
        isRunning = true;
        this.messageWrapper = messageWrapper;
    }

    @Override
    public void run() {
        while (isRunning) {
            List<Message> messages = clientConnection.readMessages();
            messageWrapper.addMessageToList(messages);
            messages.stream()
                    .map(m -> ((messageWrapper.getIndexOfObject(m) + 1) + ")" + messageFormatter().formatMessage(m)))
                    .forEach(ViewConstants.getOutputStream()::println);
        }
    }

    //Listener Methods
    @Override
    public void shouldStopListening() {
        try {
            clientConnection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        isRunning = false;
    }

    public static MessageFormatter messageFormatter() {

        return m -> {
            if (m.getName().equals(UserConstants.getUserName()))
                return "you" + ":" + m.getText();

            return m.getName() + ":" + m.getText();
        };
    }
}

interface MessageFormatter {
    String formatMessage(Message message);
}