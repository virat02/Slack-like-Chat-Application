package edu.northeastern.ccs.im.view;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import edu.northeastern.ccs.im.communication.CommunicationUtils;
import edu.northeastern.ccs.im.communication.NetworkResponse;
import edu.northeastern.ccs.im.userGroup.Group;
import edu.northeastern.ccs.im.userGroup.Profile;
import edu.northeastern.ccs.im.userGroup.User;

public class ResponseParser {

  private static void throwErrorIfResponseFailed(NetworkResponse networkResponse) throws IOException
          , NetworkResponseFailureException {
    if (networkResponse.status().equals(NetworkResponse.STATUS.FAILED)) {
      JsonNode jsonNode = CommunicationUtils
              .getObjectMapper().readTree(networkResponse.payload().jsonString());
      throw new NetworkResponseFailureException("Error");
    }
  }

  static User parseLoginNetworkResponse(NetworkResponse networkResponse) throws IOException,
          NetworkResponseFailureException {
    throwErrorIfResponseFailed(networkResponse);
    User parsedUserObj = CommunicationUtils
            .getObjectMapper().readValue(networkResponse.payload().jsonString(), User.class);
    UserConstants.setUserObj(parsedUserObj);
    return parsedUserObj;
  }

  static boolean parseForgotPasswordResponse(NetworkResponse networkResponse) {
    return networkResponse.status().equals(NetworkResponse.STATUS.SUCCESSFUL);
  }

  static User parseSearchUserNetworkResponse(NetworkResponse networkResponse) throws IOException,
          NetworkResponseFailureException {
    throwErrorIfResponseFailed(networkResponse);
    User parsedUserObj = CommunicationUtils
            .getObjectMapper().readValue(networkResponse.payload().jsonString(), User.class);
    return parsedUserObj;
  }

  static List<Group> parseSearchGroupNetworkResponse(NetworkResponse networkResponse) throws
          IOException, NetworkResponseFailureException {
    throwErrorIfResponseFailed(networkResponse);
    List<Group> parsedObjects = CommunicationUtils
            .getObjectMapper().readValue(networkResponse.payload().jsonString(), List.class);
//    List<Group> groupList = new ArrayList<>();
//    for (String obj : parsedObjects) {
//      groupList.add(CommunicationUtils
//              .getObjectMapper().readValue(obj, Group.class));
//    }
    return parsedObjects;
  }

  static boolean parseAddGroupResponse(NetworkResponse networkResponse) {
    return networkResponse.status().equals(NetworkResponse.STATUS.SUCCESSFUL);
  }

  static boolean parseDeleteGroupResponse(NetworkResponse networkResponse) {
    return networkResponse.status().equals(NetworkResponse.STATUS.SUCCESSFUL);
  }

  static Profile parseUpdateUserProfile(NetworkResponse networkResponse) throws IOException,
          NetworkResponseFailureException {
    throwErrorIfResponseFailed(networkResponse);
    Profile parsedProfileObj = CommunicationUtils
            .getObjectMapper().readValue(networkResponse.payload().jsonString(), Profile.class);
    return parsedProfileObj;
  }

  static List<User> parseFollowersList(NetworkResponse networkResponse) throws IOException,
          NetworkResponseFailureException {
    throwErrorIfResponseFailed(networkResponse);
    Profile parsedProfileObj = CommunicationUtils
            .getObjectMapper().readValue(networkResponse.payload().jsonString(), Profile.class);
    return null;
  }

  static void parseSetFollowersList(NetworkResponse networkResponse) throws IOException,
          NetworkResponseFailureException {
    throwErrorIfResponseFailed(networkResponse);
    User parsedUserObj = CommunicationUtils
            .getObjectMapper().readValue(networkResponse.payload().jsonString(), User.class);
    UserConstants.setUserObj(parsedUserObj);
  }
}
