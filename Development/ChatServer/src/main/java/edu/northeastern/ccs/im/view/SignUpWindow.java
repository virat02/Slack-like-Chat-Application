package edu.northeastern.ccs.im.view;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;

import edu.northeastern.ccs.im.communication.ClientConnectionImpl;
import edu.northeastern.ccs.im.communication.NetworkRequest;
import edu.northeastern.ccs.im.communication.NetworkRequestFactory;

public class SignUpWindow extends AbstractTerminalWindow {

  private String userName;
  private String passwordString;
  private String emailAddress;

  SignUpWindow(TerminalWindow caller) {
    super(caller, new HashMap<Integer, String>() {{
      put(0, ConstantStrings.kEmailAddressString);
      put(1, ConstantStrings.kUserNameString);
      put(2, ConstantStrings.kPasswordString);
      put(3, ConstantStrings.kReEnterPasswordString);
    }});
  }

  @Override
  void inputFetchedFromUser(String inputString) {
    if (getCurrentProcess() == 0) {
      emailAddress = inputString;
      printInConsoleForNextProcess();
    }
    else if (getCurrentProcess() == 1) {
      userName = inputString;
      printInConsoleForNextProcess();
    }
    else if (getCurrentProcess() == 2) {
      passwordString = inputString;
      printInConsoleForNextProcess();
    }
    else if (getCurrentProcess() == 3) {
      if (!inputString.equals(passwordString)) {
        printMessageInConsole(ConstantStrings.kPasswordsDoNotMatch);
        printInConsoleForProcess(2);
      }
      else  {
        createUser();
      }
    }
    else {
      if (inputString.length() == 1) {
        if (inputString.equals("1")) {
          printInConsoleForProcess(0);
        }
        else if (inputString.equals("0")) {
          goBack();
        }
        else if (inputString.equals("*")) {
          exitWindow();
        }
        else {
          invalidInputPassed();
        }
      }
      else {
        invalidInputPassed();
      }
    }
  }

  private void createUser() {
    try {
      sendNetworkConnection(new NetworkRequestFactory().createUserRequest(userName,
              emailAddress, passwordString));
    }
    catch (IOException exception) {
      printMessageInConsole(ConstantStrings.kNetworkError);
      printMessageInConsole(exception.getMessage());
    }
  }
}
