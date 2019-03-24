package edu.northeastern.ccs.im.service;

import edu.northeastern.ccs.im.service.jpa_service.ProfileJPAService;
import edu.northeastern.ccs.im.user_group.Message;
import edu.northeastern.ccs.im.user_group.Profile;
import edu.northeastern.ccs.im.user_group.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Class to test the profile service methods
 */
public class ProfileServiceTest {

//    private ProfileService profileService = new ProfileService();
//    private ProfileJPAService profileJPAService;
//    private UserService userService;
//
//    @Mock
//    private Message m = new Message();
//
//    @Mock
//    private Message m1 = new Message();
//
//    private User u = new User();
//    private Profile p1 = new Profile();
//
//    private static final String P1_EMAIL = "p1@gmail.com";
//    private static final String P1_IMAGE_URL = "https://p1.com";
//
//    /**
//     * Sets up the required information
//     */
//    @Before
//    public void setUp() {
//
//        profileJPAService = mock(ProfileJPAService.class);
//        userService = mock(UserService.class);
//
//        m = mock(Message.class);
//        m1 = mock(Message.class);
//
//        //Setup a user
//        u.setId(1);
//        u.setPassword("password");
//        u.setUsername("virat");
//
//        //Setup a profile
//        p1 = new Profile();
//        p1.setId(1);
//        p1.setEmail(P1_EMAIL);
//        p1.setImageUrl(P1_IMAGE_URL);
//    }
//
//    /**
//     *Test for able to create profile in ProfileService
//     */
//    @Test
//    public void testCreateProfile() {
//
//        when(profileJPAService.createProfile(any(Profile.class))).thenReturn(1);
//        when(m.getId()).thenReturn(1);
//        when(profileJPAService.getProfile(anyInt())).thenReturn(p1);
//
//        profileService.setProfileJPAService(profileJPAService);
//
//        assertTrue(profileService.createProfile(p1));
//    }
//
//    /**
//     *Test for unable to create profile in ProfileService
//     */
//    @Test
//    public void testCreateProfileFalse() {
//
//        when(profileJPAService.createProfile(any(Profile.class))).thenReturn(-1);
//        profileService.setProfileJPAService(profileJPAService);
//
//        assertFalse(profileService.createProfile(p1));
//    }
//
//    /**
//     * Test successful update profile method
//     */
//    @Test
//    public void testUpdateProfileService() {
//
//        when(profileJPAService.updateProfile(any(Profile.class))).thenReturn(true);
//        profileService.setProfileJPAService(profileJPAService);
//        assertTrue(profileService.updateProfile(p1));
//    }
//
//    /**
//     * Test unsuccessful update profile method
//     */
//    @Test
//    public void testUpdateProfileServiceUnsuccessful() {
//
//        when(profileJPAService.updateProfile(any(Profile.class))).thenReturn(false);
//        profileService.setProfileJPAService(profileJPAService);
//        assertFalse(profileService.updateProfile(p1));
//    }
//
//    /**
//     * Test get profile method
//     */
//    @Test
//    public void testGetProfile() {
//
//        when(profileJPAService.getProfile(anyInt())).thenReturn(p1);
//        profileService.setProfileJPAService(profileJPAService);
//        assertEquals(p1, profileService.get(2));
//    }
//
//    /**
//     * Test get message method to return null
//     */
//    @Test
//    public void testGetProfileNullMessage() {
//
//        when(profileJPAService.getProfile(anyInt())).thenReturn(null);
//        profileService.setProfileJPAService(profileJPAService);
//        assertNull(profileService.get(2));
//    }
//
//    /**
//     * Test the delete profile method when profile is deleted
//     */
//    @Test
//    public void testDeleteProfile() {
//        when(profileJPAService.deleteProfile(any(Profile.class))).thenReturn(1);
//        profileService.setProfileJPAService(profileJPAService);
//        assertTrue(profileService.deleteProfile(p1));
//    }
//
//    /**
//     * Test the delete profile method when profile is not deleted
//     */
//    @Test
//    public void testDeleteProfileFalse() {
//        when(profileJPAService.deleteProfile(any(Profile.class))).thenReturn(-1);
//        profileService.setProfileJPAService(profileJPAService);
//        assertFalse(profileService.deleteProfile(p1));
//    }
}
