package edu.northeastern.ccs.im.view;

import org.omg.CORBA.PRIVATE_MEMBER;

import java.io.IOException;
import java.util.HashMap;

import edu.northeastern.ccs.im.communication.ClientConnectionFactory;
import edu.northeastern.ccs.im.communication.NetworkRequestFactory;
import edu.northeastern.ccs.im.communication.NetworkResponse;
import edu.northeastern.ccs.im.userGroup.Profile;

public class UpdateProfileWindow extends AbstractTerminalWindow {

  public UpdateProfileWindow(TerminalWindow caller, ClientConnectionFactory clientConnectionFactory) {
    super(caller, new HashMap<Integer, String>() {{
      put(0, ConstantStrings.UPDATE_PROFILE);
      put(1, ConstantStrings.UPDATE_PROFILE_EMAIL);
      put(2, ConstantStrings.UPDATE_PROFILE_IMAGEURL);
    }}, clientConnectionFactory);
  }

  @Override
  void inputFetchedFromUser(String inputString) {
    Profile userProfile = UserConstants.getUserObj().getProfile();
    if (getCurrentProcess() == 1) {
      if (updateUserProfile(inputString,
              userProfile == null ? "" : userProfile.getImageUrl())) {
        printMessageInConsole(ConstantStrings.UPDATE_PROFILE_SUCCESS);
      }
      else {
        printMessageInConsole(ConstantStrings.UPDATE_PROFILE_FAILED);
      }
      printInConsoleForProcess(0);
    }
    else if (getCurrentProcess() == 2) {
      if (updateUserProfile(userProfile == null ? "" : userProfile.getEmail(),
              inputString)) {
        printMessageInConsole(ConstantStrings.UPDATE_PROFILE_SUCCESS);
      }
      else {
        printMessageInConsole(ConstantStrings.UPDATE_PROFILE_FAILED);
      }
      printInConsoleForProcess(0);
    }
    else if (getCurrentProcess() == 2) {
      if (updateUserProfile(userProfile == null ? "" : userProfile.getEmail(), inputString)) {
        printMessageInConsole(ConstantStrings.UPDATE_PROFILE_SUCCESS);
      }
      else {
        printMessageInConsole(ConstantStrings.UPDATE_PROFILE_FAILED);
      }
      printInConsoleForProcess(0);
    }
    if (inputString.equals("1")) {
      printInConsoleForProcess(1);
    } else if (inputString.equals("2")) {
      printInConsoleForProcess(2);
    } else if (inputString.equals("3")) {
      if (updateUserViewStatus(!UserConstants.getUserObj().getProfileAccess())) {
        printMessageInConsole(ConstantStrings.UPDATE_PROFILE_SUCCESS);
      }
      else {
        printMessageInConsole(ConstantStrings.UPDATE_PROFILE_FAILED);
      }
      printInConsoleForProcess(0);
    } else if (inputString.equals("4")) {
      if (UserConstants.getUserObj().getProfile() == null) {
        printMessageInConsole("Email Address : (not set)");
        printMessageInConsole("Image URL : (not set)");
        printMessageInConsole("Is profile public : (not set)");
      }
      else {
        printMessageInConsole("Email Address : " + UserConstants.getUserObj().getProfile().getEmail());
        printMessageInConsole("Image URL : " + UserConstants.getUserObj().getProfile().getImageUrl());
        printMessageInConsole("Is profile public : " + (UserConstants.getUserObj().getProfileAccess() ? "Yes" : "No"));
      }
      printInConsoleForProcess(0);
    } else if (inputString.equals("0")) {
      goBack();
    } else if (inputString.equals("*")) {
      exitWindow();
    } else {
      invalidInputPassed();
    }
  }

  private boolean updateUserProfile(String userEmailAddress, String imageUrl) {
    try {
      NetworkResponse networkResponse;
      if (UserConstants.getUserObj().getProfile() == null) {
        networkResponse = sendNetworkConnection(new NetworkRequestFactory()
                .createUserProfile(userEmailAddress, imageUrl));
      }
      else {
        networkResponse = sendNetworkConnection(new NetworkRequestFactory()
                .createUpdateUserProfile(userEmailAddress, imageUrl, UserConstants.getUserObj()));
      }
      if (networkResponse.status() == NetworkResponse.STATUS.SUCCESSFUL) {
        Profile profile = ResponseParser.parseUpdateUserProfile(networkResponse);
        networkResponse = sendNetworkConnection(new NetworkRequestFactory()
                .createUpdateUserProfile(profile, UserConstants.getUserObj()));
        if (networkResponse.status() == NetworkResponse.STATUS.SUCCESSFUL) {
          UserConstants.getUserObj().setProfile(profile);
          return true;
        }
      }
      else {
        printMessageInConsole(ConstantStrings.NETWORK_ERROR);
      }
    } catch (IOException | NetworkResponseFailureException exception) {
      /* TODO Provide some good custom message */
      printMessageInConsole(ConstantStrings.NETWORK_ERROR);
    }
    return false;
  }

  private boolean updateUserViewStatus(boolean profileViewStatus) {
    try {
      NetworkResponse networkResponse = sendNetworkConnection(new NetworkRequestFactory()
              .createUpdateProfileStatus(profileViewStatus, UserConstants.getUserObj()));
      return ResponseParser.parseUpdateUserObj(networkResponse);
    } catch (IOException exception) {
      /* TODO Provide some good custom message */
      printMessageInConsole(ConstantStrings.NETWORK_ERROR);
    }
    return false;
  }
}
