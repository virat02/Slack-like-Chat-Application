package edu.northeastern.ccs.im.view;

import edu.northeastern.ccs.im.communication.ClientConnectionFactory;
import edu.northeastern.ccs.im.communication.NetworkResponse;
import edu.northeastern.ccs.im.userGroup.Group;
import edu.northeastern.ccs.im.userGroup.User;

import java.util.*;

public class SearchWindow extends AbstractTerminalWindow {

    private boolean isSearchingForUser;
    private String searchQuery;

    protected SearchWindow(TerminalWindow callerWindow,
                           ClientConnectionFactory clientConnectionFactory, boolean isSearchingForUser) {
        super(callerWindow, new HashMap<Integer, String>() {{
            put(0, ConstantStrings.SEARCH_QUERY);
            put(1, ConstantStrings.SEARCH_COMPLETE);
        }}, clientConnectionFactory);
        this.isSearchingForUser = isSearchingForUser;
    }

    @Override
    void inputFetchedFromUser(String inputString) {
        if (getCurrentProcess() == 0) {
            searchQuery = inputString;
            if (isSearchingForUser) {
                User searchedUser = searchForUsers(inputString);
                if (searchedUser != null) {
                    printMessageInConsole("UserName: " + searchedUser.getUsername());
                    printMessageInConsole("Email ID: " + searchedUser.getProfile().getEmail());
                    printMessageInConsole("ImageURL: " + searchedUser.getProfile().getImageUrl());
                }
            }
            else {
                Group searchedGroup = searchForGroup(inputString);
                if (searchedGroup != null) {
                    printMessageInConsole("Group Name: " + searchedGroup.getName());
                    printMessageInConsole("Group ID: " + searchedGroup.getId());
                    StringBuilder moderators = new StringBuilder(searchedGroup.getModerators()
                            .get(0).getUsername());
                    for (int mod = 1 ; mod < searchedGroup.getModerators().size() ; mod++) {
                        moderators.append(searchedGroup.getModerators().get(mod).getUsername());
                    }
                    printMessageInConsole("Moderators: " + moderators.toString());
                }
            }
            printInConsoleForNextProcess();
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
        } else {
            invalidInputPassed();
        }
    }

    private User searchForUsers(String inputString) {
        try {
            NetworkResponse networkResponse = sendNetworkConnection(networkRequestFactory
                    .createSearchUserRequest(inputString));
            return ResponseParser.parseSearchUserNetworkResponse(networkResponse);
        } catch (NetworkResponseFailureException exception) {
            printMessageInConsole(exception.getMessage());
            return null;
        }
    }

    private Group searchForGroup(String inputString) {
        try {
            NetworkResponse networkResponse = sendNetworkConnection(networkRequestFactory
                    .createGetGroupRequest(inputString));
            return ResponseParser.parseGroupNetworkResponse(networkResponse);
        } catch (NetworkResponseFailureException exception) {
            printMessageInConsole(exception.getMessage());
            return null;
        }
    }
}
