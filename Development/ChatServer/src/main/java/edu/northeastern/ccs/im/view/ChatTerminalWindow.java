package edu.northeastern.ccs.im.view;

import java.util.HashMap;
import java.util.Map;

public class ChatTerminalWindow extends AbstractTerminalWindow {

    private final int userId;

    Map<Integer, AbstractTerminalWindow> mapper = new HashMap<>();

    public ChatTerminalWindow(TerminalWindow caller, int userId) {
        super(caller, new HashMap<Integer, String>() {{
            put(0, ConstantStrings.chatMainCommand);
        }});
        this.userId = userId;
    }

    @Override
    void inputFetchedFromUser(String inputString) {
        if (inputString.length() == 1) {
            if (inputString.equals("1")) {
                SearchWindow searchWindow = (SearchWindow) mapper.computeIfAbsent(1, e -> new SearchWindow(this));
                searchWindow.runWindow();
            } else if (inputString.equals("2")) {

            } else if (inputString.equals("3")) {

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
}
