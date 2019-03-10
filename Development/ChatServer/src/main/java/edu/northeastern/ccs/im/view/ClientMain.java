package edu.northeastern.ccs.im.view;

import edu.northeastern.ccs.im.communication.ClientConnectionFactory;

public abstract class ClientMain {

  private static TerminalWindow rootWindow;

  public static void main(String[] args) {
    rootWindow = new RootWindow(null, new ClientConnectionFactory());
    rootWindow.runWindow();
  }
}