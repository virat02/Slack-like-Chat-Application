package edu.northeastern.ccs.im.view;

import edu.northeastern.ccs.im.communication.ClientConnectionFactory;

import java.util.HashMap;

class RootWindow extends AbstractTerminalWindow {

  private TerminalWindow loginWindow;
  private TerminalWindow signUpWindow;

  public TerminalWindow getLoginWindow(){
    if (loginWindow == null) {
      loginWindow = new LoginWindow(this, clientConnectionFactory);
    }
    return loginWindow;
  }

  public TerminalWindow getSignUpWindow(){
    if (signUpWindow == null) {
      signUpWindow = new SignUpWindow(this, clientConnectionFactory);
    }
    return signUpWindow;
  }

  RootWindow(TerminalWindow caller, ClientConnectionFactory clientConnectionFactory) {
    super(caller, new HashMap<Integer, String>() {{
      put(0, ConstantStrings.INITIAL_LAUNCH);
    }}, clientConnectionFactory);
  }

  @Override
  void inputFetchedFromUser(String inputString) {
    if (inputString.equals("1")) {
      getLoginWindow().runWindow();
    }
    else if (inputString.equals("2")) {
      getSignUpWindow().runWindow();
    }
    else if (inputString.equals("*")) {
      exitWindow();
    }
    else {
      invalidInputPassed();
    }
  }

  @Override
  protected String helpCommand() {
    return "Create your account, login and start chatting";
  }
}
