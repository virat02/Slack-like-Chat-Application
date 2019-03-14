package edu.northeastern.ccs.im.view;

import edu.northeastern.ccs.im.communication.ClientConnectionFactory;
import edu.northeastern.ccs.im.communication.CommunicationUtils;
import edu.northeastern.ccs.im.communication.NetworkResponse;

import java.io.IOException;
import java.util.*;

public class SearchWindow extends AbstractTerminalWindow {

    Map<Integer, String> processMap = new HashMap<>();
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
                printMessageInConsole(ConstantStrings.INVALID_INPUT_STRING);
                printInConsoleForProcess(0);
            }
        }
        else if (getCurrentProcess() == 1) {
            searchQuery = inputString;
            try {
                NetworkResponse networkResponse = sendNetworkConnection(networkRequestFactory.createSearchUserRequest(""));
                List<String> userResults = parseUserResults(networkResponse);
                userResults.forEach(this::printMessageInConsole);
            } catch (IOException e) {
                printMessageInConsole("Search Request failed");
            }
            //SENDING THE NW REQ
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

    private List<String> parseUserResults(NetworkResponse networkResponse) throws IOException {
        return CommunicationUtils.getObjectMapper().readValue(networkResponse.payload().jsonString(), ArrayList.class);
    }
}
