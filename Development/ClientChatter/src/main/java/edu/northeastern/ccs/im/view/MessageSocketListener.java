package edu.northeastern.ccs.im.view;

import edu.northeastern.ccs.im.Message;
import edu.northeastern.ccs.im.communication.MessageClientConnection;

import java.io.IOException;
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
      messages.stream()
              .map(m -> messageFormatter().formatMessage(m))
              .forEach(ViewConstants.getOutputStream()::println);
    }
    ViewConstants.getOutputStream().println("Ended");
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
      if (UserConstants.getUserName().equals(m.getName()))
        return "";
      return m.getName() + ":" + m.getText();
    };
  }
}

interface MessageFormatter {
  String formatMessage(Message message);
}