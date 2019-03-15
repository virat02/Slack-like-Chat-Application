package edu.northeastern.ccs.im.entity;

import edu.northeastern.ccs.im.user_group.Message;
import edu.northeastern.ccs.im.user_group.Group;
import edu.northeastern.ccs.im.user_group.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the User Class.
 */
public class UserEntityTest {
    private User user;

    /**
     * Sets up the user for the UserEntityTest Class.
     */
    @Before
    public void setUp() {
        user = new User();
    }

    /**
     * Tests the addMessages Method.
     */
    @Test
    public void testAddMessages() {
        Message message = new Message();
        user.addMessages(message);
        assertEquals(message, user.getMessages().get(0));
    }

    /**
     * Tests the addGroup Method.
     */
    @Test
    public void testAdGroup() {
        Group group = new Group();
        user.addGroup(group);
        assertEquals(group, user.getGroups().get(0));
    }

    /**
     * Tests the addFollowing method when we pass it a null value.
     */
    @Test (expected = NullPointerException.class)
    public void addFollowingFail() {
        user.addFollowing(null);
    }
}