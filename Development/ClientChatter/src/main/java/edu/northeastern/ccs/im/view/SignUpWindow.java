package edu.northeastern.ccs.im.view;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;

import edu.northeastern.ccs.im.communication.*;
import edu.northeastern.ccs.im.userGroup.Profile;

public class SignUpWindow extends AbstractTerminalWindow {

    private String userName;
    private String passwordString;
    private String emailAddress;
    private String imageURL;
    private TerminalWindow chatTerminalWindow;

    SignUpWindow(TerminalWindow caller, ClientConnectionFactory clientConnectionFactory) {
        super(caller, new HashMap<Integer, String>() {{
            put(0, ConstantStrings.EMAIL_ADDRESS_STRING);
            put(1, ConstantStrings.USER_NAME_STRING);
            put(2, ConstantStrings.PASSWORD_STRING);
            put(3, ConstantStrings.RE_ENTER_PASSWORD_STRING);
            put(4, ConstantStrings.UPDATE_PROFILE_IMAGEURL);
            put(5, ConstantStrings.SIGN_UP_FAILED);
        }}, clientConnectionFactory);
    }

    public TerminalWindow getChatTerminalWindow() {
        if (chatTerminalWindow == null) {
            chatTerminalWindow = new ChatTerminalWindow(this, clientConnectionFactory);
        }
        return chatTerminalWindow;
    }

    @Override
    void inputFetchedFromUser(String inputString) {
        if (getCurrentProcess() == 0) {
            emailAddress = inputString;
            printInConsoleForNextProcess();
        } else if (getCurrentProcess() == 1) {
            userName = inputString;
            printInConsoleForNextProcess();
        } else if (getCurrentProcess() == 2) {
            passwordString = inputString;
            printInConsoleForNextProcess();
        } else if (getCurrentProcess() == 3) {
            if (!inputString.equals(passwordString)) {
                printMessageInConsole(ConstantStrings.PASSWORDS_DO_NOT_MATCH);
                printInConsoleForProcess(2);
            } else {
                passwordString = inputString;
                printInConsoleForNextProcess();
            }
        } else if (getCurrentProcess() == 4) {
            imageURL = inputString;
            int id = createUserAndFetchId();
            if (id == -1) {
                printInConsoleForProcess(5);
            } else {
                printMessageInConsole(ConstantStrings.SIGN_UP_SUCCESSFUL);
                getChatTerminalWindow().runWindow();
            }
        } else {
            if (inputString.equals("1")) {
                printInConsoleForProcess(0);
            } else if (inputString.equals("0")) {
                goBack();
            } else if (inputString.equals("*")) {
                exitWindow();
            } else {
                invalidInputPassed();
            }
        }
    }

    private int createUserAndFetchId() {
        try {
            NetworkResponse networkResponse = sendNetworkConnection(new NetworkRequestFactory()
                    .createUserProfile(emailAddress, imageURL));
            Profile profile = ResponseParser.parseUpdateUserProfile(networkResponse);
            networkResponse = sendNetworkConnection(new NetworkRequestFactory()
                    .createUserRequest(userName, passwordString));
            int userId = ResponseParser.parseLoginNetworkResponse(networkResponse).getId();
            if (networkResponse.status().equals(NetworkResponse.STATUS.SUCCESSFUL) && userId != -1) {
                networkResponse = sendNetworkConnection(new NetworkRequestFactory()
                        .createUpdateUserProfile(profile, UserConstants.getUserObj()));
                if (networkResponse.status() == NetworkResponse.STATUS.SUCCESSFUL) {
                    UserConstants.getUserObj().setProfile(profile);
                    return userId;
                }
            }
        } catch (NetworkResponseFailureException exception) {
            printMessageInConsole(exception.getMessage());
        }
        return -1;
    }
}
