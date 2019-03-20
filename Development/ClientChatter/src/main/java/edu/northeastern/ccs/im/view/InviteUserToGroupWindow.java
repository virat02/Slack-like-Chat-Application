package edu.northeastern.ccs.im.view;

import edu.northeastern.ccs.im.communication.ClientConnectionFactory;
import edu.northeastern.ccs.im.communication.NetworkRequestFactory;
import edu.northeastern.ccs.im.communication.NetworkResponse;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InviteUserToGroupWindow extends AbstractTerminalWindow {

    private NetworkRequestFactory networkRequestFactory = new NetworkRequestFactory();
    private String userName = "";
    public InviteUserToGroupWindow(TerminalWindow terminalWindow, ClientConnectionFactory clientConnectionFactory) {
        super(terminalWindow,
                Stream.of(new AbstractMap.SimpleEntry<>(1, ConstantStrings.USER_NAME_STRING))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                ,clientConnectionFactory);
    }

    @Override
    public void inputFetchedFromUser(String inputString) {
        if (getCurrentProcess() == 0) {
            userName = inputString;
            printInConsoleForNextProcess();
        }
        else if (getCurrentProcess() == 1)  {
            sendGroupInvite(userName, inputString);
        }
    }

    private void sendGroupInvite(String userName, String groupCode) {
        // TODO
        // Perform network connection, show response,
        // I am not sure what if it fails, or success, what should be the next flow.
        try {
            NetworkResponse networkResponse = sendNetworkConnection(networkRequestFactory.createGroupInviteRequest(userName, groupCode));
            printInConsoleForNextProcess(networkResponse.payload().jsonString());
        } catch (IOException e) {
            // TODO ask Tarun about the control flow.
        }

    }
}
