package edu.northeastern.ccs.im.view;

import java.io.IOException;
import java.util.HashMap;

import edu.northeastern.ccs.im.communication.ClientConnectionFactory;
import edu.northeastern.ccs.im.communication.NetworkRequestFactory;
import edu.northeastern.ccs.im.communication.NetworkResponse;

public class ForgotPasswordWindow extends AbstractTerminalWindow {

  private final int kRecoveryEmailProcess;
  private final int kRecoveryInitiatedProcess;
  private final int kRecoveryFailedProcess;

  ForgotPasswordWindow(TerminalWindow caller, ClientConnectionFactory clientConnectionFactory) {
    super(caller, new HashMap<Integer, String>() {{
      put(0, ConstantStrings.RECOVERY_EMAIL);
      put(1, ConstantStrings.RECOVERY_INITIATED);
      put(2, ConstantStrings.RECOVERY_FAILED);
    }}, clientConnectionFactory);
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
    try {
      NetworkResponse networkResponse = sendNetworkConnection(new NetworkRequestFactory()
              .createForgotPasswordRequest(recoveryAddress));

      return ResponseParser.parseForgotPasswordResponse(networkResponse);
    } catch (NetworkResponseFailureException exception) {
      printMessageInConsole(exception.getMessage());
      return false;
    }
  }
}
