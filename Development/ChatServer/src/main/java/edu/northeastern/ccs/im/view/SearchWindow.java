package edu.northeastern.ccs.im.view;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.northeastern.ccs.im.communication.ClientConnectionFactory;
import edu.northeastern.ccs.im.communication.CommunicationUtils;
import edu.northeastern.ccs.im.communication.NetworkResponse;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class SearchWindow extends AbstractTerminalWindow {

    private TerminalWindow messageWindow;
    Map<Integer, String> processMap = new HashMap<>();
    private String searchQuery;

    protected SearchWindow(TerminalWindow callerWindow, ClientConnectionFactory clientConnectionFactory) {
        super(callerWindow,
                Arrays.asList(new AbstractMap.SimpleEntry<>(0, "Enter search query"),
                        new AbstractMap.SimpleEntry<>(1, "1 - Search Again\n2 - Select Chat\n0 - " +
                                "Go Back\n* - Exit"),
                        new AbstractMap.SimpleEntry<>(2, "Enter User Name"))
                        .stream().collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey,
                        AbstractMap.SimpleEntry::getValue)), clientConnectionFactory);
    }

  public TerminalWindow getMessageWindow(String chatId) {
    if (messageWindow == null) {
      messageWindow = new MessageWindow(this, clientConnectionFactory, chatId);
    }
    return messageWindow;
  }

    @Override
    void inputFetchedFromUser(String inputString) {
        if (getCurrentProcess() == 0) {
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
        } else if (getCurrentProcess() == 1) {
            if (inputString.length() == 1) {
                if (inputString.equals("1")) {
                    printInConsoleForProcess(0);
                } else if (inputString.equals("2")) {
                    printInConsoleForProcess(2);
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
        else if (getCurrentProcess() == 2) {
          if (UserConstants.getUserName().compareTo(inputString) > 0) {
            getMessageWindow(inputString + "_" + UserConstants.getUserName()).runWindow();
          }
          else {
            getMessageWindow(UserConstants.getUserName() + "_" + inputString).runWindow();
          }
        } else {
            invalidInputPassed();
        }
    }

    private List<String> parseUserResults(NetworkResponse networkResponse) throws IOException {
        return CommunicationUtils.getObjectMapper().readValue(networkResponse.payload().jsonString(), ArrayList.class);
    }
}
