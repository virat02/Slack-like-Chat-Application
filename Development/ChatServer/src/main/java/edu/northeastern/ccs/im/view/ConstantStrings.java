package edu.northeastern.ccs.im.view;

public class ConstantStrings {

  private ConstantStrings() {}

  public static final String kInvalidInputString = "Invalid Input";
  public static final String kConfirmExitMessage = "Confirm(Y/N)";
  public static final String kFailureMenu = "1 - Retry\n0 - Go Back\n* - Exit";

  //Network Related
  public static final String kNetworkError = "Network Error";

  //App Launch Strings
  public static final String kInitialLaunch = "1 - Login\n2 - Sign Up\n* - QUIT";

  //Login View Strings
  public static final String kEmailAddressString = "Enter Email Address: ";
  public static final String kPasswordString = "Enter Password: ";
  public static final String kLoginSuccessful = "Login Successful";
  public static final String kLoginFailed = "Invalid Username/password\n"
          + "1 - Retry\n"
          + "2 - Forgot Password\n"
          + "0 - Go Back\n* - Exit";

  //Sign Up Strings
  public static final String kUserNameString = "Enter Username: ";
  public static final String kReEnterPasswordString = "Confirm Password: ";
  public static final String kPasswordsDoNotMatch = "Passwords do not match. Renter password";
  public static final String kSignUpSuccessful = "Sign Up Successful";
  public static final String kSignUpFailed = "Sign Up Failed\n" + kFailureMenu;


  //Forgot Password
  public static final String kRecoveryEmail = "Enter Recovery Email Address: ";
  public static final String kRecoveryInitiated = "Recovery initiated. "
          + "Check your email address for more details.\n" + kFailureMenu;
  public static final String kRecoveryFailed = "Recovery failed. " + kFailureMenu;

  public static final String chatMainCommand = "1 - Search user\n2 - Create Group \n 3 - Sign out\n";
  public static final String searchUser = "Search user";
  public static final String createGroup = "Create group";
  public static final String signOut = "Sign out";
}
