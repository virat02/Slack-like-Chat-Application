package edu.northeastern.ccs.im.view;

import edu.northeastern.ccs.im.communication.ClientConnectionFactory;
import edu.northeastern.ccs.im.communication.NetworkConstants;

public abstract class ClientMain {

  private static TerminalWindow rootWindow;

  public static void main(String[] args) {
    if (args.length != 2) {
      System.out.println("Invalid Arguments");
      System.exit(0);
    }
    try {
      Integer.parseInt(args[1]);
    }
    catch (NumberFormatException exception) {
      System.out.println("Invalid Arguments");
      System.exit(0);
    }


    try {
      NetworkConstants.setHostName(args[0]);
      NetworkConstants.setPortNumber(Integer.parseInt(args[1]));
//      NetworkConstants.setHostName("localhost");
//      NetworkConstants.setPortNumber(4545);
      rootWindow = new RootWindow(null, new ClientConnectionFactory());
      rootWindow.runWindow();
    }
    catch (Exception e) {
      System.out.println("Oops. Something went wrong");
      System.out.println(e.getMessage());
    }
  }
}