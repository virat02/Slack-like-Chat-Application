package edu.northeastern.ccs.im.view;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.HashMap;

import edu.northeastern.ccs.im.communication.ClientConnectionFactory;
import edu.northeastern.ccs.im.communication.CommunicationUtils;
import edu.northeastern.ccs.im.communication.NetworkRequestFactory;
import edu.northeastern.ccs.im.communication.NetworkResponse;

public class LoginWindow extends AbstractTerminalWindow {

  private TerminalWindow chatTerminalWindow;
  private TerminalWindow forgotPasswordWindow;

  public TerminalWindow getChatTerminalWindow(int userId) {
    if (chatTerminalWindow == null) {
      chatTerminalWindow = new ChatTerminalWindow(this, userId, clientConnectionFactory);
    }
    return chatTerminalWindow;
  }

  public TerminalWindow getForgotPasswordWindow() {
    if (forgotPasswordWindow == null) {
      forgotPasswordWindow = new ForgotPasswordWindow(this, clientConnectionFactory);
    }
    return forgotPasswordWindow;
  }

  private String userIdString;
  private String passwordString;

  LoginWindow(TerminalWindow caller, ClientConnectionFactory clientConnectionFactory) {
    super(caller, new HashMap<Integer, String>() {{
      put(0, ConstantStrings.EMAIL_ADDRESS_STRING);
      put(1, ConstantStrings.PASSWORD_STRING);
      put(2, ConstantStrings.LOGIN_FAILED);
    }}, clientConnectionFactory);
  }

  @Override
  void inputFetchedFromUser(String inputString) {
    if (getCurrentProcess() == 0) {
      userIdString = inputString;
      printInConsoleForNextProcess();
    } else if (getCurrentProcess() == 1) {
      passwordString = inputString;
      int currentUser;
      if ((currentUser = loginUser()) != -1) {
        printMessageInConsole(ConstantStrings.LOGIN_SUCCESSFUL);
        getChatTerminalWindow(currentUser).runWindow();
      } else {
        printInConsoleForProcess(2);
      }
    } else {
      if (inputString.equals("1")) {
        printInConsoleForProcess(0);
      } else if (inputString.equals("2")) {
        getForgotPasswordWindow().runWindow();
      } else if (inputString.equals("0")) {
        goBack();
      } else if (inputString.equals("*")) {
        exitWindow();
      } else {
        invalidInputPassed();
      }
    }
  }

  private int loginUser() {
    if (!userIdString.contains("@")) {
      return -1;
    }
    try {
      NetworkResponse networkResponse = sendNetworkConnection(new NetworkRequestFactory()
              .createLoginRequest(userIdString, passwordString));
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