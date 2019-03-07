package edu.northeastern.ccs.im.view;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SearchWindow extends AbstractTerminalWindow {

    Map<Integer, String> processMap = new HashMap<>();

    protected SearchWindow(TerminalWindow callerWindow) {
        super(callerWindow,
                Collections.singletonList(new AbstractMap.SimpleEntry<>(1, "Enter search query"))
                        .stream().collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue)));

    }

    @Override
    void inputFetchedFromUser(String inputString) {

    }
}
