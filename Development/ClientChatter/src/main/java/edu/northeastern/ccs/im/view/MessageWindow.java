package edu.northeastern.ccs.im.view;

import java.io.IOException;
import java.util.HashMap;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.Message;
import edu.northeastern.ccs.im.communication.*;


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
    messageClientConnection =
            (MessageClientConnection) clientConnectionFactory.createMessageClientConnection(NetworkConstants.getHostName(),
                    NetworkConstants.getPortNumber());
    NetworkRequest networkRequest = networkRequestFactory.createJoinGroup(groupCode);
    try {
      messageClientConnection.connect();
      messageClientConnection.sendRequest(networkRequest);
      NetworkResponse networkResponse = messageClientConnection.readResponse();
      if (networkResponse.status() == NetworkResponse.STATUS.SUCCESSFUL) {
        ResponseParser.readRecentMessagesAndPrintInScreen(networkResponse);
        messageSocketListener = new MessageSocketListener(messageClientConnection);
        Thread threadObject = new Thread((Runnable) messageSocketListener);
        threadObject.start();
        messageClientConnection.sendMessage(Message.makeSimpleLoginMessage(UserConstants.getUserName(), groupCode));
        super.runWindow();
      }
    } catch (NetworkResponseFailureException e) {
      ChatLogger.error("Could not be joined due as network request was not successful");
    } catch (IOException e) {
      ChatLogger.error("Could not be joined to chat group due to an error");
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