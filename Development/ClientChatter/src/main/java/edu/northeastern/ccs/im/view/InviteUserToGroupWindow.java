package edu.northeastern.ccs.im.view;

import edu.northeastern.ccs.im.communication.ClientConnectionFactory;
import edu.northeastern.ccs.im.communication.NetworkRequest;
import edu.northeastern.ccs.im.communication.NetworkResponse;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InviteUserToGroupWindow extends AbstractTerminalWindow {

    private String userName = "";

    public InviteUserToGroupWindow(TerminalWindow terminalWindow, ClientConnectionFactory clientConnectionFactory) {
        super(terminalWindow,
                Stream.of(new AbstractMap.SimpleEntry<>(0, ConstantStrings.USER_NAME_STRING),
                        new AbstractMap.SimpleEntry<>(1, ConstantStrings.CREATE_GROUP_CODE),
                        new AbstractMap.SimpleEntry<>(2, ConstantStrings.FAILURE_MENU))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                , clientConnectionFactory);
    }

    @Override
    public void inputFetchedFromUser(String inputString) {
        if (getCurrentProcess() == 0) {
            userName = inputString;
            printInConsoleForNextProcess();
        } else if (getCurrentProcess() == 1) {
            sendGroupInvite(userName, inputString);
        }
        else    {
            if (inputString.equals("1"))    {
                printInConsoleForProcess(0);
            }
            else if (inputString.equals("0")) {
                goBack();
            } else if (inputString.equals("*")) {
                exitWindow();
            }
        }
    }

    private void sendGroupInvite(String userName, String groupCode) {
        NetworkRequest inviteGroupRequest = networkRequestFactory.createGroupInviteRequest(userName, UserConstants.getUserName(), groupCode);
        try {
            NetworkResponse networkResponse = sendNetworkConnection(inviteGroupRequest);
            ResponseParser.parseNetworkResponse(networkResponse);
        } catch (IOException | NetworkResponseFailureException exception)  {
            ViewConstants.getOutputStream().println(exception.getMessage());
            printInConsoleForProcess(2);
        }

        goBack();
    }

    public static void main(String args[]) {
        InviteUserToGroupWindow inviteUserToGroupWindow = new InviteUserToGroupWindow(null, new ClientConnectionFactory());
        inviteUserToGroupWindow.runWindow();
    }
}
