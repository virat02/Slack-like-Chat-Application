package edu.northeastern.ccs.im.view;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.HashMap;

import edu.northeastern.ccs.im.communication.ClientConnectionFactory;
import edu.northeastern.ccs.im.communication.CommunicationUtils;
import edu.northeastern.ccs.im.communication.NetworkRequestFactory;
import edu.northeastern.ccs.im.communication.NetworkResponse;

public class CreateGroupWindow extends AbstractTerminalWindow {

  public CreateGroupWindow(TerminalWindow caller, ClientConnectionFactory clientConnectionFactory) {
    super(caller, new HashMap<Integer, String>() {{
      put(0, ConstantStrings.CREATE_GROUP);
      put(1, ConstantStrings.CREATE_GROUP_SUCCESS);
      put(2, ConstantStrings.CREATE_GROUP_FAILED);
    }}, clientConnectionFactory);
  }

  @Override
  void inputFetchedFromUser(String inputString) {
    if (getCurrentProcess() == 0) {
      if (createGroup(inputString)) {
        printInConsoleForNextProcess();
      }
      else {
        printInConsoleForProcess(2);
      }
    }
    else {
      if (inputString.equals("1")) {
        printInConsoleForProcess(0);
      } else if (inputString.equals("0")) {
        goBack();
      } else if (inputString.equals("*")) {
        exitWindow();
      } else {
        invalidInputPassed();
      }
    }
  }

  private boolean createGroup(String groupName) {
    try {
      NetworkResponse networkResponse = sendNetworkConnection(new NetworkRequestFactory()
              .createGroupRequest(groupName));
      return getGroupCreationStatus(networkResponse);
    } catch (IOException exception) {
      // TODO Provide some good custom message
      printMessageInConsole(ConstantStrings.NETWORK_ERROR);
      return false;
    }
  }

  private boolean getGroupCreationStatus(NetworkResponse networkResponse) throws IOException {
    JsonNode jsonNode = CommunicationUtils
            .getObjectMapper().readTree(networkResponse.payload().jsonString());
    boolean status = jsonNode.get("status").asBoolean();
    return status;
  }
}
