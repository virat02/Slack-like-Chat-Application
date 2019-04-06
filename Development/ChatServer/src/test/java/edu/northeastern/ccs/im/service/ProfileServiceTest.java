package edu.northeastern.ccs.im.service;

import edu.northeastern.ccs.im.customexceptions.*;
import edu.northeastern.ccs.im.service.jpa_service.AllJPAService;
import edu.northeastern.ccs.im.service.jpa_service.ProfileJPAService;
import edu.northeastern.ccs.im.user_group.Message;
import edu.northeastern.ccs.im.user_group.Profile;
import edu.northeastern.ccs.im.user_group.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Class to test the profile service methods
 */

@RunWith(MockitoJUnitRunner.class)
public class ProfileServiceTest {

    private ProfileService profileService = new ProfileService();

    private AllJPAService jpaService;

    private ProfileJPAService profileJPAService;

    private UserService userService;

    @Mock
    private Message m = new Message();

    @Mock
    private Message m1 = new Message();

    private User u = new User();
    private Profile p1;

    private static final String P1_EMAIL = "p1@gmail.com";
    private static final String P1_IMAGE_URL = "https://p1.com";

    /**
     * Sets up the required information
     */
    @Before
    public void setUp() {

        jpaService = mock(AllJPAService.class);
        profileJPAService = mock(ProfileJPAService.class);
        userService = mock(UserService.class);

        m = mock(Message.class);
        m1 = mock(Message.class);

        //Setup a user
        u.setId(1);
        u.setPassword("password");
        u.setUsername("virat");

        //Setup a profile
        p1 = new Profile();
        p1.setId(1);
        p1.setEmail(P1_EMAIL);
        p1.setImageUrl(P1_IMAGE_URL);
    }

    /**
     * Test for successful create profile in ProfileService
     */
    @Test
    public void testCreateProfile() throws InvalidEmailException, InvalidImageURLException {

        //AllJPAService mjpaService = mock(AllJPAService.class);
        profileService.setAllJPAService(jpaService);
        when(jpaService.createEntity(any())).thenReturn(true);
        assertTrue(profileService.createProfile(p1));
    }

    /**
     * Test for unable to create profile in ProfileService for throwing ProfileNotPersistedException
     */
    @Test
    public void testCreateProfileForFalse() throws InvalidEmailException, InvalidImageURLException {

        //AllJPAService jpaService = mock(AllJPAService.class);
        profileService.setAllJPAService(jpaService);
        when(jpaService.createEntity(any(Profile.class))).thenReturn(false);
        assertFalse(profileService.createProfile(p1));
    }

    /**
     * Test for unable to create profile in ProfileService for throwing InvalidEmailException
     */
    @Test(expected = InvalidEmailException.class)
    public void testCreateProfileForInvalidEmailException()
            throws InvalidEmailException, InvalidImageURLException {

        Profile p = new Profile();
        p.setEmail("abcd");

        profileService.createProfile(p);
    }

    /**
     * Test for unable to create profile in ProfileService for throwing InvalidEmailException for null email id
     */
    @Test(expected = InvalidEmailException.class)
    public void testCreateProfileForInvalidEmailExceptionForNullEmailId()
            throws InvalidEmailException, InvalidImageURLException {

        Profile p = new Profile();
        p.setEmail(null);

        profileService.createProfile(p);
    }

    /**
     * Test successful update profile method
     */
    @Test
    public void testUpdateProfileService() throws ProfileNotFoundException {

        when(profileJPAService.updateProfile(any(Profile.class))).thenReturn(true);
        profileService.setProfileJPAService(profileJPAService);
        assertTrue(profileService.updateProfile(p1));
    }

    /**
     * Test unsuccessful update profile method
     */
    @Test
    public void testUpdateProfileServiceUnsuccessful() throws ProfileNotFoundException {

        when(profileJPAService.updateProfile(any(Profile.class))).thenReturn(false);
        profileService.setProfileJPAService(profileJPAService);
        assertFalse(profileService.updateProfile(p1));
    }

    /**
     * Test update profile method for ProfileNotFoundException
     */
    @Test(expected = ProfileNotFoundException.class)
    public void testUpdateProfileServiceForProfileNotFoundException() throws ProfileNotFoundException {

        when(profileJPAService.updateProfile(any(Profile.class))).thenThrow(new ProfileNotFoundException("Could not find profile!"));
        profileService.setProfileJPAService(profileJPAService);
        assertFalse(profileService.updateProfile(p1));
    }

    /**
     * Test get profile method
     */
    @Test
    public void testGetProfile() throws ProfileNotFoundException {

        when(profileJPAService.getProfile(anyInt())).thenReturn(p1);
        profileService.setProfileJPAService(profileJPAService);
        assertEquals(p1, profileService.get(2));
    }

    /**
     * Test get message method to return null
     */
    @Test(expected = ProfileNotFoundException.class)
    public void testGetProfileForProfileNotFoundException() throws ProfileNotFoundException {

        when(profileJPAService.getProfile(anyInt())).thenThrow(new ProfileNotFoundException("Could not find profile!"));
        profileService.setProfileJPAService(profileJPAService);
        profileService.get(2);
    }

    /**
     * Test the delete profile method when profile is deleted
     */
    @Test
    public void testDeleteProfile() {
        when(jpaService.deleteEntity(any(Profile.class))).thenReturn(true);
        profileService.setAllJPAService(jpaService);
        assertTrue(profileService.deleteProfile(p1));
    }

    /**
     * Test the delete profile method when profile is not deleted
     */
    @Test
    public void testDeleteProfileFalse() {
        when(jpaService.deleteEntity(any(Profile.class))).thenReturn(false);
        profileService.setAllJPAService(jpaService);
        assertFalse(profileService.deleteProfile(p1));
    }

    /**
     * Test for a valid Image URL entered by User
     *
     * @throws InvalidImageURLException
     * @throws InvalidEmailException
     */
    @Test
    public void testValidImageURL() throws InvalidImageURLException, InvalidEmailException {
        Profile p = new Profile();
        p.setEmail("virat@gmail.com");
        p.setImageUrl("http://virat.com");
        profileService.setProfileJPAService(profileJPAService);
        when(profileJPAService.createProfile(any())).thenReturn(p);
        assertEquals(p, profileService.createProfile(p));
    }

    /**
     * Test for a Invalid Image URL entered by User
     *
     * @throws InvalidImageURLException
     * @throws InvalidEmailException
     */
    @Test(expected = InvalidImageURLException.class)
    public void testInvalidImageURL() throws InvalidImageURLException, InvalidEmailException {
        Profile p = new Profile();
        p.setEmail("virat@gmail.com");
        p.setImageUrl("virat.com");

        assertEquals(p, profileService.createProfile(p));
    }
}
