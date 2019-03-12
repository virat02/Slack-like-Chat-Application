package edu.northeastern.ccs.im.view;

import java.util.HashMap;
import java.util.Map;

import edu.northeastern.ccs.im.communication.ClientConnectionFactory;

public class ChatTerminalWindow extends AbstractTerminalWindow {

    private final int userId;

    Map<Integer, AbstractTerminalWindow> mapper = new HashMap<>();

    public ChatTerminalWindow(TerminalWindow caller, int userId, ClientConnectionFactory clientConnectionFactory) {
        super(caller, new HashMap<Integer, String>() {{
            put(0, ConstantStrings.CHAT_MAIN_COMMAND);
        }}, clientConnectionFactory);
        this.userId = userId;
    }

    @Override
    void inputFetchedFromUser(String inputString) {
        if (inputString.length() == 1) {
            if (inputString.equals("1")) {
                SearchWindow searchWindow = (SearchWindow) mapper.computeIfAbsent(1,
                        e -> new SearchWindow(this, clientConnectionFactory));
                searchWindow.runWindow();
            } else if (inputString.equals("2")) {
                CreateGroupWindow createGroupWindowWindow = (CreateGroupWindow) mapper
                        .computeIfAbsent(2, e -> new CreateGroupWindow(this,
                                clientConnectionFactory));
                createGroupWindowWindow.runWindow();
            } else if (inputString.equals("3")) {
                DeleteGroupWindow deleteGroupWindowWindow = (DeleteGroupWindow) mapper
                        .computeIfAbsent(3, e -> new DeleteGroupWindow(this,
                                clientConnectionFactory));
                deleteGroupWindowWindow.runWindow();
            } else if (inputString.equals("4")) {
                UpdateProfileWindow updateProfileWindow = (UpdateProfileWindow) mapper
                        .computeIfAbsent(4, e -> new UpdateProfileWindow(this,
                                clientConnectionFactory));
                updateProfileWindow.runWindow();
            } else if (inputString.equals("5")) {
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
