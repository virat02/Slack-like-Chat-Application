package edu.northeastern.ccs.im.view;

import java.util.HashMap;

public class ForgotPasswordWindow extends AbstractTerminalWindow {

  private final int kRecoveryEmailProcess;
  private final int kRecoveryInitiatedProcess;

  ForgotPasswordWindow(TerminalWindow caller) {
    super(caller, new HashMap<Integer, String>() {{
      put(0, ConstantStrings.kRecoveryEmail);
      put(1, ConstantStrings.kRecoveryInitiated);
    }});
    kRecoveryEmailProcess = 0;
    kRecoveryInitiatedProcess = 1;
  }

  @Override
  void inputFetchedFromUser(String inputString) {
    if (getCurrentProcess() == kRecoveryEmailProcess) {
      initiateRecovery(inputString);
      printInConsoleForNextProcess();
    }
    else {
      if (inputString.length() == kRecoveryInitiatedProcess) {
        if (inputString.equals("0")) {
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

  private void initiateRecovery(String recoveryAddress) {
    //Just for fun
  }
}
