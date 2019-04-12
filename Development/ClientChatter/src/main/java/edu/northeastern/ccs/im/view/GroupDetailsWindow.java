package edu.northeastern.ccs.im.view;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import edu.northeastern.ccs.im.communication.ClientConnectionFactory;
import edu.northeastern.ccs.im.communication.NetworkRequestFactory;
import edu.northeastern.ccs.im.communication.NetworkResponse;
import edu.northeastern.ccs.im.userGroup.Group;
import edu.northeastern.ccs.im.userGroup.User;

public class GroupDetailsWindow extends AbstractTerminalWindow {

  private Group baseGroup;
  private int currentProcess = 0;
  private String groupName;
  private Group group;
  private String subGroupName;
  private String subGroupId;

  public GroupDetailsWindow(TerminalWindow caller, ClientConnectionFactory clientConnectionFactory) {
    super(caller, new HashMap<Integer, String>() {{
      put(0, ConstantStrings.GROUP_UPDATE);
      put(1, ConstantStrings.GROUP_NOT_FOUND);
      put(2, ConstantStrings.GROUP_PROPERTIES);
      put(3, ConstantStrings.GROUP_UPDATE_USER);
      put(4, ConstantStrings.GROUP_UPDATE_PASSWORD);
      put(5, ConstantStrings.CREATE_GROUP);
      put(6, ConstantStrings.CREATE_GROUP_CODE);
      put(7, ConstantStrings.GROUP_UPDATE);
    }}, clientConnectionFactory);
  }

  public GroupDetailsWindow(TerminalWindow caller,
                            ClientConnectionFactory clientConnectionFactory, Group baseGroup,
                            Group currentGroup) {
    this(caller, clientConnectionFactory);
    this.baseGroup = baseGroup;
    this.group = currentGroup;
    this.groupName = currentGroup.getName();
  }

  @Override
  public void runWindow() {
    if (baseGroup == null && group == null) {
      super.runWindow();
    } else {
      printMessageInConsole("Group - " + groupName);
      printInConsoleForProcess(2);
    }
  }

  @Override
  public void goBack() {
    baseGroup = null;
    groupName = null;
    group = null;
    subGroupName = null;
    subGroupId = null;
    super.goBack();
  }

  @Override
  protected String helpCommand() {
    return "View, access and modify groups which you have created";
  }

  @Override
  void inputFetchedFromUser(String inputString) {
    if (getCurrentProcess() == 0) {
      groupName = inputString;
      currentProcess = 0;
      group = searchForGroup(groupName);
      if (group != null) {
        printInConsoleForProcess(2);
      }
      else {
        printInConsoleForProcess(1);
      }
    } else if (getCurrentProcess() == 1) {
      if (inputString.equals("1")) {
        printInConsoleForProcess(0);
      } else if (inputString.equals("0")) {
        goBack();
      } else if (inputString.equals("*")) {
        exitWindow();
      } else {
        invalidInputPassed();
      }
    } else if (getCurrentProcess() == 2) {
      if (inputString.equals("1")) {
        currentProcess = 1;
        printInConsoleForProcess(3);
      } else if (inputString.equals("2")) {
        currentProcess = 2;
        printInConsoleForProcess(3);
      } else if (inputString.equals("3")) {
        printInConsoleForProcess(4);
      } else if (inputString.equals("4")) {
        printGroupDetails();
        printInConsoleForProcess(2);
      } else if (inputString.equals("5")) {
        printInConsoleForProcess(5);
      } else if (inputString.equals("6")) {
        printInConsoleForProcess(7);
      } else if (inputString.equals("0")) {
        goBack();
      } else if (inputString.equals("*")) {
        exitWindow();
      } else {
        invalidInputPassed();
      }
    } else if (getCurrentProcess() == 3) {
      addOrRemoveUser(inputString);
      printInConsoleForProcess(2);
    } else if (getCurrentProcess() == 4) {
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
      printInConsoleForProcess(2);
    } else if (getCurrentProcess() == 5) {
      subGroupName = inputString;
      printInConsoleForProcess(6);
    } else if (getCurrentProcess() == 6) {
      subGroupId = inputString;
      if (createSubGroup(subGroupName, subGroupId)) {
        printMessageInConsole(ConstantStrings.CREATE_GROUP_SUCCESS);
        printInConsoleForProcess(2);
      }
      else {
        printMessageInConsole(ConstantStrings.CREATE_GROUP_FAILED);
        printInConsoleForProcess(2);
      }
    } else if (getCurrentProcess() == 7) {
      List<Group> subGroups = group.getGroups().stream()
              .filter(obj -> obj.getGroupCode().equals(inputString)).collect(Collectors.toList());
      if (subGroups.size() != 0) {
        new GroupDetailsWindow(this, clientConnectionFactory, group, subGroups.get(0))
                .runWindow();
      }
      else {
        printMessageInConsole(ConstantStrings.GROUP_NOT_FOUND_MESSAGE);
        printInConsoleForProcess(2);
      }
    }
  }

  private void printGroupDetails() {
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

    StringBuilder subGroupsBuilder = new StringBuilder("Sub Groups: ");
    if (group.getGroups().size() > 0) {
      subGroupsBuilder.append(group.getGroups().get(0).getName());
      for (int index = 1 ; index < group.getUsers().size() ; index ++) {
        Group subGroup = group.getGroups().get(index);
        subGroupsBuilder.append(", ");
        subGroupsBuilder.append(subGroup.getName());
      }
    }
    else {
      subGroupsBuilder.append("-");
    }
    printMessageInConsole(subGroupsBuilder.toString());

    if (group.getModerators().stream().filter(obj -> obj.getId()
            == UserConstants.getUserId()).collect(Collectors.toList()).size() > 0) {
      printMessageInConsole("Password: " + group.getGroupPassword());
    }
    else {
      printMessageInConsole("Password: ******");
    }
  }

  private void addOrRemoveUser(String inputString) {
    User user = searchForUsers(inputString);
    if (user != null) {
      List<User> groupUsers = group.getUsers();
      if (group.getModerators().stream().filter(obj -> obj.getId()
              == UserConstants.getUserId()).collect(Collectors.toList()).size() > 0) {
        if (currentProcess == 1) {
          if (groupUsers.stream().filter(obj -> obj.getId() == user.getId()).collect(Collectors.toList()).size() > 0) {
            printMessageInConsole(ConstantStrings.GROUP_USER_PRESENT);
          }
          else {
            if (baseGroup != null && baseGroup.getUsers()
                    .stream().filter(obj -> obj.getUsername().equals(user.getUsername()))
                    .count() == 0) {
              printMessageInConsole(ConstantStrings.GROUP_USER_ABSENT_PARENT);
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
  }

  private boolean updateGroup(Group group) {
    try {
      NetworkResponse networkResponse = sendNetworkConnection(networkRequestFactory
              .createUpdateGroupRequest(group));
      return networkResponse.status().equals(NetworkResponse.STATUS.SUCCESSFUL);
    } catch (NetworkResponseFailureException exception) {
      printMessageInConsole(exception.getMessage());
    }
    return false;
  }

  private User searchForUsers(String inputString) {
    try {
      NetworkResponse networkResponse = sendNetworkConnection(networkRequestFactory
              .createSearchUserRequest(inputString));
      return ResponseParser.parseSearchUserNetworkResponse(networkResponse);
    } catch (NetworkResponseFailureException exception) {
      printMessageInConsole(exception.getMessage());
    }
    return null;
  }

  private Group searchForGroup(String inputString) {
    try {
      NetworkResponse networkResponse = sendNetworkConnection(networkRequestFactory
              .createGetGroupRequest(inputString));
      return ResponseParser.parseGroupNetworkResponse(networkResponse);
    } catch (NetworkResponseFailureException exception) {
      printMessageInConsole(exception.getMessage());
    }
    return null;
  }

  private boolean createSubGroup(String groupName, String groupCode) {
    try {
      NetworkResponse networkResponse = sendNetworkConnection(new NetworkRequestFactory()
              .createGroupRequest(groupName,groupCode, UserConstants.getUserObj()));
      Group subGroup = ResponseParser.parseAddGroupResponse(networkResponse);
      if (subGroup != null) {
        List<Group> subGroups = group.getGroups();
        subGroups.add(subGroup);
        group.setGroups(subGroups);
        return updateGroup(group);
      }
    } catch (NetworkResponseFailureException exception) {
      printMessageInConsole(exception.getMessage());
    }
    return false;
  }
}
