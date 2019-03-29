package edu.northeastern.ccs.im.view;

import java.util.HashMap;

import edu.northeastern.ccs.im.communication.ClientConnectionFactory;
import edu.northeastern.ccs.im.communication.NetworkRequestFactory;
import edu.northeastern.ccs.im.communication.NetworkResponse;

public class CreateGroupWindow extends AbstractTerminalWindow {

  private String groupName;
  private String groupCode;
  public CreateGroupWindow(TerminalWindow caller, ClientConnectionFactory clientConnectionFactory) {
    super(caller, new HashMap<Integer, String>() {{
      put(0, ConstantStrings.CREATE_GROUP);
      put(1, ConstantStrings.CREATE_GROUP_CODE);
      put(2, ConstantStrings.DEFAULT_MENU);
      put(3, ConstantStrings.FAILURE_MENU);
    }}, clientConnectionFactory);
  }

  @Override
  void inputFetchedFromUser(String inputString) {
    if (getCurrentProcess() == 0) {
      groupName = inputString;
      printInConsoleForNextProcess();
    } else if (getCurrentProcess() == 1) {
      groupCode = inputString;
      if (createGroup(groupName,groupCode)) {
        printMessageInConsole(ConstantStrings.CREATE_GROUP_SUCCESS);
        printInConsoleForProcess(2);
      }
      else {
        printMessageInConsole(ConstantStrings.CREATE_GROUP_FAILED);
        printInConsoleForProcess(3);
      }
    }
    else {
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

  private boolean createGroup(String groupName,String groupCode) {
    try {
      NetworkResponse networkResponse = sendNetworkConnection(new NetworkRequestFactory()
              .createGroupRequest(groupName,groupCode, UserConstants.getUserObj()));

      return ResponseParser.parseAddGroupResponse(networkResponse) != null;
    } catch (NetworkResponseFailureException exception) {
      printMessageInConsole(exception.getMessage());
      return false;
    }
  }
}
