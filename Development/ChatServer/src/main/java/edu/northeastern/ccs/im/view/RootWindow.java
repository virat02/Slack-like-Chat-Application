package edu.northeastern.ccs.im.view;

import java.util.HashMap;

class RootWindow extends AbstractTerminalWindow {

  private final TerminalWindow loginWindow;
  private final TerminalWindow signUpWindow;

  RootWindow(TerminalWindow caller) {
    super(caller, new HashMap<Integer, String>() {{
      put(0, ConstantStrings.kInitialLaunch);
    }});
    loginWindow = new LoginWindow(this);
    signUpWindow = new SignUpWindow(this);
  }

  @Override
  void inputFetchedFromUser(String inputString) {
    if (inputString.length() == 1) {
      if (inputString.equals("1")) {
        loginWindow.runWindow();
      }
      else if (inputString.equals("2")) {
        signUpWindow.runWindow();
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
