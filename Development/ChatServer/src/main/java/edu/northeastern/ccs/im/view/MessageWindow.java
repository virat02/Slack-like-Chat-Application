package edu.northeastern.ccs.im.view;

import java.io.IOException;
import java.util.HashMap;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.Message;
import edu.northeastern.ccs.im.communication.*;
import edu.northeastern.ccs.im.controller.UserController;

public class MessageWindow extends AbstractTerminalWindow implements MessageListerner {

    private Listener messageSocketListener;
    private final String groupCode;
    private MessageClientConnection messageClientConnection;

    public MessageWindow(TerminalWindow caller, ClientConnectionFactory clientConnectionFactory, String groupCode) {
        super(caller, new HashMap<Integer, String>() {{
            put(0, "Message Window");
        }}, clientConnectionFactory);
        this.groupCode = groupCode;
    }

    @Override
    public void runWindow() {
        messageClientConnection = (MessageClientConnection) clientConnectionFactory.createMessageClientConnection(hostName, port);
        NetworkRequest networkRequest = networkRequestFactory.createJoinGroup(groupCode);
        try {
            messageClientConnection.sendRequest(networkRequest);
            NetworkResponse networkResponse = messageClientConnection.readResponse();
            if (networkResponse.status() == NetworkResponse.STATUS.SUCCESSFUL) {
                messageSocketListener = new MessageSocketListener(messageClientConnection);
                Thread threadObject = new Thread((Runnable) messageSocketListener);
                threadObject.start();
                super.runWindow();
                messageClientConnection.sendMessage(Message.makeSimpleLoginMessage(UserConstants.getUserName(), groupCode));
            }
        } catch (IOException e) {
            ChatLogger.error("Could not be joined to chat group due to an error");
            goBack();
        }
    }

    @Override
    void inputFetchedFromUser(String inputString) {
        Message message = Message.makeBroadcastMessage(inputString, UserConstants.getUserName(), groupCode);
        messageClientConnection.sendMessage(message);
    }

    @Override
    public void goBack() {
        if (messageSocketListener != null)
            messageSocketListener.shouldStopListening();
        super.goBack();
    }

    @Override
    public void exitWindow() {
        messageSocketListener.shouldStopListening();
        super.exitWindow();
    }

    @Override
    public Message newMessageReceived() {
        return null;
    }

    public static void main(String args[])  {
        MessageWindow messageWindow = new MessageWindow(null, new ClientConnectionFactory(), "");
    }
}
