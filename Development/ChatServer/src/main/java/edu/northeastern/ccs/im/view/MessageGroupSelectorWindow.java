package edu.northeastern.ccs.im.view;

import java.util.HashMap;

import edu.northeastern.ccs.im.communication.ClientConnectionFactory;

public class MessageGroupSelectorWindow extends AbstractTerminalWindow {

  private TerminalWindow messageWindow;
  private boolean isGroupChatSelected;
  private String chatName;

  public MessageGroupSelectorWindow(TerminalWindow caller, ClientConnectionFactory clientConnectionFactory) {
    super(caller, new HashMap<Integer, String>() {{
      put(0, ConstantStrings.CHAT_GROUP_SELECTOR);
      put(1, ConstantStrings.CHAT_USER_SELECTED);
      put(2, ConstantStrings.CHAT_GROUP_SELECTED);
      put(3, ConstantStrings.INVALID_CHAT);
    }}, clientConnectionFactory);
  }

  public TerminalWindow getMessageWindow(String chatId) {
    if (messageWindow == null) {
      messageWindow = new MessageWindow(this, clientConnectionFactory, chatId);
    }
    return messageWindow;
  }

  @Override
  void inputFetchedFromUser(String inputString) {
    if (getCurrentProcess() == 1) {
      isGroupChatSelected = true;
      chatName = inputString;
      if (checkIfChatIsValid(chatName)) {
        getMessageWindow(chatName).runWindow();
      }
      else {
        printInConsoleForProcess(3);
      }
    }
    else if (getCurrentProcess() == 2) {
      isGroupChatSelected = true;
      chatName = UserConstants.getUserName() + "_" + inputString;
      if (UserConstants.getUserName().compareTo(inputString) > 0) {
        chatName = inputString + "_" + UserConstants.getUserName();
      }
      if (checkIfChatIsValid(chatName)) {
        getMessageWindow(chatName).runWindow();
      }
      else {
        printInConsoleForProcess(3);
      }
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

  private boolean checkIfChatIsValid(String chatName) {
    /* TODO Must check if the given chat name is valid */
    return true;
  }
}
