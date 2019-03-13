package edu.northeastern.ccs.im.view;

public class MessageSocketListener implements Runnable, Listener {

  private final MessageListerner messageListerner;
  private boolean isRunning;

  public MessageSocketListener(MessageListerner messageListerner) {
    this.messageListerner = messageListerner;
    isRunning = true;
  }

  //Runnable methods
  @Override
  public void run() {
    System.out.println("Entered");
    while (isRunning) {
      /* TODO: Must make a socket connection and make the class listen to new messages of the
      group */
    }
    System.out.println("Ended");
  }

  //Listener Methods
  @Override
  public void shouldStopListening() {
    isRunning = false;
  }
}
