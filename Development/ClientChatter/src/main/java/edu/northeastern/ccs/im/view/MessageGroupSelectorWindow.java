package edu.northeastern.ccs.im.view;

import java.util.HashMap;

import edu.northeastern.ccs.im.communication.ClientConnectionFactory;

public class MessageGroupSelectorWindow extends AbstractTerminalWindow {

  private TerminalWindow messageWindow;
  private String chatName;

  public MessageGroupSelectorWindow(TerminalWindow caller, ClientConnectionFactory clientConnectionFactory) {
    super(caller, new HashMap<Integer, String>() {{
      put(0, ConstantStrings.CHAT_GROUP_SELECTOR);
      put(1, ConstantStrings.CHAT_USER_SELECTED);
      put(2, ConstantStrings.CHAT_GROUP_SELECTED);
      put(3, ConstantStrings.INVALID_CHAT);
    }}, clientConnectionFactory);
  }

  public TerminalWindow getMessageWindow(String chatId, boolean isPrivate) {
    messageWindow = new MessageWindow(this, clientConnectionFactory, chatId, isPrivate);
    return messageWindow;
  }

  @Override
  void inputFetchedFromUser(String inputString) {
    if (getCurrentProcess() == 1) {
      chatName = UserConstants.getUserName() + "_" + inputString;
      if (UserConstants.getUserName().compareTo(inputString) > 0) {
        chatName = inputString + "_" + UserConstants.getUserName();
      }
      else  {
        chatName = UserConstants.getUserName() + "_" + inputString;
      }
      getMessageWindow(chatName, true).runWindow();
    }
    else if (getCurrentProcess() == 2) {
      chatName = inputString;
      getMessageWindow(chatName, false).runWindow();
    }
    if ((getCurrentProcess() == 3) && (inputString.equals("1"))) {
      printInConsoleForProcess(0);
    } else if (inputString.equals("1")) {
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
}
