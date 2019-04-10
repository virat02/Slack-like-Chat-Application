package edu.northeastern.ccs.im.view;

import java.io.IOException;
import java.util.HashMap;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.Message;
import edu.northeastern.ccs.im.communication.*;

import javax.swing.text.View;

public class MessageWindow extends AbstractTerminalWindow implements MessageListerner {

    private final boolean isPrivate;
    private Listener messageSocketListener;
    private final String groupCode;
    private MessageClientConnection messageClientConnection;

    public MessageWindow(TerminalWindow caller, ClientConnectionFactory clientConnectionFactory, String groupCode, boolean isPrivate) {
        super(caller, new HashMap<Integer, String>() {{
            put(0, "");
        }}, clientConnectionFactory);
        this.groupCode = groupCode;
        this.isPrivate = isPrivate;
    }

    @Override
    public void runWindow() {
        messageClientConnection =
                (MessageClientConnection) clientConnectionFactory.createMessageClientConnection(NetworkConstants.getHostName(),
                        NetworkConstants.getPortNumber());
        NetworkRequest networkRequest = networkRequestFactory.createJoinGroup(groupCode, UserConstants.getUserName(), isPrivate);
        try {
            messageClientConnection.connect();
            messageClientConnection.sendRequest(networkRequest);
            NetworkResponse networkResponse = messageClientConnection.readResponse();
            ResponseParser.readRecentMessagesAndPrintInScreen(networkResponse);
            messageSocketListener = new MessageSocketListener(messageClientConnection);
            Thread threadObject = new Thread((Runnable) messageSocketListener);
            threadObject.start();
            messageClientConnection.sendMessage(Message.makeSimpleLoginMessage(UserConstants.getUserName(), groupCode));
            super.runWindow();
        } catch (NetworkResponseFailureException | IOException e) {
            ViewConstants.getOutputStream().println(e.getMessage());
        }

        goBack();
    }

    @Override
    protected void getInputFromUser() {
        String input;
        try {
            while ((input = ViewConstants.getInputStream().readLine()) != null) {
                if (input.trim().equals("")) {
                    printMessageInConsole(ConstantStrings.INVALID_INPUT_STRING);
                    printInConsoleForCurrentProcess();
                } else if ((input.equals("exit")) || (input.equals("/.."))) {
                    logOffUserFromGroup();
                    goBack();
                    return;
                } else
                    inputFetchedFromUser(input);
            }
        } catch (IOException e) {
            getInputFromUser();
        }
    }

    private void logOffUserFromGroup() {
        messageClientConnection.sendMessage(Message.makeQuitMessage(UserConstants.getUserName(), groupCode));
        if (messageSocketListener != null)
            messageSocketListener.shouldStopListening();
        NetworkRequest networkRequest = networkRequestFactory.
                createLogOffFromChatRoomRequest(UserConstants.getUserName(), groupCode);
        try {
            NetworkResponse networkResponse = sendNetworkConnection(networkRequest);
            ResponseParser.parseNetworkResponse(networkResponse);
        } catch (NetworkResponseFailureException | IOException e) {
            ViewConstants.getOutputStream().println(e.getMessage());
            goBack();
        }
    }

    @Override
    void inputFetchedFromUser(String inputString) {
        Message message = Message.makeBroadcastMessage(UserConstants.getUserName(), inputString, groupCode);
        messageClientConnection.sendMessage(message);
    }

    @Override
    public void goBack() {

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