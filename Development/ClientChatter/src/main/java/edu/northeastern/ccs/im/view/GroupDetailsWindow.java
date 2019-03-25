package edu.northeastern.ccs.im.view;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import edu.northeastern.ccs.im.communication.ClientConnectionFactory;
import edu.northeastern.ccs.im.communication.NetworkRequestFactory;
import edu.northeastern.ccs.im.communication.NetworkResponse;
import edu.northeastern.ccs.im.userGroup.Group;
import edu.northeastern.ccs.im.userGroup.Profile;
import edu.northeastern.ccs.im.userGroup.User;

public class GroupDetailsWindow extends AbstractTerminalWindow {

  private int currentProcess = 0;
  private String groupName;

  public GroupDetailsWindow(TerminalWindow caller, ClientConnectionFactory clientConnectionFactory) {
    super(caller, new HashMap<Integer, String>() {{
      put(0, ConstantStrings.GROUP_PROPERTIES);
      put(1, ConstantStrings.GROUP_UPDATE);
      put(2, ConstantStrings.GROUP_UPDATE_USER);
      put(3, ConstantStrings.GROUP_UPDATE_PASSWORD);
    }}, clientConnectionFactory);
  }

  @Override
  void inputFetchedFromUser(String inputString) {
    if (getCurrentProcess() == 0) {
      if (inputString.equals("1")) {
        currentProcess = 1;
        printInConsoleForProcess(1);
      } else if (inputString.equals("2")) {
        currentProcess = 2;
        printInConsoleForProcess(1);
      } else if (inputString.equals("3")) {
        currentProcess = 3;
        printInConsoleForProcess(1);
      } else if (inputString.equals("4")) {
        currentProcess = 4;
        printInConsoleForProcess(1);
      }else if (inputString.equals("0")) {
        goBack();
      } else if (inputString.equals("*")) {
        exitWindow();
      } else {
        invalidInputPassed();
      }
    } else if (getCurrentProcess() == 1) {
      groupName = inputString;
      if ((currentProcess == 1) || (currentProcess == 2)) {
        printInConsoleForProcess(2);
      } else if (currentProcess == 3) {
        groupName = inputString;
        printInConsoleForProcess(3);
      } else if (currentProcess == 4) {
        Group group = searchForGroup(groupName);
        if (group != null) {
          printMessageInConsole("Group Name: " + group.getName());
          printMessageInConsole("Group Code: " + group.getGroupCode());

          StringBuilder moderator = new StringBuilder("Moderators: ");
          if (group.getModerators().size() > 0) {
            moderator.append(group.getModerators().get(0).getUsername());
            for (int index = 1 ; index < group.getModerators().size() ; index ++) {
              User user = group.getModerators().get(index);
              moderator.append(", ");
              moderator.append(user.getUsername());
            }
          }
          else {
            moderator.append("-");
          }
          printMessageInConsole(moderator.toString());

          StringBuilder usersString = new StringBuilder("Users: ");
          if (group.getUsers().size() > 0) {
            usersString.append(group.getUsers().get(0).getUsername());
            for (int index = 1 ; index < group.getUsers().size() ; index ++) {
              User user = group.getUsers().get(index);
              usersString.append(", ");
              usersString.append(user.getUsername());
            }
          }
          else {
            usersString.append("-");
          }
          printMessageInConsole(usersString.toString());
          if (group.getModerators().stream().filter(obj -> obj.getId()
                  == UserConstants.getUserId()).collect(Collectors.toList()).size() > 0) {
            printMessageInConsole("Password: " + group.getGroupPassword());
          }
          else {
            printMessageInConsole("Password: ******");
          }
          printInConsoleForProcess(0);
        }
        else {
          printMessageInConsole("No Groups");
          printInConsoleForProcess(0);
        }
      }
      else {
        invalidInputPassed();
      }
    } else if (getCurrentProcess() == 2) {
      Group group = searchForGroup(groupName);
      if (group != null) {

        User user = searchForUsers(inputString);
        if (user != null) {
          List<User> groupUsers = group.getUsers();
          if (group.getModerators().stream().filter(obj -> obj.getId()
                  == UserConstants.getUserId()).collect(Collectors.toList()).size() > 0) {
            if (currentProcess == 1){
              if (groupUsers.stream().filter(obj -> obj.getId() == user.getId()).collect(Collectors.toList()).size() > 0) {
                printMessageInConsole(ConstantStrings.GROUP_USER_PRESENT);
              }
              else {
                groupUsers.add(user);
                group.setUsers(groupUsers);
                if (updateGroup(group)) {
                  printMessageInConsole(ConstantStrings.GROUP_UPDATE_SUCCESSFUL);
                }
                else {
                  printMessageInConsole(ConstantStrings.GROUP_UPDATE_FAILED);
                }
              }
            }
            else {
              if (groupUsers.stream().filter(obj -> obj.getId() == user.getId()).collect(Collectors.toList()).size() > 0) {

                User currentUser = groupUsers.stream().filter(obj -> obj.getId() == user.getId())
                        .collect(Collectors.toList()).get(0);

                groupUsers.remove(currentUser);
                group.setUsers(groupUsers);
                if (updateGroup(group)) {
                  printMessageInConsole(ConstantStrings.GROUP_UPDATE_SUCCESSFUL);
                }
                else {
                  printMessageInConsole(ConstantStrings.GROUP_UPDATE_FAILED);
                }
              }
              else {
                printMessageInConsole(ConstantStrings.GROUP_USER_ABSENT);
              }
            }
          }
          else {
            printMessageInConsole(ConstantStrings.GROUP_USER_NO_PERMISSION);
          }
        }
        else {
          printMessageInConsole(ConstantStrings.USER_INVALID);
        }

        printInConsoleForProcess(0);
      }
      else {
        printMessageInConsole("No Groups");
        printInConsoleForProcess(0);
      }
    } else if (getCurrentProcess() == 3) {
      Group group = searchForGroup(groupName);
      if (group != null) {
        if (group.getModerators().stream().filter(obj -> obj.getId()
                == UserConstants.getUserId()).collect(Collectors.toList()).size() > 0) {
          group.setGroupPassword(inputString);
          if (updateGroup(group)) {
            printMessageInConsole(ConstantStrings.GROUP_UPDATE_SUCCESSFUL);
          }
          else {
            printMessageInConsole(ConstantStrings.GROUP_UPDATE_FAILED);
          }
        } else {
          printMessageInConsole(ConstantStrings.GROUP_USER_NO_PERMISSION);
        }

        printInConsoleForProcess(0);
      }
      else {
        printMessageInConsole("No Groups");
        printInConsoleForProcess(0);
      }
    } else if (inputString.equals("0")) {
      goBack();
    } else if (inputString.equals("*")) {
      exitWindow();
    } else {
      invalidInputPassed();
    }
  }

  private boolean updateGroup(Group group) {
    try {
      NetworkResponse networkResponse = sendNetworkConnection(networkRequestFactory
              .createUpdateGroupRequest(group));
      return networkResponse.status().equals(NetworkResponse.STATUS.SUCCESSFUL);
    } catch (IOException exception) {
      /* TODO Provide some good custom message */
      printMessageInConsole(ConstantStrings.NETWORK_ERROR);
    }
    return false;
  }

  private User searchForUsers(String inputString) {
    try {
      NetworkResponse networkResponse = sendNetworkConnection(networkRequestFactory
              .createSearchUserRequest(inputString));
      return ResponseParser.parseSearchUserNetworkResponse(networkResponse);
    } catch (IOException | NetworkResponseFailureException exception) {
      /* TODO Provide some good custom message */
      printMessageInConsole(ConstantStrings.NETWORK_ERROR);
    }
    return null;
  }

  private Group searchForGroup(String inputString) {
    try {
      NetworkResponse networkResponse = sendNetworkConnection(networkRequestFactory
              .createGetGroupRequest(inputString));
      return ResponseParser.parseGroupNetworkResponse(networkResponse);
    } catch (IOException | NetworkResponseFailureException exception) {
      /* TODO Provide some good custom message */
      printMessageInConsole(ConstantStrings.NETWORK_ERROR);
    }
    return null;
  }
}
