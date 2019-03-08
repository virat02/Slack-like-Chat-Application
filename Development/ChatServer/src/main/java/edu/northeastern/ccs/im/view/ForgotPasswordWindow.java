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
      put(0, ConstantStrings.kRecoveryEmail);
      put(1, ConstantStrings.kRecoveryInitiated);
      put(2, ConstantStrings.kRecoveryFailed);
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
        printInConsoleForNextProcess();
      }
    }
    else {
      if (inputString.equals("0")) {
        goBack();
      }
      else if (inputString.equals("*")) {
        exitWindow();
      }
      else if ((getCurrentProcess() == kRecoveryFailedProcess) && (inputString.equals("1"))) {
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

      return getRecoverySentState(networkResponse);
    } catch (IOException exception) {
      exception.printStackTrace();
      printMessageInConsole(ConstantStrings.kNetworkError);
      printMessageInConsole(exception.getMessage());
    }
    return false;
  }

  private boolean getRecoverySentState(NetworkResponse networkResponse) {
    try {
      JsonNode jsonNode = CommunicationUtils.getObjectMapper().readTree(networkResponse.payload().jsonString());
      boolean isRecoveryMailSent = jsonNode.get("success").asBoolean();
      return isRecoveryMailSent;
    } catch (IOException e) {
      e.printStackTrace();
    }

    return false;
  }
}
