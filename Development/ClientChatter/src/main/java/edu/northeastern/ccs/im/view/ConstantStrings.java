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
    public static final String FETCH_DATA_FAILED = "Unable to fetch data";

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
    public static final String SIGN_OUT = "Sign out";


    // Invite user to group
    public static final String INVITE_GROUP_FAILED = "Request failed. Invitation was not successful.\n"
            + FAILURE_MENU;

    // Invitation Accept or Reject
    public static final String INVITE_ACCEPT_OR_REJECT = "Enter invitation ID followed by a space with an accept(A) or reject(R) letter\n" +
            DEFAULT_MENU;

    public static final String INVITE_ACCEPT_OR_REJECT_FAILED = "Request failed. \n"
            + FAILURE_MENU;

    //Forgot Password
    public static final String RECOVERY_EMAIL = "Enter Recovery Email Address: ";
    public static final String RECOVERY_INITIATED = "Recovery initiated. "
            + "Check your email address for more details.\n" + FAILURE_MENU;
    public static final String RECOVERY_FAILED = "Recovery failed.\n" + FAILURE_MENU;

    //Main Chat
    public static final String CHAT_MAIN_COMMAND = "1 - Chat\n"
            + "2 - Search\n"
            + "3 - Create Group\n"
            + "4 - Invite User to your group\n"
            + "5 - Delete Group\n"
            + "6 - Group Details\n"
            + "7 - Profile\n"
            + "8 - Circles\n"
            + "9 - Sign out\n"
            + "10 - View list of invitations\n"
            + "* - Exit";

    //Message Group Selector
    public static final String CHAT_GROUP_SELECTOR = "1 - Chat with user\n"
            + "2 - Chat in group\n" + DEFAULT_MENU;
    public static final String CHAT_USER_SELECTED = "Enter user name: ";
    public static final String CHAT_GROUP_SELECTED = "Enter group name: ";
    public static final String INVALID_CHAT = "Invalid Chat Name\n" + FAILURE_MENU;

    //Search
    public static final String SEARCH_MESSAGE = "1 - Search Users\n"
            + "2 - Search Groups\n" + DEFAULT_MENU;
    public static final String SEARCH_QUERY = "Enter Search String: ";
    public static final String SEARCH_COMPLETE = "1 - Search Again\n" + DEFAULT_MENU;
    public static final String SEARCH_EMPTY = "No results found";

    //Update Profile
    public static final String UPDATE_PROFILE =
            "1 - Update Email\n2 - Update Image Url\n3 - Invert Profile View Status\n4 - View " +
                    "Profile\n5 - Update Password\n" + DEFAULT_MENU;
    public static final String UPDATE_PROFILE_EMAIL = "Enter new email id: ";
    public static final String UPDATE_PROFILE_IMAGEURL = "Enter new image URL: ";
    public static final String UPDATE_PROFILE_SUCCESS = "Profile updated successfully";
    public static final String UPDATE_PROFILE_FAILED = "Profile update failed";
    public static final String UPDATE_ENTER_OLD_PASSWORD = "Enter old password: ";
    public static final String UPDATE_OLD_PASSWORD_WRONG = "Password is wrong";
    public static final String UPDATE_ENTER_NEW_PASSWORD = "Enter new password: ";

    //Create Group
    public static final String CREATE_GROUP = "Enter Group Name: ";
    public static final String CREATE_GROUP_CODE = "Enter a unique group code:";
    public static final String CREATE_GROUP_SUCCESS = "Group created successfully\n";
    public static final String CREATE_GROUP_FAILED = "Group creation failed\n";

    //Delete Group
    public static final String DELETE_GROUP = "Enter Group Name To Delete: ";
    public static final String DELETE_GROUP_SUCCESS = "Group deleted successfully\n" + DEFAULT_MENU;
    public static final String DELETE_GROUP_FAILED = "Group deletion failed\n" + FAILURE_MENU;

    //Update Group
    public static final String GROUP_PROPERTIES = "1 - Add User To Group\n2 - Delete User To " +
            "Group\n3 - Set Password\n4 - View Group Details\n5 - Create sub group\n6 - View " +
            "Sub group Details\n" + DEFAULT_MENU;
    public static final String GROUP_UPDATE = "Enter Group Code To Update: ";
    public static final String GROUP_NOT_FOUND = "Group not found\n" + FAILURE_MENU;
    public static final String GROUP_UPDATE_USER = "Enter User Name To Update: ";
    public static final String GROUP_UPDATE_PASSWORD = "Enter Group Password: ";
    public static final String GROUP_UPDATE_SUCCESSFUL = "Group Updated Successful";
    public static final String GROUP_UPDATE_FAILED = "Group Update Failed";
    public static final String GROUP_USER_PRESENT = "User already in group";
    public static final String GROUP_USER_ABSENT = "User not in group";
    public static final String GROUP_USER_NO_PERMISSION = "You do not have permission to update " +
            "the " +
            "group";

    //Circles
    public static final String CIRCLE_MENU = "1 - Users following\n"
            + "2 - Follow user\n3 - Unfollow User\n" + DEFAULT_MENU;
//    public static final String CIRCLE_MENU = "1 - Users following\n"
//            + "2 - Users following you\n"
//            + "3 - Users followed by another user\n"
//            + "4 - Follow user\n"
//            + "5 - Un-Follow user\n" + DEFAULT_MENU;
    public static final String USERS_FOLLOWED_USER = "Enter user name: ";
    public static final String FOLLOW_SUCCESSFUL = "Success";
    public static final String FOLLOW_FAILED = "Failed";
    public static final String USER_INVALID = "Invalid User";
}
