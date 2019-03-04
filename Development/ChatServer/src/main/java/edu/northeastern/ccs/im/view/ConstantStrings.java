package edu.northeastern.ccs.im.view;

public class ConstantStrings {

  public static final String kInvalidInputString = "Invalid Input";
  public static final String kConfirmExitMessage = "Confirm(Y/N)";

  //App Launch Strings
  public static final String kInitialLaunch = "1 - Login\n2 - Sign Up\n* - QUIT";

  //Login View Strings
  public static final String kUserNameString = "Enter Username: ";
  public static final String kPasswordString = "Enter Password: ";
  public static final String kLoginSuccessful = "Login Successful";
  public static final String kLoginFailed = "Invalid Username/password\n"
          + "1 - Retry\n"
          + "2 - Forgot Password\n"
          + "0 - Go Back\n* - Exit";

  //Forgot Password
  public static final String kRecoveryEmail = "Enter Recovery Email Address: ";
  public static final String kRecoveryInitiated = "Recovery initiated. "
          + "Check your email address for more details.\n"
          + "0 - Back\n* - Exit";

  private ConstantStrings() {}
}
