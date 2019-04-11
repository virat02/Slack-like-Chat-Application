package edu.northeastern.ccs.im.view;

import java.util.HashMap;
import java.util.Map;

import edu.northeastern.ccs.im.communication.ClientConnectionFactory;

public class ChatTerminalWindow extends AbstractTerminalWindow {

    Map<Integer, AbstractTerminalWindow> mapper = new HashMap<>();

    public ChatTerminalWindow(TerminalWindow caller, ClientConnectionFactory clientConnectionFactory) {
        super(caller, new HashMap<Integer, String>() {{
            put(0, ConstantStrings.CHAT_MAIN_COMMAND);
        }}, clientConnectionFactory);
    }

    @Override
    void inputFetchedFromUser(String inputString) {
        if (inputString.length() <= 2) {
            if (inputString.equals("1")) {
                MessageGroupSelectorWindow selectorWindow = (MessageGroupSelectorWindow) mapper.computeIfAbsent(1,
                        e -> new MessageGroupSelectorWindow(this, clientConnectionFactory));
                selectorWindow.runWindow();
            } else if (inputString.equals("2")) {
                GroupWindow groupWindow = (GroupWindow) mapper.computeIfAbsent(2,
                        e -> new GroupWindow(this, clientConnectionFactory));
                groupWindow.runWindow();
            } else if (inputString.equals("3")) {
                CirclesWindow circlesWindow = (CirclesWindow) mapper
                        .computeIfAbsent(3, e -> new CirclesWindow(this,
                                clientConnectionFactory));
                circlesWindow.runWindow();
            } else if (inputString.equals("4")) {
                UpdateProfileWindow updateProfileWindow = (UpdateProfileWindow) mapper
                        .computeIfAbsent(4, e -> new UpdateProfileWindow(this,
                                clientConnectionFactory));
                updateProfileWindow.runWindow();
            } else if (inputString.equals("0")) {
                signOutApp();
            } else if (inputString.equals("*")) {
                exitWindow();
            } else {
                invalidInputPassed();
            }
        } else {
            invalidInputPassed();
        }
    }

    @Override
    protected String helpCommand() {
        return "Access the different features that you can enjoy";
    }
}
