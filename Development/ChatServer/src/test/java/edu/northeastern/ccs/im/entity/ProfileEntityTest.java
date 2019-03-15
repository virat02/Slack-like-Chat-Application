package edu.northeastern.ccs.im.entity;

import edu.northeastern.ccs.im.user_group.Profile;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Class for testing the Profile entity methods
 */
public class ProfileEntityTest {

    @Test
    public void testConstructor() {
        Profile p = new Profile(1,"hi@bye.com", "http://hi.com");

        String expected = "Email: hi@bye.com\n" +
                "Image URL: http://hi.com";
        assertEquals(expected, p.toString());
    }
}
