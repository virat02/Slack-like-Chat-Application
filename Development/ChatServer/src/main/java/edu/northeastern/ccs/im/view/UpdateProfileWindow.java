package edu.northeastern.ccs.im.view;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.HashMap;

import edu.northeastern.ccs.im.communication.ClientConnectionFactory;
import edu.northeastern.ccs.im.communication.CommunicationUtils;
import edu.northeastern.ccs.im.communication.NetworkRequestFactory;
import edu.northeastern.ccs.im.communication.NetworkResponse;

public class UpdateProfileWindow extends AbstractTerminalWindow {

  private TerminalWindow chatTerminalWindow;
  private TerminalWindow forgotPasswordWindow;

  public UpdateProfileWindow(TerminalWindow caller, ClientConnectionFactory clientConnectionFactory) {
    super(caller, new HashMap<Integer, String>() {{
      put(0, ConstantStrings.UPDATE_PROFILE);
      put(1, ConstantStrings.UPDATE_PROFILE_USERNAME);
      put(2, ConstantStrings.UPDATE_PROFILE_STATUS);
    }}, clientConnectionFactory);
  }

  @Override
  void inputFetchedFromUser(String inputString) {
    if (getCurrentProcess() == 1) {
      if (updateUserName(inputString)) {
        printMessageInConsole(ConstantStrings.UPDATE_PROFILE_SUCCESS);
      }
      else {
        printMessageInConsole(ConstantStrings.UPDATE_PROFILE_FAILED);
      }
      printInConsoleForProcess(0);
    }
    else if (getCurrentProcess() == 2) {
      if (updateUserStatus(inputString)) {
        printMessageInConsole(ConstantStrings.UPDATE_PROFILE_SUCCESS);
      }
      else {
        printMessageInConsole(ConstantStrings.UPDATE_PROFILE_FAILED);
      }
      printInConsoleForProcess(0);
    }
    if (inputString.equals("1")) {
      printInConsoleForProcess(1);
    } else if (inputString.equals("2")) {
      printInConsoleForProcess(2);
    } else if (inputString.equals("0")) {
      goBack();
    } else if (inputString.equals("*")) {
      exitWindow();
    } else {
      invalidInputPassed();
    }
  }

  private boolean updateUserName(String userName) {
    try {
      NetworkResponse networkResponse = sendNetworkConnection(new NetworkRequestFactory()
              .createUpdateUserName(userName));
      return updateSuccessful(networkResponse);
    } catch (IOException exception) {
      // TODO Provide some good custom message
      printMessageInConsole(ConstantStrings.NETWORK_ERROR);
      return false;
    }
  }

  private boolean updateUserStatus(String userStatus) {
    try {
      NetworkResponse networkResponse = sendNetworkConnection(new NetworkRequestFactory()
              .createUpdateUserStatus(userStatus));
      return updateSuccessful(networkResponse);
    } catch (IOException exception) {
      // TODO Provide some good custom message
      printMessageInConsole(ConstantStrings.NETWORK_ERROR);
      return false;
    }
  }

  private boolean updateSuccessful(NetworkResponse networkResponse) throws IOException {
    JsonNode jsonNode = CommunicationUtils
            .getObjectMapper().readTree(networkResponse.payload().jsonString());
    boolean status = jsonNode.get("status").asBoolean();
    return status;
  }
}
