package edu.northeastern.ccs.im.view;

import java.util.HashMap;

import edu.northeastern.ccs.im.Message;
import edu.northeastern.ccs.im.communication.ClientConnectionFactory;

public class MessageWindow extends AbstractTerminalWindow implements MessageListerner {

  private final Listener messageSocketListener;
  private final Thread threadObject;

  public MessageWindow(TerminalWindow caller, ClientConnectionFactory clientConnectionFactory) {
    super(caller, new HashMap<Integer, String>() {{
      put(0, "Message Window");
    }}, clientConnectionFactory);
    messageSocketListener = new MessageSocketListener(this);
    threadObject = new Thread((Runnable) messageSocketListener);
  }
  @Override
  public void runWindow() {
//    threadObject.run();
    super.runWindow();
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
