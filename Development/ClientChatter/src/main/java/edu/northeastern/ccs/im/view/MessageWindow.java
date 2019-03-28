package edu.northeastern.ccs.im.view;

import java.io.IOException;
import java.util.HashMap;

import edu.northeastern.ccs.im.Message;
import edu.northeastern.ccs.im.communication.*;

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
            ResponseParser.throwErrorIfResponseFailed(networkResponse);
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
      while((input = ViewConstants.getInputStream().readLine()) != null) {
        if (input.trim().equals("")) {
          printMessageInConsole(ConstantStrings.INVALID_INPUT_STRING);
          printInConsoleForCurrentProcess();
          continue;
        }
        else if ((input.equals("exit")) || (input.equals("/.."))) {
          goBack();
          return;
        }
        else
          inputFetchedFromUser(input);
      }
    } catch (IOException e) {
      getInputFromUser();
    }
  }

    @Override
    void inputFetchedFromUser(String inputString) {
        Message message = Message.makeBroadcastMessage(UserConstants.getUserName(), inputString, groupCode);
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

}