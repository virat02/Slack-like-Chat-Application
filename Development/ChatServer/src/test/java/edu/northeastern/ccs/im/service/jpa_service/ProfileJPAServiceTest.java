package edu.northeastern.ccs.im.service.jpa_service;

import edu.northeastern.ccs.im.customexceptions.ProfileNotDeletedException;
import edu.northeastern.ccs.im.customexceptions.ProfileNotFoundException;
import edu.northeastern.ccs.im.customexceptions.ProfileNotPersistedException;
import edu.northeastern.ccs.im.user_group.Profile;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for Profile JPA services
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class ProfileJPAServiceTest {

    @Mock
    private ProfileJPAService profileJPAService;

    private EntityManager entityManager;

    private EntityTransaction entityTransaction;

    @Mock
    private Profile p1;

    @Mock
    private Profile p2;

    @Mock
    private Profile p3;

    private static final String P1_EMAIL= "p1@gmail.com";
    private static final String P1_IMAGE_URL = "http://p1.com";
    private static final String P2_EMAIL = "p2@gmail.com";
    private static final String P2_IMAGE_URL = "http://p2.com";
    private static final String P3_EMAIL = "p3@gmail.com";
    private static final String P3_IMAGE_URL = "http://p3.com";

    /**
     * Sets up the users information
     */
    @Before
    public void setUp() {
        entityManager = mock(EntityManager.class);
        entityTransaction = mock(EntityTransaction.class);

        p1 = new Profile();
        p1.setId(1);
        p1.setEmail(P1_EMAIL);
        p1.setImageUrl(P1_IMAGE_URL);

        p2 = new Profile();
        p2.setId(2);
        p2.setEmail(P2_EMAIL);
        p2.setImageUrl(P2_IMAGE_URL);

        p3 = new Profile();
        p3.setId(3);
        p3.setEmail(P3_EMAIL);
        p3.setImageUrl(P3_IMAGE_URL);

        profileJPAService = new ProfileJPAService();
    }

    /**
     * Test the create profile method for Profile JPA Service
     */
    @Test
    public void testCreateProfileForProfileJPAService() throws ProfileNotPersistedException {
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        profileJPAService.setEntityManager(entityManager);
        assertEquals(p1, profileJPAService.createProfile(p1));
    }

    /**
     * Test the create profile method for Profile JPA Service for an exception
     */
    @Test(expected = ProfileNotPersistedException.class)
    public void testCreateProfileNotPersistedException() throws ProfileNotPersistedException {

        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        profileJPAService.setEntityManager(entityManager);
        doThrow(new IllegalArgumentException()).when(entityManager).persist(any(Profile.class));
        profileJPAService.createProfile(p1);
    }

    /**
     * Test the update profile method of ProfileJPAService
     */
    @Test
    public void testUpdateProfileOfProfileJPA() throws ProfileNotFoundException {

        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        profileJPAService.setEntityManager(entityManager);
        when(entityManager.find(any(), anyInt())).thenReturn(p1);
        profileJPAService.updateProfile(p2);

        assertEquals(p1.toString(), p2.toString());
    }

    /**
     * Test the update profile method of ProfileJPAService for a ProfileNotFound exception
     */
    @Test(expected = ProfileNotFoundException.class)
    public void testFalseUpdateProfileForProfileNotFoundException() throws ProfileNotFoundException{

        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        profileJPAService.setEntityManager(entityManager);
        when(entityManager.find(any(), anyInt())).thenReturn(null);
        assertFalse(profileJPAService.updateProfile(p3));
    }

    /**
     * Test the update profile method of ProfileJPAService for a false update
     */
    @Test
    public void testUpdateProfileForFalseUpdate() throws ProfileNotFoundException {

        Profile p = mock(Profile.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        profileJPAService.setEntityManager(entityManager);
        when(entityManager.find(any(), anyInt())).thenReturn(p);
        assertFalse(profileJPAService.updateProfile(p2));
    }

    /**
     * Test the delete profile method
     */
    @Test
    public void testDeleteProfile() throws ProfileNotDeletedException {

        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        profileJPAService.setEntityManager(entityManager);
        when(entityManager.find(any(), anyInt())).thenReturn(p2);
        assertTrue(profileJPAService.deleteProfile(p2));

    }

    /**
     * Test the delete profile method for non existing profile
     */
    @Test(expected = ProfileNotDeletedException.class)
    public void testDeleteNonExistingProfile() throws ProfileNotDeletedException{

        Profile p4 = mock(Profile.class);

        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        profileJPAService.setEntityManager(entityManager);
        doThrow(new EntityNotFoundException()).when(entityManager).remove(any(Profile.class));
        assertEquals(-1,profileJPAService.deleteProfile(p4));
    }

    /**
     * Test for getting a valid profile
     */
    @Test
    public void testGetProfileForProfileJPA() throws ProfileNotFoundException{

        TypedQuery mockedQuery = mock(TypedQuery.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        profileJPAService.setEntityManager(entityManager);
        when(entityManager.createQuery(anyString(), any())).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenReturn(p3);

        assertEquals(p3, profileJPAService.getProfile(p3.getId()));
    }

    /**
     * Test for getting a message which doesn't exist
     */
    @Test(expected = ProfileNotFoundException.class)
    public void testGetMessageForMessageJPAForException() throws ProfileNotFoundException {

        TypedQuery mockedQuery = mock(TypedQuery.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        profileJPAService.setEntityManager(entityManager);
        when(entityManager.createQuery(anyString(), any())).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenThrow(new NoResultException ());
        profileJPAService.getProfile(p1.getId());
    }

}
