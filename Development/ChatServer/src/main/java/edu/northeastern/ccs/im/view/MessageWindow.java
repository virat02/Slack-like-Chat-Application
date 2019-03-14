package edu.northeastern.ccs.im.view;

import java.io.IOException;
import java.util.HashMap;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.Message;
import edu.northeastern.ccs.im.communication.*;

public class MessageWindow extends AbstractTerminalWindow implements MessageListerner {

    private Listener messageSocketListener;
    private final String groupCode;

    public MessageWindow(TerminalWindow caller, ClientConnectionFactory clientConnectionFactory, String groupCode) {
        super(caller, new HashMap<Integer, String>() {{
            put(0, "Message Window");
        }}, clientConnectionFactory);
        this.groupCode = groupCode;
    }

    @Override
    public void runWindow() {
        ClientConnection clientConnection = clientConnectionFactory.createMessageClientConnection(hostName, port);
        NetworkRequest networkRequest = networkRequestFactory.createJoinGroup(groupCode);
        try {
            clientConnection.sendRequest(networkRequest);
            NetworkResponse networkResponse = clientConnection.readResponse();
            if (networkResponse.status() == NetworkResponse.STATUS.SUCCESSFUL) {
                messageSocketListener = new MessageSocketListener((MessageClientConnection) clientConnection);
                Thread threadObject = new Thread((Runnable) messageSocketListener);
                threadObject.start();
                super.runWindow();
            }
        } catch (IOException e) {
            ChatLogger.error("Could not be joined to chat group due to an error");
            goBack();
        }
    }

    @Override
    void inputFetchedFromUser(String inputString) {
        if (inputString.equals("1")) {
            printInConsoleForProcess(0);
        } else if (inputString.equals("0")) {
            goBack();
        } else if (inputString.equals("*")) {
            exitWindow();
        } else {
            invalidInputPassed();
        }
    }

    @Override
    public void goBack() {
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
}
