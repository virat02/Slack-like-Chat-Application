package edu.northeastern.ccs.im.view;

import edu.northeastern.ccs.im.communication.ClientConnectionFactory;
import edu.northeastern.ccs.im.communication.NetworkResponse;
import edu.northeastern.ccs.im.userGroup.Group;
import edu.northeastern.ccs.im.userGroup.User;

import java.io.IOException;
import java.util.*;

public class SearchWindow extends AbstractTerminalWindow {

    private boolean isSearchingForUser;
    private String searchQuery;

    protected SearchWindow(TerminalWindow callerWindow, ClientConnectionFactory clientConnectionFactory) {
        super(callerWindow, new HashMap<Integer, String>() {{
            put(0, ConstantStrings.SEARCH_MESSAGE);
            put(1, ConstantStrings.SEARCH_QUERY);
            put(2, ConstantStrings.SEARCH_COMPLETE);
        }}, clientConnectionFactory);
    }

    @Override
    void inputFetchedFromUser(String inputString) {
        if (getCurrentProcess() == 0) {
            if (inputString.equals("1")) {
                isSearchingForUser = true;
                printInConsoleForProcess(1);
            } else if (inputString.equals("2")) {
                isSearchingForUser = false;
                printInConsoleForProcess(1);
            }
            else {
                if (inputString.equals("1")) {
                    printInConsoleForProcess(0);
                } else if (inputString.equals("0")) {
                    goBack();
                }
                else {
                    printMessageInConsole(ConstantStrings.INVALID_INPUT_STRING);
                    printInConsoleForProcess(0);
                }
            }
        }
        else if (getCurrentProcess() == 1) {
            searchQuery = inputString;
            if (isSearchingForUser) {
                User searchedUser = searchForUsers(inputString);
                if (searchedUser == null) {
                    printMessageInConsole(ConstantStrings.SEARCH_EMPTY);
                }
                else  {
                    printMessageInConsole("UserName: " + searchedUser.getUsername());
                    printMessageInConsole("Email ID: " + searchedUser.getProfile().getEmail());
                    printMessageInConsole("ImageURL: " + searchedUser.getProfile().getImageUrl());
                }
            }
            else {
                List<Group> searchedGroups = searchForGroup(inputString);
                if (searchedGroups == null || searchedGroups.size() == 0) {
                    printMessageInConsole(ConstantStrings.SEARCH_EMPTY);
                }
                else {
                    printMessageInConsole(searchedGroups.size() + " Groups Found");
//                    for (int index = 0 ; index < searchedGroups.size() ; index++) {
//                        printMessageInConsole("Group" + index + "->");
//                        Group groupObj = searchedGroups.get(index);
//                        printMessageInConsole("Group Name: " + searchedGroups.get(index).getName());
//                        printMessageInConsole("Group ID: " + searchedGroups.get(index).getId());
//                        StringBuilder moderators = new StringBuilder(searchedGroups
//                                .get(index).getModerators().get(0).getUsername());
//                        for (int mod = 1 ; mod < searchedGroups.get(index).getModerators().size() ; mod++) {
//                            moderators.append(searchedGroups
//                                    .get(index).getModerators().get(mod).getUsername());
//                        }
//                        printMessageInConsole("Moderators: " + moderators.toString());
//                    }
                }
            }
            printInConsoleForNextProcess();
        } else if (getCurrentProcess() == 2) {
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
            return null;
        }
    }

    private List<Group> searchForGroup(String inputString) {
        try {
            NetworkResponse networkResponse = sendNetworkConnection(networkRequestFactory
                    .createSearchGroupRequest(inputString));
            return ResponseParser.parseSearchGroupNetworkResponse(networkResponse);
        } catch (NetworkResponseFailureException exception) {
            return null;
        }
    }
}
