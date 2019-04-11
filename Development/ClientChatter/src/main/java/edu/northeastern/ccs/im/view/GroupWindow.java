package edu.northeastern.ccs.im.view;

import java.util.HashMap;
import java.util.Map;

import edu.northeastern.ccs.im.communication.ClientConnectionFactory;
import edu.northeastern.ccs.im.userGroup.Group;

public class GroupWindow extends AbstractTerminalWindow {

  Map<Integer, AbstractTerminalWindow> mapper = new HashMap<>();

  public GroupWindow(TerminalWindow caller, ClientConnectionFactory clientConnectionFactory) {
    super(caller, new HashMap<Integer, String>() {{
      put(0, ConstantStrings.GROUP_MAIN_COMMAND);
    }}, clientConnectionFactory);
  }

  @Override
  void inputFetchedFromUser(String inputString) {
    if (inputString.equals("1")) {
      if (UserConstants.getUserObj().getGroups().size() == 0) {
        printMessageInConsole(ConstantStrings.NO_GROUPS_FOUND);
      }
      else {
        printMessageInConsole("----------");
        for (Group group : UserConstants.getUserObj().getGroups()) {
          printMessageInConsole("Group Name: " + group.getName());
          printMessageInConsole("Group Code: " + group.getGroupCode());
          printMessageInConsole("----------");
        }
      }
      printInConsoleForProcess(0);
    } else if (inputString.equals("2")) {
      SearchWindow searchWindow = (SearchWindow) mapper.computeIfAbsent(2,
              e -> new SearchWindow(this, clientConnectionFactory, false));
      searchWindow.runWindow();
    } else if (inputString.equals("3")) {
      CreateGroupWindow createGroupWindowWindow = (CreateGroupWindow) mapper
              .computeIfAbsent(3, e -> new CreateGroupWindow(this,
                      clientConnectionFactory));
      createGroupWindowWindow.runWindow();
    } else if (inputString.equals("4")) {
      InviteUserToGroupWindow inviteUserToGroupWindow = (InviteUserToGroupWindow) mapper.computeIfAbsent(
              4, e -> new InviteUserToGroupWindow(this, clientConnectionFactory));
      inviteUserToGroupWindow.runWindow();
    } else if (inputString.equals("5")) {
      DeleteGroupWindow deleteGroupWindowWindow = (DeleteGroupWindow) mapper
              .computeIfAbsent(5, e -> new DeleteGroupWindow(this,
                      clientConnectionFactory));
      deleteGroupWindowWindow.runWindow();
    } else if (inputString.equals("6")) {
      GroupDetailsWindow groupDetailsWindowWindow = (GroupDetailsWindow) mapper
              .computeIfAbsent(6, e -> new GroupDetailsWindow(this,
                      clientConnectionFactory));
      groupDetailsWindowWindow.runWindow();
    } else if (inputString.equals("7")) {
      ViewInvitationsWindow viewInvitationsWindow = (ViewInvitationsWindow) mapper
              .computeIfAbsent(7, e -> new ViewInvitationsWindow(this,
                      clientConnectionFactory));
      viewInvitationsWindow.runWindow();
    } else if (inputString.equals("0")) {
      goBack();
    } else if (inputString.equals("*")) {
      exitWindow();
    } else {
      invalidInputPassed();
    }
  }

  @Override
  protected String helpCommand() {
    return "Access the different operations that can eb performed on a chat group";
  }
}
