package edu.northeastern.ccs.im.view;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.northeastern.ccs.im.Message;
import edu.northeastern.ccs.im.communication.CommunicationUtils;
import edu.northeastern.ccs.im.communication.NetworkResponse;
import edu.northeastern.ccs.im.userGroup.Group;
import edu.northeastern.ccs.im.userGroup.Invite;
import edu.northeastern.ccs.im.userGroup.Profile;
import edu.northeastern.ccs.im.userGroup.User;

public class ResponseParser {

    private static void throwErrorIfResponseFailed(NetworkResponse networkResponse) throws IOException
            , NetworkResponseFailureException {
        if (networkResponse.status().equals(NetworkResponse.STATUS.FAILED)) {
            JsonNode jsonNode = CommunicationUtils
                    .getObjectMapper().readTree(networkResponse.payload().jsonString());
            if (jsonNode.has("exceptionMessage")) {
                ViewConstants.getOutputStream().println(jsonNode.get("exceptionMessage").asText());
            }

            throw new NetworkResponseFailureException("Error");
        }
    }

    static User parseLoginNetworkResponse(NetworkResponse networkResponse) throws IOException,
            NetworkResponseFailureException {
        throwErrorIfResponseFailed(networkResponse);
        if (networkResponse.payload() == null || networkResponse.payload().jsonString().equals("")) {
            throw new NetworkResponseFailureException("");
        }
        User parsedUserObj = CommunicationUtils
                .getObjectMapper().readValue(networkResponse.payload().jsonString(), User.class);
        if (parsedUserObj == null) {
            throw new NetworkResponseFailureException("");
        }
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

    static Group parseGroupNetworkResponse(NetworkResponse networkResponse) throws
            IOException, NetworkResponseFailureException {
        throwErrorIfResponseFailed(networkResponse);
        Group parsedGroupObj = CommunicationUtils
                .getObjectMapper().readValue(networkResponse.payload().jsonString(), Group.class);
        return parsedGroupObj;
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

    static Group parseAddGroupResponse(NetworkResponse networkResponse) throws IOException,
            NetworkResponseFailureException {
        throwErrorIfResponseFailed(networkResponse);
        Group parsedUserObj = CommunicationUtils
                .getObjectMapper().readValue(networkResponse.payload().jsonString(), Group.class);
        return parsedUserObj;
    }

    static boolean parseDeleteGroupResponse(NetworkResponse networkResponse) {
        return networkResponse.status().equals(NetworkResponse.STATUS.SUCCESSFUL);
    }

    static boolean parseUpdateUserObj(NetworkResponse networkResponse) throws IOException {
        if (networkResponse.status().equals(NetworkResponse.STATUS.SUCCESSFUL)) {
            User parsedUserObj = CommunicationUtils
                    .getObjectMapper().readValue(networkResponse.payload().jsonString(), User.class);
            UserConstants.setUserObj(parsedUserObj);
            return true;
        }
        return false;
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

        List<User> parsedUsers =
                CommunicationUtils.getObjectMapper().readValue(networkResponse.payload().jsonString()
                        , ArrayList.class);

        return parsedUsers;
    }

    static boolean parseSetFollowersList(NetworkResponse networkResponse) throws IOException {
        if (networkResponse.status().equals(NetworkResponse.STATUS.SUCCESSFUL)) {
            User parsedUserObj = CommunicationUtils
                    .getObjectMapper().readValue(networkResponse.payload().jsonString(), User.class);
            UserConstants.setUserObj(parsedUserObj);
            return true;
        }
        return false;
    }

    public static List<Message> readRecentMessagesAndPrintInScreen(NetworkResponse networkResponse) throws IOException, NetworkResponseFailureException {
        throwErrorIfResponseFailed(networkResponse);
        List<Message> messages = CommunicationUtils.getObjectMapper().readValue(networkResponse.payload().jsonString(), new TypeReference<ArrayList<Message>>() {
        });
        messages.stream().map(m -> MessageSocketListener.messageFormatter().formatMessage(m)).filter(m -> !m.equals(""))
                .forEach(ViewConstants.getOutputStream()::println);

        return messages;

    }

    public static void parseNetworkResponse(NetworkResponse networkResponse) throws IOException, NetworkResponseFailureException {
        if (networkResponse.status() == NetworkResponse.STATUS.FAILED)
            throwErrorIfResponseFailed(networkResponse);

        JsonNode jsonNode = CommunicationUtils.getObjectMapper().readTree(networkResponse.payload().jsonString());
        if (jsonNode.has("message")) {
            ViewConstants.getOutputStream().println(jsonNode.get("message").asText());
        }
    }

    public static List<Invite> parseInvitationsList(NetworkResponse networkResponse) throws IOException, NetworkResponseFailureException {
        if (networkResponse.status() == NetworkResponse.STATUS.FAILED)
            throwErrorIfResponseFailed(networkResponse);

        return CommunicationUtils.getObjectMapper().readValue(networkResponse.payload().jsonString(), new TypeReference<ArrayList<Invite>>() {
        });
    }
}
