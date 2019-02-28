package edu.northeastern.ccs.im.view;

import java.util.HashMap;

class LoginWindow extends AbstractTerminalWindow {

  private final TerminalWindow chatTerminalWindow;
  private final TerminalWindow forgotPasswordWindow;

  private String userIdString;
  private String passwordString;

  LoginWindow(TerminalWindow caller) {
    super(caller, new HashMap<Integer, String>() {{
      put(0, ConstantStrings.kUserNameString);
      put(1, ConstantStrings.kPasswordString);
      put(2, ConstantStrings.kLoginFailed);
    }});
    chatTerminalWindow = new ChatTerminalWindow(this);
    forgotPasswordWindow = new ForgotPasswordWindow(this);
  }

  @Override
  void inputFetchedFromUser(String inputString) {
    if (getCurrentProcess() == 0) {
      userIdString = inputString;
      printInConsoleForNextProcess();
    }
    else if (getCurrentProcess() == 1) {
      passwordString = inputString;
      if (isUserDetailsValid()) {
        printMessageInConsole(ConstantStrings.kLoginSuccessful);
        chatTerminalWindow.runWindow();
      }
      else  {
        printInConsoleForNextProcess();
      }
    }
    else {
      if (inputString.length() == 1) {
        if (inputString.equals("1")) {
          printInConsoleForProcess(0);
        }
        else if (inputString.equals("2")) {
          forgotPasswordWindow.runWindow();
        }
        else if (inputString.equals("0")) {
          goBack();
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

  private boolean isUserDetailsValid() {
    return userIdString.contains("@");
  }
}
