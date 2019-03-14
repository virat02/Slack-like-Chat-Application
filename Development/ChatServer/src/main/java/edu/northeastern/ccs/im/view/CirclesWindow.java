package edu.northeastern.ccs.im.view;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import edu.northeastern.ccs.im.communication.ClientConnectionFactory;
import edu.northeastern.ccs.im.communication.NetworkRequestFactory;
import edu.northeastern.ccs.im.communication.NetworkResponse;
import edu.northeastern.ccs.im.userGroup.User;

public class CirclesWindow extends AbstractTerminalWindow {

  private int currentOperation = 0;

  protected CirclesWindow(TerminalWindow callerWindow, ClientConnectionFactory clientConnectionFactory) {
    super(callerWindow, new HashMap<Integer, String>() {{
      put(0, ConstantStrings.CIRCLE_MENU);
      put(1, ConstantStrings.USERS_FOLLOWED_USER);
    }}, clientConnectionFactory);
  }

  @Override
  void inputFetchedFromUser(String inputString) {
    if (getCurrentProcess() == 0) {
      if (inputString.equals("1")) {
        getUsersFollowed();
        printInConsoleForProcess(0);
      } else if (inputString.equals("2")) {
        getUserFollowees();
        printInConsoleForProcess(0);
      } else if (inputString.equals("3")) {
        currentOperation = 3;
        printInConsoleForProcess(1);
      } else if (inputString.equals("4")) {
        currentOperation = 4;
        printInConsoleForProcess(1);
      } else if (inputString.equals("5")) {
        currentOperation = 5;
        printInConsoleForProcess(1);
      } else if (inputString.equals("0")) {
        goBack();
      } else if (inputString.equals("*")) {
        exitWindow();
      } else {
        invalidInputPassed();
      }
    }
    else if (getCurrentProcess() == 1) {
      if (currentOperation == 3) {
        getUserFollowedByOtherUser();
        printMessageInConsole(ConstantStrings.FOLLOW_SUCCESSFUL);
        printInConsoleForProcess(0);
      } else if (currentOperation == 4) {
        followUser();
        printMessageInConsole(ConstantStrings.FOLLOW_FAILED);
        printInConsoleForProcess(0);
      } else if (currentOperation == 5) {
        followUser();
        printMessageInConsole(ConstantStrings.FOLLOW_SUCCESSFUL);
        printInConsoleForProcess(0);
      }
      else {
        invalidInputPassed();
      }
    } else {
      invalidInputPassed();
    }
  }

  private List<User> getUsersFollowed() {
    NetworkResponse networkResponse;
    try {
      networkResponse = sendNetworkConnection(new NetworkRequestFactory()
              .createGetUserFollowersList(UserConstants.getUserName()));
      return ResponseParser.parseFollowersList(networkResponse);
    } catch (IOException | NetworkResponseFailureException e) {
      printMessageInConsole(ConstantStrings.FETCH_DATA_FAILED);
      printInConsoleForProcess(0);
    }
    return null;
  }

  private List<User> getUserFollowees() {
    NetworkResponse networkResponse;
    try {
      networkResponse = sendNetworkConnection(new NetworkRequestFactory()
              .createGetUserFolloweesList(UserConstants.getUserName()));
      return ResponseParser.parseFollowersList(networkResponse);
    } catch (IOException | NetworkResponseFailureException e) {
      printMessageInConsole(ConstantStrings.FETCH_DATA_FAILED);
      printInConsoleForProcess(0);
    }
    return null;
  }

  private void getUserFollowedByOtherUser() {
//    NetworkResponse networkResponse;
//    try {
//      networkResponse = sendNetworkConnection(new NetworkRequestFactory()
//              .createGetUserFollowersList(UserConstants.getUserName()));
//      return ResponseParser.parseFollowersList(networkResponse);
//    } catch (IOException | NetworkResponseFailureException e) {
//      printMessageInConsole(ConstantStrings.FETCH_DATA_FAILED);
//      printInConsoleForProcess(0);
//    }
//    return null;
  }

  private void followUser() {
//    NetworkResponse networkResponse;
//    try {
//      networkResponse = sendNetworkConnection(new NetworkRequestFactory()
//              .createGetUserFollowersList(UserConstants.getUserName()));
//      return ResponseParser.parseFollowersList(networkResponse);
//    } catch (IOException | NetworkResponseFailureException e) {
//      printMessageInConsole(ConstantStrings.FETCH_DATA_FAILED);
//      printInConsoleForProcess(0);
//    }
//    return null;
  }

  private void unfollowUser() {

  }
}
