package edu.northeastern.ccs.im.view;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.HashMap;

import edu.northeastern.ccs.im.communication.CommunicationUtils;
import edu.northeastern.ccs.im.communication.NetworkRequestFactory;
import edu.northeastern.ccs.im.communication.NetworkResponse;

class LoginWindow extends AbstractTerminalWindow {

  private TerminalWindow chatTerminalWindow;
  private TerminalWindow forgotPasswordWindow;

  public TerminalWindow getChatTerminalWindow(int userId) {
    if (chatTerminalWindow == null) {
      chatTerminalWindow = new ChatTerminalWindow(this, userId);
    }
    return chatTerminalWindow;
  }

  public TerminalWindow getForgotPasswordWindow() {
    if (forgotPasswordWindow == null) {
      forgotPasswordWindow = new ForgotPasswordWindow(this);
    }
    return forgotPasswordWindow;
  }

  private String userIdString;
  private String passwordString;

  LoginWindow(TerminalWindow caller) {
    super(caller, new HashMap<Integer, String>() {{
      put(0, ConstantStrings.kEmailAddressString);
      put(1, ConstantStrings.kPasswordString);
      put(2, ConstantStrings.kLoginFailed);
    }});
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
        printMessageInConsole(ConstantStrings.kLoginSuccessful);
        getChatTerminalWindow(currentUser).runWindow();
      }
      else {
        printInConsoleForProcess(2);
      }
    } else {
      if (inputString.length() == 1) {
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
      } else {
        invalidInputPassed();
      }
    }
  }

  private int loginUser() {
    try {
      NetworkResponse networkResponse = sendNetworkConnection(new NetworkRequestFactory()
              .createLoginRequest(userIdString, passwordString));

      return getUserId(networkResponse);
    } catch (IOException exception) {
      exception.printStackTrace();
      printMessageInConsole(ConstantStrings.kNetworkError);
      printMessageInConsole(exception.getMessage());
    }

    return 0;
  }

  private int getUserId(NetworkResponse networkResponse) {
    try {
      JsonNode jsonNode = CommunicationUtils
              .getObjectMapper().readTree(networkResponse.payload().jsonString());
      int userId = jsonNode.get("id").asInt();
      UserConstants.setUserId(userId);
      return userId;
    } catch (IOException e) {
      e.printStackTrace();
    }

    return -1;
  }
}