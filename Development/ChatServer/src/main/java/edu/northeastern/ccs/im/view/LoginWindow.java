package edu.northeastern.ccs.im.view;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.HashMap;

import edu.northeastern.ccs.im.communication.ClientConnectionFactory;
import edu.northeastern.ccs.im.communication.CommunicationUtils;
import edu.northeastern.ccs.im.communication.NetworkRequestFactory;
import edu.northeastern.ccs.im.communication.NetworkResponse;
import edu.northeastern.ccs.im.userGroup.User;

public class LoginWindow extends AbstractTerminalWindow {

  private TerminalWindow chatTerminalWindow;
  private TerminalWindow forgotPasswordWindow;

  public TerminalWindow getChatTerminalWindow(int userId) {
    if (chatTerminalWindow == null) {
      chatTerminalWindow = new ChatTerminalWindow(this, clientConnectionFactory);
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
    try {
      NetworkResponse networkResponse = sendNetworkConnection(new NetworkRequestFactory()
              .createLoginRequest(userIdString, passwordString));
      return ResponseParser.parseLoginNetworkResponse(networkResponse).getId();
    } catch (IOException exception) {
      printMessageInConsole(ConstantStrings.NETWORK_ERROR);
    }
    catch (NetworkResponseFailureException exception) {
      printMessageInConsole(exception.getMessage());
    }
    return -1;
  }
}