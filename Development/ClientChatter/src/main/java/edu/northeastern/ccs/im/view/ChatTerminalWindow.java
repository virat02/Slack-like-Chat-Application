package edu.northeastern.ccs.im.view;

import java.util.HashMap;
import java.util.Map;

import edu.northeastern.ccs.im.communication.ClientConnectionFactory;

public class ChatTerminalWindow extends AbstractTerminalWindow {

    Map<Integer, AbstractTerminalWindow> mapper = new HashMap<>();

    public ChatTerminalWindow(TerminalWindow caller, ClientConnectionFactory clientConnectionFactory) {
        super(caller, new HashMap<Integer, String>() {{
            put(0, ConstantStrings.CHAT_MAIN_COMMAND);
        }}, clientConnectionFactory);
    }

    @Override
    void inputFetchedFromUser(String inputString) {
        if (inputString.length() <= 2) {
            if (inputString.equals("1")) {
                MessageGroupSelectorWindow selectorWindow = (MessageGroupSelectorWindow) mapper.computeIfAbsent(1,
                        e -> new MessageGroupSelectorWindow(this, clientConnectionFactory));
                selectorWindow.runWindow();
            } else if (inputString.equals("2")) {
                SearchWindow searchWindow = (SearchWindow) mapper.computeIfAbsent(2,
                        e -> new SearchWindow(this, clientConnectionFactory));
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
                        .computeIfAbsent(4, e -> new DeleteGroupWindow(this,
                                clientConnectionFactory));
                deleteGroupWindowWindow.runWindow();
            } else if (inputString.equals("6")) {
                GroupDetailsWindow groupDetailsWindowWindow = (GroupDetailsWindow) mapper
                        .computeIfAbsent(5, e -> new GroupDetailsWindow(this,
                                clientConnectionFactory));
                groupDetailsWindowWindow.runWindow();
            } else if (inputString.equals("7")) {
                UpdateProfileWindow updateProfileWindow = (UpdateProfileWindow) mapper
                        .computeIfAbsent(6, e -> new UpdateProfileWindow(this,
                                clientConnectionFactory));
                updateProfileWindow.runWindow();
            } else if (inputString.equals("8")) {
                CirclesWindow circlesWindow = (CirclesWindow) mapper
                        .computeIfAbsent(7, e -> new CirclesWindow(this,
                                clientConnectionFactory));
                circlesWindow.runWindow();
            } else if (inputString.equals("9")) {
                signOutApp();
            } else if (inputString.equals("10"))   {
                ViewInvitationsWindow viewInvitationsWindow = new ViewInvitationsWindow(this, clientConnectionFactory);
                viewInvitationsWindow.runWindow();
            } else if (inputString.equals("*")) {
                exitWindow();
            } else {
                invalidInputPassed();
            }
        } else {
            invalidInputPassed();
        }
    }
}
