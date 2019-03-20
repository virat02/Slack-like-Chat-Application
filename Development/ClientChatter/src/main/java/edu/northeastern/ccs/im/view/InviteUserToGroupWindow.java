package edu.northeastern.ccs.im.view;

import edu.northeastern.ccs.im.communication.ClientConnectionFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InviteUserToGroupWindow extends AbstractTerminalWindow {

    private String userName = "";
    public InviteUserToGroupWindow(TerminalWindow terminalWindow, ClientConnectionFactory clientConnectionFactory) {
        super(terminalWindow,
                Stream.of(new AbstractMap.SimpleEntry<>(1, ConstantStrings.USER_NAME_STRING))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                , clientConnectionFactory);
    }

    @Override
    public void inputFetchedFromUser(String inputString) {
        if (getCurrentProcess() == 0) {
            userName = inputString;
            printInConsoleForNextProcess();
        }
    }
}
