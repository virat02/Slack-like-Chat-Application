package edu.northeastern.ccs.im.view;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.HashMap;

import edu.northeastern.ccs.im.communication.ClientConnectionFactory;
import edu.northeastern.ccs.im.communication.CommunicationUtils;
import edu.northeastern.ccs.im.communication.NetworkRequestFactory;
import edu.northeastern.ccs.im.communication.NetworkResponse;

public class MessageWindow extends AbstractTerminalWindow {

  public MessageWindow(TerminalWindow caller, ClientConnectionFactory clientConnectionFactory) {
    super(caller, new HashMap<Integer, String>() {{
      put(0, ConstantStrings.EMAIL_ADDRESS_STRING);
    }}, clientConnectionFactory);
  }

  @Override
  void inputFetchedFromUser(String inputString) {
    if (getCurrentProcess() == 0) {

    } else {
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

  private int sendMessage() {
    try {
      NetworkResponse networkResponse = sendNetworkConnection(new NetworkRequestFactory()
              .createLoginRequest("", ""));
      return getUserId(networkResponse);
    } catch (IOException exception) {
      // TODO Provide some good custom message
      printMessageInConsole(ConstantStrings.NETWORK_ERROR);
      return -1;
    }
  }

  private int getUserId(NetworkResponse networkResponse) throws IOException {
    JsonNode jsonNode = CommunicationUtils
            .getObjectMapper().readTree(networkResponse.payload().jsonString());
    int userId = jsonNode.get("id").asInt();
    UserConstants.setUserId(userId);
    return userId;
  }
}
