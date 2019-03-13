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

  SignUpWindow(TerminalWindow caller, ClientConnectionFactory clientConnectionFactory) {
    super(caller, new HashMap<Integer, String>() {{
      put(0, ConstantStrings.EMAIL_ADDRESS_STRING);
      put(1, ConstantStrings.USER_NAME_STRING);
      put(2, ConstantStrings.PASSWORD_STRING);
      put(3, ConstantStrings.RE_ENTER_PASSWORD_STRING);
      put(4, ConstantStrings.SIGN_UP_FAILED);
    }}, clientConnectionFactory);
  }

  public TerminalWindow getChatTerminalWindow(int userId) {
    if (chatTerminalWindow == null) {
      chatTerminalWindow = new ChatTerminalWindow(this, userId, clientConnectionFactory);
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
        printMessageInConsole(ConstantStrings.PASSWORDS_DO_NOT_MATCH);
        printInConsoleForProcess(2);
      } else {
        int id = createUserAndFetchId();
        if (id == -1) {
          printInConsoleForProcess(4);
        }
        else {
          printMessageInConsole(ConstantStrings.SIGN_UP_SUCCESSFUL);
          getChatTerminalWindow(id).runWindow();
        }
      }
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

  private int createUserAndFetchId() {
    try {
      NetworkResponse networkResponse = sendNetworkConnection(new NetworkRequestFactory()
              .createUserRequest(userName, emailAddress, passwordString));
      return ResponseParser.parseLoginNetworkResponse(networkResponse).getId();
    } catch (IOException exception) {
      // TODO Provide some good custom message
      printMessageInConsole(ConstantStrings.NETWORK_ERROR);
    } catch (NetworkResponseFailureException exception) {
      printMessageInConsole(exception.getMessage());
    }
    return -1;
  }
}
