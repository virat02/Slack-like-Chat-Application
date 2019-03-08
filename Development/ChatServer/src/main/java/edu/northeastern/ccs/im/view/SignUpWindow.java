package edu.northeastern.ccs.im.view;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.HashMap;

import edu.northeastern.ccs.im.communication.*;

public class SignUpWindow extends AbstractTerminalWindow {

  private String userName;
  private String passwordString;
  private String emailAddress;
  private TerminalWindow chatTerminalWindow;

  SignUpWindow(TerminalWindow caller) {
    super(caller, new HashMap<Integer, String>() {{
      put(0, ConstantStrings.kEmailAddressString);
      put(1, ConstantStrings.kUserNameString);
      put(2, ConstantStrings.kPasswordString);
      put(3, ConstantStrings.kReEnterPasswordString);
    }});
  }

  public TerminalWindow getChatTerminalWindow(int userId) {
    if (chatTerminalWindow == null) {
      chatTerminalWindow = new ChatTerminalWindow(this, userId);
    }
    return chatTerminalWindow;
  }

  @Override
  void inputFetchedFromUser(String inputString) {
    if (getCurrentProcess() == 0) {
      emailAddress = inputString;
      printInConsoleForNextProcess();
    } else if (getCurrentProcess() == 1) {
      userName = inputString;
      printInConsoleForNextProcess();
    } else if (getCurrentProcess() == 2) {
      passwordString = inputString;
      printInConsoleForNextProcess();
    } else if (getCurrentProcess() == 3) {
      if (!inputString.equals(passwordString)) {
        printMessageInConsole(ConstantStrings.kPasswordsDoNotMatch);
        printInConsoleForProcess(2);
      } else {
        int id = createUserAndFetchId();
        getChatTerminalWindow(id).runWindow();
      }
    } else {
      if (inputString.length() == 1) {
        if (inputString.equals("1")) {
          printInConsoleForProcess(0);
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

  private int createUserAndFetchId() {
    try {
      NetworkResponse networkResponse = sendNetworkConnection(new NetworkRequestFactory()
              .createUserRequest(userName, emailAddress, passwordString));

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
      JsonNode jsonNode = CommunicationUtils.getObjectMapper().readTree(networkResponse.payload().jsonString());
      int userId = jsonNode.get("id").asInt();
      UserConstants.setUserId(userId);
      return userId;
    } catch (IOException e) {
      e.printStackTrace();
    }

    return -1;
  }
}
