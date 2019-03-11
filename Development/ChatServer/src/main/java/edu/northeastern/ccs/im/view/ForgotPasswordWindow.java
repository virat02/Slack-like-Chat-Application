package edu.northeastern.ccs.im.view;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.HashMap;

import edu.northeastern.ccs.im.communication.CommunicationUtils;
import edu.northeastern.ccs.im.communication.NetworkRequestFactory;
import edu.northeastern.ccs.im.communication.NetworkResponse;

public class ForgotPasswordWindow extends AbstractTerminalWindow {

  private final int kRecoveryEmailProcess;
  private final int kRecoveryInitiatedProcess;
  private final int kRecoveryFailedProcess;

  ForgotPasswordWindow(TerminalWindow caller) {
    super(caller, new HashMap<Integer, String>() {{
      put(0, ConstantStrings.RECOVERY_EMAIL);
      put(1, ConstantStrings.RECOVERY_INITIATED);
      put(2, ConstantStrings.RECOVERY_FAILED);
    }});
    kRecoveryEmailProcess = 0;
    kRecoveryInitiatedProcess = 1;
    kRecoveryFailedProcess = 2;
  }

  @Override
  void inputFetchedFromUser(String inputString) {
    if (getCurrentProcess() == kRecoveryEmailProcess) {
      if (initiateRecovery(inputString)) {
        printInConsoleForNextProcess();
      }
      else {
        printInConsoleForProcess(2);
      }
    }
    else {
      if (inputString.equals("0")) {
        goBack();
      }
      else if (inputString.equals("*")) {
        exitWindow();
      }
      else if (inputString.equals("1")) {
        printInConsoleForProcess(0);
      }
      else {
        invalidInputPassed();
      }
    }
  }

  private boolean initiateRecovery(String recoveryAddress) {
    if (!recoveryAddress.contains("@")) {
      return false;
    }
    else {
      return true;
    }
  }
}
