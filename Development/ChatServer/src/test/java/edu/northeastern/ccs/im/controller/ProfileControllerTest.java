package edu.northeastern.ccs.im.controller;

import edu.northeastern.ccs.im.communication.NetworkResponse;
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

//    private ProfileController profileController;
//    private ProfileService profileService;
//    private Group groupOne;
//    private User userOne;
//    private Profile profileOne;
//    private List<Group> groupList;
//
//    @Before
//    public void setUp() throws IOException {
//        profileController = new ProfileController();
//        profileService = mock(ProfileService.class);
//
//        //setup a group
//        groupOne = new Group();
//        groupOne.setName("groupOneTest");
//        groupOne.setGroupCode("123group");
//        groupList = new ArrayList<>();
//        groupList.add(groupOne);
//
//        //setup a user
//        userOne = new User();
//        userOne.setPassword("hello123");
//        userOne.setUsername("Hello");
//        userOne.setId(12);
//
//        //setup a profile
//        profileOne = new Profile();
//        profileOne.setId(5);
//        profileOne.setEmail("abcd@gmail.com");
//        profileOne.setImageUrl("http://abcd.com");
//
//    }
//
//    /**
//     * Test for successful create Profile in Profile Controller
//     */
//    @Test
//    public void testSuccessfulCreateProfile(){
//        when(profileService.createProfile(any())).thenReturn(true);
//        profileController.setProfileService(profileService);
//        NetworkResponse networkResponse = profileController.addEntity(profileOne);
//        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
//    }
//
//    /**
//     * Test for unsuccessful create Profile in Profile Controller
//     */
//    @Test
//    public void testUnsuccessfulCreateProfile(){
//
//        when(profileService.createProfile(any())).thenThrow(IllegalArgumentException.class);
//        profileController.setProfileService(profileService);
//        NetworkResponse networkResponse = profileController.addEntity(profileOne);
//        Assert.assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
//    }
//
//    /**
//     * Test for successful get Profile in Profile Controller
//     */
//    @Test
//    public void testSuccessfulGetProfile(){
//        when(profileService.get(anyInt())).thenReturn(profileOne);
//        profileController.setProfileService(profileService);
//        NetworkResponse networkResponse = profileController.getEntity(profileOne.getId());
//        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
//    }
//
//    /**
//     * Test for unsuccessful get Profile in Profile Controller
//     */
//    @Test
//    public void testUnsuccessfulGetProfile(){
//        when(profileService.get(anyInt())).thenThrow(IllegalArgumentException.class);
//        profileController.setProfileService(profileService);
//        NetworkResponse networkResponse = profileController.getEntity(4);
//        Assert.assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
//    }
//
//    /**
//     * Test for successful update Profile in Profile Controller
//     */
//    @Test
//    public void testSuccessfulUpdateProfile(){
//        when(profileService.updateProfile(any())).thenReturn(true);
//        profileController.setProfileService(profileService);
//        NetworkResponse networkResponse = profileController.updateEntity(profileOne);
//        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
//    }
//
//    /**
//     * Test for unsuccessful update Profile in Profile Controller
//     */
//    @Test
//    public void testUnsuccessfulUpdateProfile(){
//        when(profileService.updateProfile(any())).thenThrow(IllegalArgumentException.class);
//        profileController.setProfileService(profileService);
//        NetworkResponse networkResponse = profileController.updateEntity(profileOne);
//        Assert.assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
//    }
//
//    /**
//     * Test for successful delete Profile in Profile Controller
//     */
//    @Test
//    public void testSucessfulDeleteProfile(){
//        when(profileService.deleteProfile(any())).thenReturn(true);
//        profileController.setProfileService(profileService);
//        NetworkResponse networkResponse = profileController.deleteEntity(profileOne);
//        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
//    }
//
//    /**
//     * Test for unsuccessful delete Profile in Profile Controller
//     */
//    @Test
//    public void testUnsucessfulDeleteProfile(){
//        when(profileService.deleteProfile(any())).thenThrow(IllegalArgumentException.class);
//        profileController.setProfileService(profileService);
//        NetworkResponse networkResponse = profileController.deleteEntity(profileOne);
//        Assert.assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
//    }
//
//    /**
//     * Test for search profile
//     */
//    @Test
//    public void testSearchProfile() {
//        assertNull(profileController.searchEntity("ABCD"));
//    }
}
