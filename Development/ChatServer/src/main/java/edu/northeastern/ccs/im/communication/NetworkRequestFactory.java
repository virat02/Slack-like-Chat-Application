package edu.northeastern.ccs.im.communication;

import edu.northeastern.ccs.jpa.Profile;
import edu.northeastern.ccs.jpa.User;

public class NetworkRequestFactory {
    public NetworkRequest createUserRequest(String name, String password) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.CREATE_USER,
                () -> {
                    User user = new User();
                    user.setId(0);

                    Profile profile = new Profile();
                    profile.setId(0);
                    profile.setName(name);
                    profile.setImageUrl("");
//                    profile.setUser(user);
                    profile.setPassword(password);

                    user.setProfile(profile);
                    return CommunicationUtils.getObjectMapper().writeValueAsString(user);
                });
    }
}
