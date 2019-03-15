package edu.northeastern.ccs.im.service.jpa_service;

import edu.northeastern.ccs.im.user_group.Profile;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

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
    public void testCreateProfileForProfileJPAService() {
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        profileJPAService.setEntityManager(entityManager);
        assertNotEquals(-1, profileJPAService.createProfile(p1));
    }

    /**
     * Test the create profile method for Profile JPA Service for an exception
     */
    @Test
    public void testCreateProfileForProfileJPAServiceForException() {

        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        profileJPAService.setEntityManager(entityManager);
        doThrow(new IllegalArgumentException()).when(entityManager).persist(any(Profile.class));
        assertEquals(-1, profileJPAService.createProfile(p1));
    }

    /**
     * Test the update profile method of ProfileJPAService
     */
    @Test
    public void testUpdateProfileOfProfileJPA() {

        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        profileJPAService.setEntityManager(entityManager);
        when(entityManager.find(any(), anyInt())).thenReturn(p1);
        profileJPAService.updateProfile(p2);

        assertEquals(p1.toString(), p2.toString());
    }

    /**
     * Test the update profile method of ProfileJPAService for a false update
     */
    @Test(expected = EntityNotFoundException.class)
    public void testFalseUpdateProfileOfProfileJPA() {

        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        profileJPAService.setEntityManager(entityManager);
        when(entityManager.find(any(), anyInt())).thenReturn(null);
        assertFalse(profileJPAService.updateProfile(p3));
    }

    /**
     * Test the update profile method for non-existing profile
     */
    @Test(expected = EntityNotFoundException.class)
    public void testUpdateNonExistingProfile() {

        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        profileJPAService.setEntityManager(entityManager);
        when(entityManager.find(any(), anyInt())).thenReturn(null);
        profileJPAService.updateProfile(p1);
    }

    /**
     * Test the delete profile method
     */
    @Test
    public void testDeleteProfile() {

        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        profileJPAService.setEntityManager(entityManager);

        assertNotEquals(-1, profileJPAService.deleteProfile(p2));

    }

    /**
     * Test the delete profile method for non existing profile
     */
    @Test
    public void testDeleteNonExistingMessage() {

        Profile p4 = mock(Profile.class);

        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        profileJPAService.setEntityManager(entityManager);
        doThrow(new EntityNotFoundException()).when(entityManager).remove(any(Profile.class));
        assertEquals(-1,profileJPAService.deleteProfile(p4));
    }

    /**
     * Test for getting a valid message
     */
    @Test
    public void testGetProfileForProfileJPA() {

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
    @Test(expected = NullPointerException.class)
    public void testGetMessageForMessageJPAForException() {

        mock(TypedQuery.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        profileJPAService.setEntityManager(entityManager);

        profileJPAService.getProfile(p1.getId());
    }

}
