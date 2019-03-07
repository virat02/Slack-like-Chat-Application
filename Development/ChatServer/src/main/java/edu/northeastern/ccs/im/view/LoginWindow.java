package edu.northeastern.ccs.im.view;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;

import edu.northeastern.ccs.im.communication.NetworkRequest;
import edu.northeastern.ccs.im.communication.NetworkRequestFactory;

class LoginWindow extends AbstractTerminalWindow {

  private TerminalWindow chatTerminalWindow;
  private TerminalWindow forgotPasswordWindow;

  public TerminalWindow getChatTerminalWindow(){
    if (chatTerminalWindow == null) {
      chatTerminalWindow = new ChatTerminalWindow(this);
    }
    return chatTerminalWindow;
  }

  public TerminalWindow getForgotPasswordWindow(){
    if ( forgotPasswordWindow == null ){
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
    }
    else if (getCurrentProcess() == 1) {
      passwordString = inputString;
      if (isUserDetailsValid()) {
        printMessageInConsole(ConstantStrings.kLoginSuccessful);
        getChatTerminalWindow().runWindow();
      }
      else  {
        printInConsoleForNextProcess();
      }
    }
    else {
      if (inputString.length() == 1) {
        if (inputString.equals("1")) {
          printInConsoleForProcess(0);
        }
        else if (inputString.equals("2")) {
          getForgotPasswordWindow().runWindow();
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

  private boolean isUserDetailsValid() {
    return false;
  }
}
