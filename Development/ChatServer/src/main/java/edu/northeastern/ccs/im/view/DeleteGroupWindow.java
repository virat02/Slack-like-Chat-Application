package edu.northeastern.ccs.im.view;

import java.io.IOException;
import java.util.HashMap;

import edu.northeastern.ccs.im.communication.ClientConnectionFactory;
import edu.northeastern.ccs.im.communication.NetworkRequestFactory;
import edu.northeastern.ccs.im.communication.NetworkResponse;

public class DeleteGroupWindow extends AbstractTerminalWindow {
	
	private String groupName;
	private String groupCode;
  public DeleteGroupWindow(TerminalWindow caller, ClientConnectionFactory clientConnectionFactory) {
    super(caller, new HashMap<Integer, String>() {{
      put(0, ConstantStrings.DELETE_GROUP);
      put(1, ConstantStrings.CREATE_GROUP_CODE);
      put(2, ConstantStrings.DELETE_GROUP_SUCCESS);
      put(3, ConstantStrings.DELETE_GROUP_FAILED);
    }}, clientConnectionFactory);
  }

  @Override
  void inputFetchedFromUser(String inputString) {
	  if (getCurrentProcess() == 0) {
	    	 groupName = inputString;
	    	 printInConsoleForNextProcess();
	    } else if (getCurrentProcess() == 1) {
	    	groupCode = inputString;
	      if (deleteGroup(groupName,groupCode)) {
	        printInConsoleForNextProcess();
	      }
	      else {
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

  private boolean deleteGroup(String groupName,String groupCode) {
    try {
      NetworkResponse networkResponse = sendNetworkConnection(new NetworkRequestFactory()
              .createDeleteGroupRequest(groupName,groupCode));

      return ResponseParser.parseDeleteGroupResponse(networkResponse);
    } catch (IOException exception) {
      // TODO Provide some good custom message
      printMessageInConsole(ConstantStrings.NETWORK_ERROR);
      return false;
    }
  }
}
