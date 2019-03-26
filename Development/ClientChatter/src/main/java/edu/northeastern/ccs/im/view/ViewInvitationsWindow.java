package edu.northeastern.ccs.im.view;

import edu.northeastern.ccs.im.communication.ClientConnectionFactory;
import edu.northeastern.ccs.im.communication.NetworkRequest;
import edu.northeastern.ccs.im.communication.NetworkResponse;
import edu.northeastern.ccs.im.userGroup.Invite;
import edu.northeastern.ccs.im.userGroup.Status;

import javax.swing.text.View;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ViewInvitationsWindow extends AbstractTerminalWindow {
    private String groupCode = "";

    protected ViewInvitationsWindow(TerminalWindow callerWindow, ClientConnectionFactory clientConnectionFactory) {
        super(callerWindow, Stream.of(new AbstractMap.SimpleEntry<>(0, ConstantStrings.CREATE_GROUP_CODE),
                new AbstractMap.SimpleEntry<>(1, ConstantStrings.INVITE_ACCEPT_OR_REJECT),
                new AbstractMap.SimpleEntry<>(2, ConstantStrings.INVITE_ACCEPT_OR_REJECT_FAILED))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)), clientConnectionFactory);
    }

    @Override
    void inputFetchedFromUser(String inputString) {
        if (getCurrentProcess() == 0) {
            fetchInvitations(inputString);
            printInConsoleForProcess(1);
        } else if (getCurrentProcess() == 1) {
            if (inputString.equals("0")) {
                goBack();
            } else if (inputString.equals("*")) {
                exitWindow();
            } else {
                String[] split = inputString.split("\\s+");
                handleInvitation(split[0], split[1]);
            }
        } else if (getCurrentProcess() == 2) {
            if (inputString.equals("1")) {
                printInConsoleForProcess(1);
            } else if (inputString.equals("0")) {
                goBack();
            } else if (inputString.equals("*")) {
                exitWindow();
            }
        }
    }

    private void handleInvitation(String invitationId, String status) {
        Status inviteStatus = Status.NOUPDATE;
        if (status.equals("A")) {
            inviteStatus = Status.ACCEPTED;
        } else if (status.equals("B")) {
            inviteStatus = Status.REJECTED;
        }

        NetworkRequest networkRequest = networkRequestFactory.createUpdateGroupInvite(invitationId, inviteStatus);
        try {
            NetworkResponse networkResponse = sendNetworkConnection(networkRequest);
            ResponseParser.parseNetworkResponse(networkResponse);
            printInConsoleForProcess(1);
        } catch (IOException | NetworkResponseFailureException e) {
            printInConsoleForProcess(2);
        }

        printInConsoleForProcess(1);
    }

    /***
     *
     * @param groupCode -> the group code
     */
    private void fetchInvitations(String groupCode) {
        NetworkRequest networkRequest = networkRequestFactory.fetchInvitationRequest(UserConstants.getUserName(), groupCode);
        try {
            NetworkResponse networkResponse = sendNetworkConnection(networkRequest);
            List<Invite> invitations = ResponseParser.parseInvitationsList(networkResponse);
            if (invitations.isEmpty()) {
                ViewConstants.getOutputStream().println("No invitations found for this group");
                printInConsoleForProcess(0);
            } else {
                invitations.forEach(ViewConstants.getOutputStream()::println);
                printInConsoleForProcess(1);
            }
        } catch (IOException | NetworkResponseFailureException e) {
            ViewConstants.getOutputStream().println("Network error");
            goBack();
        }
    }
}
