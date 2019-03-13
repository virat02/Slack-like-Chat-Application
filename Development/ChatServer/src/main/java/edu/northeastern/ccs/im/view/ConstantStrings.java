package edu.northeastern.ccs.im.view;

public class ConstantStrings {

    public static final String THANK_YOU = "Thank you";
    public static final String EXIT = "exit";

    private ConstantStrings() {
    }

    public static final String INVALID_INPUT_STRING = "Invalid Input";
    public static final String CONFIRM_EXIT_MESSAGE = "Confirm(Y/N)";
    public static final String DEFAULT_MENU = "0 - Go Back\n* - Exit";
    public static final String FAILURE_MENU = "1 - Retry\n" + DEFAULT_MENU;

    //Network Related
    public static final String NETWORK_ERROR = "Network Error";

    //App Launch Strings
    public static final String INITIAL_LAUNCH = "1 - Login\n2 - Sign Up\n* - QUIT";

    //Login View Strings
    public static final String EMAIL_ADDRESS_STRING = "Enter Email Address: ";
    public static final String PASSWORD_STRING = "Enter Password: ";
    public static final String LOGIN_SUCCESSFUL = "Login Successful";
    public static final String LOGIN_FAILED = "Invalid Username/password\n"
            + "1 - Retry\n" + "2 - Forgot Password\n" + DEFAULT_MENU;

    //Sign Up Strings
    public static final String USER_NAME_STRING = "Enter Username: ";
    public static final String RE_ENTER_PASSWORD_STRING = "Confirm Password: ";
    public static final String PASSWORDS_DO_NOT_MATCH = "Passwords do not match. Renter password";
    public static final String SIGN_UP_SUCCESSFUL = "Sign Up Successful";
    public static final String SIGN_UP_FAILED = "Sign Up Failed\n" + FAILURE_MENU;


    //Forgot Password
    public static final String RECOVERY_EMAIL = "Enter Recovery Email Address: ";
    public static final String RECOVERY_INITIATED = "Recovery initiated. "
            + "Check your email address for more details.\n" + FAILURE_MENU;
    public static final String RECOVERY_FAILED = "Recovery failed.\n" + FAILURE_MENU;

    public static final String CHAT_MAIN_COMMAND = "1 - Search User/Group\n"
            + "2 - Create Group\n"
            + "3 - Delete Group\n"
            + "4 - Update Profile\n"
            + "5 - Sign out\n"
            + "* - Exit";
    public static final String SEARCH_USER = "Search user";
    public static final String SIGN_OUT = "Sign out";

    //Update Profile
    public static final String UPDATE_PROFILE = "1 - Update User Name\n2 - Update Status\n" + DEFAULT_MENU;
    public static final String UPDATE_PROFILE_USERNAME = "Enter new user name: ";
    public static final String UPDATE_PROFILE_STATUS = "Enter new status: ";
    public static final String UPDATE_PROFILE_SUCCESS = "Profile updated successfully";
    public static final String UPDATE_PROFILE_FAILED = "Profile update failed";

    //Create Group
    public static final String CREATE_GROUP = "Enter Group Name: ";
    public static final String CREATE_GROUP_SUCCESS = "Group created successfully\n" + DEFAULT_MENU;
    public static final String CREATE_GROUP_FAILED = "Group creation failed\n" + FAILURE_MENU;

    //Delete Group
    public static final String DELETE_GROUP = "Enter Group Name To Delete: ";
    public static final String DELETE_GROUP_SUCCESS = "Group deleted successfully\n" + DEFAULT_MENU;
    public static final String DELETE_GROUP_FAILED = "Group deletion failed\n" + FAILURE_MENU;
}
