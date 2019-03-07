package edu.northeastern.ccs.im.view;

import java.util.HashMap;

class RootWindow extends AbstractTerminalWindow {

  private TerminalWindow loginWindow;
  private TerminalWindow signUpWindow;

  public TerminalWindow getLoginWindow(){
    if (loginWindow == null) {
      loginWindow = new LoginWindow(this);
    }
    return loginWindow;
  }

  public TerminalWindow getSignUpWindow(){
    if (signUpWindow == null) {
      signUpWindow = new SignUpWindow(this);
    }
    return signUpWindow;
  }

  RootWindow(TerminalWindow caller) {
    super(caller, new HashMap<Integer, String>() {{
      put(0, ConstantStrings.kInitialLaunch);
    }});
  }

  @Override
  void inputFetchedFromUser(String inputString) {
    if (inputString.length() == 1) {
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
    else {
      invalidInputPassed();
    }
  }
}
