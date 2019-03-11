package edu.northeastern.ccs.im.entity;

//import edu.northeastern.ccs.im.service.*;
//import edu.northeastern.ccs.im.userGroup.*;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import static org.junit.Assert.*;

/**
 * The Test class for the User
 */
public class UserTest {
//    private User userOne;
//    private User userTwo;
//    private User userThree;
//    private Profile profileOne;
//    private Profile profileTwo;
//    private Profile getProfileThree;
//    private UserService userServiceOne;
//    private UserService userServiceTwo;
//    private UserService userServiceThree;
//    private static final String JOHN = "John";
//    private static final String STARK = "Danny";
//    private static final String DANNY = "Stark";
//
//    /**
//     * Sets up the users information
//     */
//    @Before
//    public void setUp() {
//        profileOne = new Profile();
//        profileTwo = new Profile();
//        getProfileThree = new Profile();
//        profileOne.setId(1);
//        profileTwo.setId(2);
//        getProfileThree.setId(3);
//        profileOne.setName(JOHN);
//        profileTwo.setName(DANNY);
//        getProfileThree.setName(STARK);
//        userOne = new User();
//        userTwo = new User();
//        userThree = new User();
//        userOne.setId(1);
//        userTwo.setId(2);
//        userThree.setId(3);
//        userOne.setProfile(profileOne);
//        userTwo.setProfile(profileTwo);
//        userThree.setProfile(getProfileThree);
//        userServiceOne = new UserService(userOne);
//        userServiceTwo = new UserService(userTwo);
//        userServiceThree = new UserService(userThree);
//    }
//
//    /**
//     * Test to ensure getter is working properly.
//     */
//    @Test
//    public void testGetId() {
//        assertEquals(1, userOne.getId());
//        assertEquals(2, userTwo.getId());
//        assertEquals(3, userThree.getId());
//    }
//
//    /**
//     * Test to ensure the setter is working properly.
//     */
//    @Test
//    public void testTestId() {
//        assertEquals(1, userOne.getId());
//        userOne.setId(2);
//        assertEquals(2, userOne.getId());
//    }
//
//    /**
//     * Tests to ensure user starts with an empty list.
//     */
//    @Test
//    public void testGetMessages() {
//        assertEquals(0, userOne.getMessages().size());
//    }
//
//    /**
//     * Test to ensure we can add a message to a list.
//     */
//    @Test
//    public void testSetMessages() {
//        List<Message> messages = new ArrayList<>();
//        Message newMessage = new Message();
//        messages.add(newMessage);
//        userOne.setMessages(messages);
//        assertEquals(1, userOne.getMessages().size());
//    }
//
//    /**
//     * Test to ensure the group size is zero at the start.
//     */
//    @Test
//    public void testGetGroups() {
//        assertEquals(0, userOne.getGroups().size());
//    }
//
//    /**
//     * Test to ensure we can add a group to a list.
//     */
//    @Test
//    public void testSetGroups() {
//        List<IGroup> groups = new ArrayList<>();
//        IGroup newGroup = new Group();
//        groups.add(newGroup);
//        userOne.setGroups(groups);
//        assertEquals(1, userOne.getGroups().size());
//    }
//
//    /**
//     * Adds a message to the list of messages.
//     */
//    @Test
//    public void testAddMessage() {
//        Message newMessage = new Message();
//        userOne.addMessages(newMessage);
//        assertEquals(1, userOne.getMessages().size());
//    }
//
//    /**
//     * Adds a group to the list of messages.
//     */
//    @Test
//    public void testAddGroup() {
//        IGroup newGroup = new Group();
//        userOne.addGroup(newGroup);
//        assertEquals(1, userOne.getGroups().size());
//    }
//
//    /**
//     * Tests to ensure we get the correct profile.
//     */
//    @Test
//    public void testGetProfile() {
//        assertEquals(JOHN, userOne.getProfile().getName());
//        assertEquals(DANNY, userTwo.getProfile().getName());
//        assertEquals(STARK, userThree.getProfile().getName());
//        assertEquals(JOHN, profileOne.getName());
//        assertEquals(DANNY, profileTwo.getName());
//        assertEquals(STARK, getProfileThree.getName());
//    }
//
//    /**
//     * Tests to ensure the list of followers starts empty.
//     */
//    @Test
//    public void testGetFollowers() {
//        assertEquals(0, userOne.getFollowers().size());
//    }
//
//    /**
//     * Tests to ensure we can add a follower
//     */
//    @Test
//    public void testAddFollower() {
//        List<IUser> followers = new ArrayList<>();
//        followers.add(userTwo);
//        followers.add(userThree);
//        userOne.setFollowers(followers);
//        assertEquals(2, userOne.getFollowers().size());
//    }
//
//    /**
//     * Tests to ensure the list of following starts empty.
//     */
//    @Test
//    public void testGetFollowing() {
//        assertEquals(0, userOne.getFollowing().size());
//    }
//
//    /**
//     * Tests to ensure we can add a people to follow
//     */
//    @Test
//    public void testAddFollowing() {
//        List<IUser> following = new ArrayList<>();
//        following.add(userTwo);
//        following.add(userThree);
//        userOne.setFollowing(following);
//        assertEquals(2, userOne.getFollowing().size());
//    }
//
//    /**
//     * Tests to ensure we get the right user when we search for them.
//     */
//    @Test
//    public void testSearch() {
//        List<IUser> following = new ArrayList<>();
//        following.add(userTwo);
//        following.add(userThree);
//        userOne.setFollowing(following);
//        assertEquals(userTwo.getProfile().getName(), userServiceOne.search(DANNY).getProfile().getName());
//    }
//
//    /**
//     * Test to ensure we can add someone to the list of people we follow.
//     */
//    @Test
//    public void testFollow() {
//        List<IUser> following = new ArrayList<>();
//        following.add(userTwo);
//        userOne.setFollowing(following);
//        assertEquals(userTwo.getProfile().getName(), userServiceOne.search(DANNY).getProfile().getName());
//        assertEquals(1, userOne.getFollowing().size());
//        userServiceOne.follow(userThree);
//        assertEquals(userThree.getProfile().getName(), userServiceOne.search(STARK).getProfile().getName());
//        userServiceOne.follow(userTwo);
//        assertEquals(2, userOne.getFollowing().size());
//    }
//
//    /**
//     * Tests we can send a message.
//     */
//    @Test
//    public void testSendMessage(){
//        List<IGroup> groups = new ArrayList<>();
//        Group newGroup = new Group();
//        newGroup.setId(1);
//        groups.add(newGroup);
//        Group newGroup2 = new Group();
//        newGroup.setId(2);
//        groups.add(newGroup2);
//        userOne.setGroups(groups);
//        userServiceOne.sendMessage("Hello", 2);
//        assertEquals("Hello", userOne.getMessages().get(0).getMessage());
//    }
//
//    /**
//     * Tests to ensure we can set an expiration to a message.
//     */
//    @Test
//    public void testSetExpiration() {
//        Message newMessage = new Message();
//        newMessage.setId(1);
//        Date newDate = new Date();
//        newDate.setTime(3L);
//        userOne.addMessages(newMessage);
//        userServiceOne.setExpiration(1, newDate);
//        assertEquals(3L, userOne.getMessages().get(0).getExpiration());
//    }
//
//    /**
//     * Test to ensure we can delete a group.
//     */
//    @Test
//    public void testDeleteGroup() {
//        List<IGroup> groups = new ArrayList<>();
//        Group newGroup = new Group();
//        newGroup.setName("Hello");
//        newGroup.setId(1);
//        groups.add(newGroup);
//        Group newGroup2 = new Group();
//        newGroup2.setName("World");
//        newGroup.setId(2);
//        groups.add(newGroup2);
//        userOne.setGroups(groups);
//        assertEquals(2, userOne.getGroups().size());
//        userServiceOne.deleteGroup(2);
//        assertEquals(1, userOne.getGroups().size());
//        assertEquals(0, userOne.getGroups().get(0).getId());
//    }
//
//    /**
//     * Test to ensure we can add a group.
//     */
//    @Test
//    public void testCreateGroup() {
//        assertEquals(0, userOne.getGroups().size());
//        userServiceOne.createIGroup(4);
//        assertEquals(1, userOne.getGroups().size());
//        assertEquals(4, userOne.getGroups().get(0).getId());
//    }
}
