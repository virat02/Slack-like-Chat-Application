package edu.northeastern.ccs.im.view;

public abstract class ClientMain {

  private static TerminalWindow rootWindow;

  public static void main(String[] args) {
    rootWindow = new RootWindow(null);
    rootWindow.runWindow();
  }
}
