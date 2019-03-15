package edu.northeastern.ccs.im.communications;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.northeastern.ccs.im.userGroup.Message;
import edu.northeastern.ccs.im.communication.CommunicationUtils;
import edu.northeastern.ccs.im.communication.NetworkRequest;
import edu.northeastern.ccs.im.communication.NetworkRequestFactory;
import edu.northeastern.ccs.im.userGroup.Group;
import edu.northeastern.ccs.im.userGroup.Profile;
import edu.northeastern.ccs.im.userGroup.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Network Request Factory Test Suite.
 */
public class NetworkRequestFactoryTests {
    private NetworkRequestFactory networkRequestFactory;

    /**
     * A Method to setup the tests.
     */
    @Before
    public void setUp() {
        networkRequestFactory = new NetworkRequestFactory();
    }

    /**
     * Test to ensure the Payload is being loaded properly.
     * @throws JsonProcessingException if the Json isn't processed properly.
     */
    @Test
    public void whenCreateUserRequestIsCalledJsonPayloadShouldBeSame() throws JsonProcessingException {
        String name = "Sibendu";
        String password = "password";
        NetworkRequest networkRequest = networkRequestFactory.createUserRequest(name, password);
        User user = new User();
        user.setUsername("Sibendu");
        user.setPassword("password");
        String expectedJSON = CommunicationUtils.getObjectMapper().writeValueAsString(user);
        Assert.assertEquals(expectedJSON, networkRequest.payload().jsonString());
    }

    /**
     * Test to make a Login Request.
     * @throws JsonProcessingException if the Json isn't processed properly.
     */
    @Test
    public void testMakeLoginRequest() throws JsonProcessingException{
        String name = "Sibendu";
        String email = "sibendu.dey@gmail.com";
        NetworkRequest networkRequest = networkRequestFactory.createLoginRequest(name, email);
        User user = new User();
        user.setUsername("Sibendu");
        user.setPassword("sibendu.dey@gmail.com");
        String expectedJSON = CommunicationUtils.getObjectMapper().writeValueAsString(user);
        Assert.assertEquals(expectedJSON, networkRequest.payload().jsonString());
    }

    /**
     * Test to make a Update Profile Request.
     * @throws JsonProcessingException if the Json isn't processed properly.
     */
    @Test
    public void testUpdatProfile() throws JsonProcessingException{
        Profile profile = new Profile();
        User user = new User();
        user.setUsername("Sibendu");
        user.setPassword("sibendu.dey@gmail.com");
        user.setProfile(profile);
        NetworkRequest networkRequest = networkRequestFactory.createUpdateUserProfile(profile, user);
        String expectedJSON = CommunicationUtils.getObjectMapper().writeValueAsString(user);
        Assert.assertEquals(expectedJSON, networkRequest.payload().jsonString());
    }

    /**
     * Test to make a Update Profile Request with details.
     * @throws JsonProcessingException if the Json isn't processed properly.
     */
    @Test
    public void testUpdatProfileWithDetails() throws JsonProcessingException{
        Profile profile = new Profile();
        User user = new User();
        user.setUsername("Sibendu");
        user.setPassword("sibendu.dey@gmail.com");
        user.setProfile(profile);
        String email = "sibendu.dey@yahoo.com";
        String imageUrl = "google.com";
        NetworkRequest networkRequest = networkRequestFactory.createUpdateUserProfile(email, imageUrl,
                user);
        profile.setEmail(email);
        profile.setImageUrl(imageUrl);
        String expectedJSON = CommunicationUtils.getObjectMapper().writeValueAsString(user.getProfile());
        Assert.assertEquals(expectedJSON, networkRequest.payload().jsonString());
    }

    /**
     * Test for an update user credentials request.
     * @throws JsonProcessingException if the Json isn't processed properly.
     */
    @Test
    public void testCreateUpdateUserCredentials() throws JsonProcessingException{
        String password = "Banjo";
        User user = new User();
        user.setPassword("Banjo");
        NetworkRequest networkRequest =
                networkRequestFactory.createUpdateUserCredentials(password, user);
        String expectedJSON = CommunicationUtils.getObjectMapper().writeValueAsString(user);
        Assert.assertEquals(expectedJSON, networkRequest.payload().jsonString());
    }

    /**
     * Test to create forgot password request.
     * @throws JsonProcessingException if the Json isn't processed properly.
     */
    @Test
    public void testCreateForgotPasswordRequest() throws JsonProcessingException{
        String email = "sibendu.dey@gmail.com";
        NetworkRequest networkRequest = networkRequestFactory.createForgotPasswordRequest(email);
        User user = new User();
        Profile profile = new Profile();
        profile.setEmail(email);
        user.setProfile(profile);
        String expectedJSON = CommunicationUtils.getObjectMapper().writeValueAsString(user);
        Assert.assertEquals(expectedJSON, networkRequest.payload().jsonString());
    }

    /**
     * Test to create a search user request.
     * @throws JsonProcessingException if the Json isn't processed properly.
     */
    @Test
    public void testCreateSearchUserRequest() throws JsonProcessingException{
        String username = "banjo";
        NetworkRequest networkRequest = networkRequestFactory.createSearchUserRequest(username);
        User user = new User();
        user.setUsername("banjo");
        String expectedJSON = CommunicationUtils.getObjectMapper().writeValueAsString(user);
        Assert.assertEquals(expectedJSON, networkRequest.payload().jsonString());
    }

    /**
     * Test to create a search group request.
     * @throws JsonProcessingException if the Json isn't processed properly.
     */
    @Test
    public void testCreateSearchGroupRequest() throws JsonProcessingException{
        String username = "banjo";
        NetworkRequest networkRequest = networkRequestFactory.createSearchGroupRequest(username);
        Group group = new Group();
        group.setName("banjo");
        String expectedJSON = CommunicationUtils.getObjectMapper().writeValueAsString(group);
        Assert.assertEquals(expectedJSON, networkRequest.payload().jsonString());
    }

    /**
     * Test to create a group request.
     * @throws JsonProcessingException if the Json isn't processed properly.
     */
    @Test
    public void testCreateGroupRequest() throws JsonProcessingException{
        String username = "banjo";
        String groupCode = "hey";
        User admin = new User();
        NetworkRequest networkRequest = networkRequestFactory.createGroupRequest(username,
                groupCode, admin);
        Group group = new Group();
        group.setName(username);
        group.setGroupCode(groupCode);
        List<User> users = new ArrayList<>();
        users.add(admin);
        group.setModerators(users);
        String expectedJSON = CommunicationUtils.getObjectMapper().writeValueAsString(group);
        Assert.assertEquals(expectedJSON, networkRequest.payload().jsonString());
    }

    /**
     * Test to create a select chat request.
     * @throws JsonProcessingException if the Json isn't processed properly.
     */
    @Test
    public void testCreateSelectChatRequest() throws JsonProcessingException{
        String username = "banjo";
        NetworkRequest networkRequest = networkRequestFactory.createSelectChatRequest(username);
        User user = new User();
        user.setUsername(username);
        String expectedJSON = CommunicationUtils.getObjectMapper().writeValueAsString(user);
        Assert.assertEquals(expectedJSON, networkRequest.payload().jsonString());
    }

    /**
     * Test to create a message request.
     * @throws JsonProcessingException if the Json isn't processed properly.
     */
    @Test
    public void testCreateMessageRequest() throws JsonProcessingException{
        String body = "banjo";
        NetworkRequest networkRequest = networkRequestFactory.createMessageRequest(body);
        Message message = new Message();
        message.setMessage(body);
        String expectedJSON = CommunicationUtils.getObjectMapper().writeValueAsString(message);
        Assert.assertEquals(expectedJSON, networkRequest.payload().jsonString());
    }

    /**
     * Test to create a join group request.
     * @throws JsonProcessingException if the Json isn't processed properly.
     */
    @Test
    public void testCreateJoinGroup() throws JsonProcessingException{
        String groupCode = "banjo";
        int id = 1;
        NetworkRequest networkRequest = networkRequestFactory.createJoinGroup(groupCode);
        Group newGroup = new Group();
        newGroup.setGroupCode(groupCode);
        User user = new User();
        user.setId(id);
        newGroup.addUser(user);
        Assert.assertEquals("{\"groupCode\":\"banjo\"}", networkRequest.payload().jsonString());
    }

    /**
     * Test to create delete group request.
     * @throws JsonProcessingException if the Json isn't processed properly.
     */
    @Test
    public void testCreateDeleteGroupRequest() throws JsonProcessingException{
        String code = "banjo";
        String name = "Bayonetta";
        User user = new User();
        NetworkRequest networkRequest = networkRequestFactory.createDeleteGroupRequest(code, name
                , user);
        Group group = new Group();
        group.setName(code);
        group.setGroupCode(name);
        List<User> moderators = new ArrayList<>();

        moderators.add(user);
        group.setModerators(moderators);
        String expectedJSON = CommunicationUtils.getObjectMapper().writeValueAsString(group);
        Assert.assertEquals(expectedJSON, networkRequest.payload().jsonString());
    }

    /**
     * Test to create a user profile request.
     * @throws JsonProcessingException if the Json isn't processed properly.
     */
    @Test
    public void testCreateUserProfile() throws JsonProcessingException{
        String email = "banjo";
        String url = "banjo.com";
        NetworkRequest networkRequest = networkRequestFactory.createUserProfile(email, url);
        Profile profile = new Profile();
        profile.setImageUrl(url);
        profile.setEmail(email);
        String expectedJSON = CommunicationUtils.getObjectMapper().writeValueAsString(profile);
        Assert.assertEquals(expectedJSON, networkRequest.payload().jsonString());
    }

    /**
     * Test to create an update user Profile request.
     * @throws JsonProcessingException if the Json isn't processed properly.
     */
    @Test
    public void testCeateUpdateUserProfile() throws JsonProcessingException{
        String email = "banjo";
        String url = "banjo.com";
        User user = new User();
        user.setUsername("jer");
        Profile profile = new Profile();
        profile.setImageUrl(url);
        profile.setEmail(email);
        NetworkRequest networkRequest = networkRequestFactory.createUpdateUserProfile(profile, user);

        user.setProfile(profile);

        String expectedJSON = CommunicationUtils.getObjectMapper().writeValueAsString(user);
        Assert.assertEquals(expectedJSON, networkRequest.payload().jsonString());
    }

    @Test
    public void testCreateGetUserFollowersList() throws JsonProcessingException{
        String username = "banjo";
        NetworkRequest networkRequest = networkRequestFactory.createGetUserFollowersList(username);
        User user = new User();
        user.setUsername(username);
        String expectedJSON = CommunicationUtils.getObjectMapper().writeValueAsString(user);
        Assert.assertEquals(expectedJSON, networkRequest.payload().jsonString());
    }

    /**
     * Test to get User Followees List request.
     * @throws JsonProcessingException if the Json isn't processed properly.
     */
    @Test
    public void testCreateGetUserFolloweesList() throws JsonProcessingException{
        String username = "banjo";
        NetworkRequest networkRequest = networkRequestFactory.createGetUserFolloweesList(username);
        User user = new User();
        user.setUsername(username);
        String expectedJSON = CommunicationUtils.getObjectMapper().writeValueAsString(user);
        Assert.assertEquals(expectedJSON, networkRequest.payload().jsonString());
    }

    /**
     * Test to create an set User Followers List request.
     * @throws JsonProcessingException if the Json isn't processed properly.
     */
    @Test
    public void testCreateSetUserFolloweresList() throws JsonProcessingException{
        String username = "banjo";
        User user = new User();
        user.setUsername(username);
        NetworkRequest networkRequest =
                networkRequestFactory.createSetUserFolloweresList(username, user);
        String expectedJSON = CommunicationUtils.getObjectMapper().writeValueAsString(user);
        Assert.assertEquals(expectedJSON+ "\n" + username, networkRequest.payload().jsonString());
    }


}
