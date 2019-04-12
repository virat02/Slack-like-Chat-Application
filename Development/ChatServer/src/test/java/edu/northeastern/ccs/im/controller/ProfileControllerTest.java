package edu.northeastern.ccs.im.controller;

import edu.northeastern.ccs.im.communication.NetworkResponse;
import edu.northeastern.ccs.im.customexceptions.*;
import edu.northeastern.ccs.im.service.ProfileService;
import edu.northeastern.ccs.im.user_group.Group;
import edu.northeastern.ccs.im.user_group.Profile;
import edu.northeastern.ccs.im.user_group.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProfileControllerTest {

    private ProfileController profileController;
    private ProfileService profileService;
    private Group groupOne;
    private User userOne;
    private Profile profileOne;
    private List<Group> groupList;

    @Before
    public void setUp() {
        profileController = ProfileController.getInstance();
        profileService = mock(ProfileService.class);

        //setup a group
        groupOne = new Group();
        groupOne.setName("groupOneTest");
        groupOne.setGroupCode("123group");
        groupList = new ArrayList<>();
        groupList.add(groupOne);

        //setup a user
        userOne = new User();
        userOne.setPassword("hello123");
        userOne.setUsername("Hello");
        userOne.setId(12);

        //setup a profile
        profileOne = new Profile();
        profileOne.setId(5);
        profileOne.setEmail("abcd@gmail.com");
        profileOne.setImageUrl("http://abcd.com");

    }

    /**
     * Test for successful create Profile in Profile Controller
     */
    @Test
    public void testSuccessfulCreateProfile() throws InvalidEmailException, InvalidImageURLException {
        when(profileService.createProfile(any())).thenReturn(true);
        profileController.setProfileService(profileService);
        NetworkResponse networkResponse = profileController.addEntity(profileOne);
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
    }

    /**
     * Test for unsuccessful create Profile in Profile Controller
     */
    @Test
    public void testUnsuccessfulCreateProfile() throws InvalidEmailException, InvalidImageURLException {

        when(profileService.createProfile(any())).thenReturn(false);
        profileController.setProfileService(profileService);
        NetworkResponse networkResponse = profileController.addEntity(profileOne);
        Assert.assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
    }

    /**
     * Test for unsuccessful create Profile in Profile Controller for InvalidEmailException
     */
    @Test
    public void testUnsuccessfulCreateProfileForInvalidEmailException() throws InvalidEmailException, InvalidImageURLException {

        when(profileService.createProfile(any())).thenThrow(new InvalidEmailException("Invalid Email!"));
        profileController.setProfileService(profileService);
        NetworkResponse networkResponse = profileController.addEntity(profileOne);
        Assert.assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
    }

    /**
     * Test for unsuccessful create Profile in Profile Controller for InvalidImageURLException
     */
    @Test
    public void testUnsuccessfulCreateProfileForInvalidImageURLException() throws InvalidEmailException, InvalidImageURLException {

        when(profileService.createProfile(any())).thenThrow(new InvalidImageURLException("Invalid Image URL!"));
        profileController.setProfileService(profileService);
        NetworkResponse networkResponse = profileController.addEntity(profileOne);
        Assert.assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
    }

    /**
     * Test for successful get Profile in Profile Controller
     */
    @Test
    public void testSuccessfulGetProfile() throws ProfileNotFoundException {
        when(profileService.get(anyInt())).thenReturn(profileOne);
        profileController.setProfileService(profileService);
        NetworkResponse networkResponse = profileController.getEntity(profileOne.getId());
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
    }

    /**
     * Test for unsuccessful get Profile in Profile Controller
     */
    @Test
    public void testUnsuccessfulGetProfile() throws ProfileNotFoundException {
        when(profileService.get(anyInt())).thenThrow(ProfileNotFoundException.class);
        profileController.setProfileService(profileService);
        NetworkResponse networkResponse = profileController.getEntity(4);
        Assert.assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
    }

    /**
     * Test for successful update Profile in Profile Controller
     */
    @Test
    public void testSuccessfulUpdateProfile() throws ProfileNotFoundException {
        when(profileService.updateProfile(any())).thenReturn(true);
        profileController.setProfileService(profileService);
        NetworkResponse networkResponse = profileController.updateEntity(profileOne);
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
    }

    /**
     * Test for unsuccessful update Profile in Profile Controller
     */
    @Test
    public void testUnsuccessfulUpdateProfile() throws ProfileNotFoundException {
        when(profileService.updateProfile(any())).thenThrow(ProfileNotFoundException.class);
        profileController.setProfileService(profileService);
        NetworkResponse networkResponse = profileController.updateEntity(profileOne);
        Assert.assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
    }

    /**
     * Test for successful delete Profile in Profile Controller
     */
    @Test
    public void testSuccessfulDeleteProfile() throws ProfileNotFoundException {
        when(profileService.deleteProfile(any())).thenReturn(true);
        profileController.setProfileService(profileService);
        NetworkResponse networkResponse = profileController.deleteEntity(profileOne);
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
    }

    /**
     * Test for unsuccessful delete Profile in Profile Controller
     */
    @Test
    public void testUnsuccessfulDeleteProfile() throws ProfileNotFoundException {
        when(profileService.deleteProfile(any())).thenReturn(false);
        profileController.setProfileService(profileService);
        NetworkResponse networkResponse = profileController.deleteEntity(profileOne);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
    }

    /**
     * Test for unsuccessful delete Profile in Profile Controller when trying to delete a non-existing profile
     */
    @Test
    public void testUnsuccessfulDeleteProfileForNonExistingProfile()
            throws ProfileNotFoundException {
        when(profileService.deleteProfile(any())).thenThrow(new ProfileNotFoundException("Profile not found!"));
        profileController.setProfileService(profileService);
        NetworkResponse networkResponse = profileController.deleteEntity(profileOne);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
    }

    /**
     * Test for search profile
     */
    @Test
    public void testSearchProfile() {
        assertNull(profileController.searchEntity("ABCD"));
    }
}
