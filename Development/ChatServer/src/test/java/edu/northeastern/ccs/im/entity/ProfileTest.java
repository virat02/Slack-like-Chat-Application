package edu.northeastern.ccs.im.entity;

import edu.northeastern.ccs.im.service.ProfileService;
import edu.northeastern.ccs.im.userGroup.IUser;
import edu.northeastern.ccs.im.userGroup.Profile;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class ProfileTest {

    private Profile p1;
    private Profile p2;
    private Profile p3;

    private IUser u1;
    private IUser u2;
    private IUser u3;

    private static final String VIRAT = "Virat";
    private static final String SANGEETHA = "Sangeetha";
    private static final String JERRY = "Jerry";
    private static final String P1_EMAIL= "p1@gmail.com";
    private static final String P1_IMAGE_URL = "http://p1.com";
    private static final String P2_EMAIL = "p2@gmail.com";
    private static final String P2_IMAGE_URL = "http://p2.com";
    private static final String P1_PASSWORD = "profile01";
    private static final String P2_PASSWORD = "profile02";
    private static final String P3_PASSWORD = "profile03";
    private static final String P3_EMAIL = "p3@gmail.com";
    private static final String P3_IMAGE_URL = "http://p3.com";
    private static final String ID_2 = "Id: 2\n";

    /**
     * Sets up the users information
     */
    @Before
    public void setUp() {
        p1 = new Profile();
        p1.setId(1);
        p1.setUsername(VIRAT);
        p1.setEmail(P1_EMAIL);
        p1.setPassword(P1_PASSWORD);
        p1.setImageUrl(P1_IMAGE_URL);
        p1.setProfileAccess(true);
        p1.setUser(u1);

        p2 = new Profile();
        p2.setId(2);
        p2.setUsername(SANGEETHA);
        p2.setEmail(P2_EMAIL);
        p2.setPassword(P2_PASSWORD);
        p2.setImageUrl(P2_IMAGE_URL);
        p2.setProfileAccess(false);
        p2.setUser(u2);

        p3 = new Profile();
        p3.setId(3);
        p3.setUsername(JERRY);
        p3.setEmail(P3_EMAIL);
        p3.setPassword(P3_PASSWORD);
        p3.setImageUrl(P3_IMAGE_URL);
        p3.setProfileAccess(true);
        p3.setUser(u3);
    }

    /**
     * Test create profile method
     */
    @Test
    public void testCreateProfile() {
        Profile p3Actual;
        ProfileService service = new ProfileService(p3);
        p3Actual = service.createProfile(JERRY, P3_EMAIL, P3_PASSWORD, P3_IMAGE_URL, true);

        assertEquals(p3.toString(), p3Actual.toString());
    }

    /**
     * Test create profile method for an invalid password
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateProfileInvalidPassword() {
        ProfileService service = new ProfileService(p3);
        service.createProfile(JERRY, P3_EMAIL, "p3", P3_IMAGE_URL, true);

    }

    /**
     * Test create profile method for an invalid email
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateProfileInvalidEmail() {
        ProfileService service = new ProfileService(p2);
        service.createProfile(SANGEETHA, "p2gmail.com", P2_PASSWORD, P2_IMAGE_URL, true);

    }

    /**
     * Test create profile method for an invalid username
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateProfileInvalidUsername() {
        ProfileService service = new ProfileService(p2);
        service.createProfile("", P2_EMAIL, P2_PASSWORD, P2_IMAGE_URL, true);

    }

    /**
     * Test create profile method for an invalid image url
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateProfileInvalidImageURL() {
        ProfileService service = new ProfileService(p1);
        service.createProfile(VIRAT, P1_EMAIL, P1_PASSWORD, "abcd", true);

    }

    /**
     * Test update profile method for a valid input
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateProfile() {
        Profile p2Actual;
        ProfileService service = new ProfileService(p2);
        service.updateProfile(VIRAT, "virat.p1@gmail.com", P1_PASSWORD, "profileVirat01", "http://p1-virat.com", false);

        String expected = ID_2 +
                "Username: Virat\n" +
                "Email: virat.p1@gmail.com\n" +
                "Image URL: http://p1-virat.com\n" +
                "Profile visible? : false\n"+
                "Password: profileVirat01";

        assertEquals(expected, p2.toString() + "\nPassword: "+p2.getPassword());
    }

    /**
     * Test update profile method for an invalid input
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateProfileInvalidInput() {
        ProfileService service = new ProfileService(p2);
        service.updateProfile("", P2_EMAIL, P2_PASSWORD, P2_PASSWORD, P2_IMAGE_URL, true);

        String expected = ID_2 +
                "Username: Sangeetha\n" +
                "Email: p2@gmail.com\n" +
                "Image URL: http://p2.com\n" +
                "Profile visible? : true";

        assertEquals(expected, p2.toString());
    }

    /**
     * Test update profile method for a correct old password
     */
    @Test
    public void testUpdateProfileCorrectOldPassword() {
        ProfileService service = new ProfileService(p1);
        service.updatePassword(P1_PASSWORD, P1_PASSWORD+"new");

        assertEquals("profile01new", p1.getPassword());
    }

    /**
     * Test update profile method for an incorrect old password
     */
    @Test
    public void testUpdateProfileIncorrectOldPassword() {
        ProfileService service = new ProfileService(p1);
        Boolean updated = service.updatePassword(P2_PASSWORD, P1_PASSWORD+"new");

        assertFalse(updated);
    }

    /**
     * Test update profile method for an invalid new password
     */
    @Test
    public void testUpdateProfileInvalidNewPassword() {
        ProfileService service = new ProfileService(p3);
        service.updatePassword(P3_PASSWORD, "new");

        assertEquals(P3_PASSWORD, p3.getPassword());
    }

    /**
     * Test the getter and setter method for profile access
     */
    @Test
    public void testGetProfileAccess() {

        p3.setProfileAccess(false);
        Boolean updatedAccess = p3.getProfileAccess();

        assertEquals(false, updatedAccess);
    }

    /**
     * Test the delete profile method
     */
    @Test
    public void testDeleteProfile() {
        ProfileService service = new ProfileService(p2);
        service.deleteProfile();

        String expected = ID_2 +
                "Username: null\n" +
                "Email: null\n" +
                "Image URL: null\n" +
                "Profile visible? : false";

        assertEquals(expected, p2.toString());
    }

}
