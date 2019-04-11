package edu.northeastern.ccs.im.service.jpa_service;

import edu.northeastern.ccs.im.customexceptions.ProfileNotFoundException;
import edu.northeastern.ccs.im.service.EntityManagerUtil;
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
import static org.mockito.Mockito.*;

/**
 * Tests for Profile JPA services
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class ProfileJPAServiceTest {

    @Mock
    private ProfileJPAService profileJPAService;

    @Mock
    private EntityTransaction entityTransaction;

    private AllJPAService allJPAService;

    @Mock
    private EntityManager entityManager;

    @Mock
    private EntityManagerUtil entityManagerUtil;

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
        entityManagerUtil = mock(EntityManagerUtil.class);
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
        allJPAService = new AllJPAService();
    }

    /**
     * Test the create profile method for Profile JPA Service
     */
    @Test
    public void testCreateProfileForProfileJPAService() {

        when(entityManagerUtil.getEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        allJPAService.setEntityManagerUtil(entityManagerUtil);
        assertTrue(allJPAService.createEntity(p1));
    }

    /**
     * Test the create profile method for Profile JPA Service for an exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateProfileForIllegalArgumentException() {

        when(entityManagerUtil.getEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        allJPAService.setEntityManagerUtil(entityManagerUtil);
        doThrow(new IllegalArgumentException()).when(entityManager).persist(any(Profile.class));
        allJPAService.createEntity(p1);
    }

    /**
     * Test the update profile method of ProfileJPAService
     */
    @Test
    public void testUpdateProfileOfProfileJPA() throws ProfileNotFoundException {

        when(entityManagerUtil.getEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        profileJPAService.setEntityManagerUtil(entityManagerUtil);
        when(entityManager.find(any(), anyInt())).thenReturn(p1);
        assertTrue(profileJPAService.updateProfile(p2));
    }

    /**
     * Test the update profile method of ProfileJPAService for a ProfileNotFound exception
     */
    @Test(expected = ProfileNotFoundException.class)
    public void testFalseUpdateProfileForProfileNotFoundException() throws ProfileNotFoundException{

        when(entityManagerUtil.getEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        profileJPAService.setEntityManagerUtil(entityManagerUtil);
        when(entityManager.find(any(), anyInt())).thenReturn(null);
        profileJPAService.updateProfile(p3);
    }

    /**
     * Test the update profile method of ProfileJPAService for a false update
     */
    @Test
    public void testUpdateProfileForFalseUpdate() throws ProfileNotFoundException {

        Profile p = mock(Profile.class);
        when(entityManagerUtil.getEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        profileJPAService.setEntityManagerUtil(entityManagerUtil);
        when(entityManager.find(any(), anyInt())).thenReturn(p);
        assertFalse(profileJPAService.updateProfile(p2));
    }

    /**
     * Test the delete profile method for successful deletion
     */
    @Test
    public void testDeleteProfile() {

        when(entityManagerUtil.getEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        allJPAService.setEntityManagerUtil(entityManagerUtil);
        assertTrue(allJPAService.deleteEntity(p1));

    }

    /**
     * Test the delete profile method for non existing profile
     */
    @Test(expected = EntityNotFoundException.class)
    public void testDeleteNonExistingProfile() {

        when(entityManagerUtil.getEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        allJPAService.setEntityManagerUtil(entityManagerUtil);
        doThrow(new EntityNotFoundException()).when(entityManager).remove(any(Profile.class));
        allJPAService.deleteEntity(p1);

    }

    /**
     * Test for getting a valid profile
     */
    @Test
    public void testGetProfileForProfileJPA() {

        TypedQuery mockedQuery = mock(TypedQuery.class);
        when(entityManagerUtil.getEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString(), any())).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenReturn(p3);
        allJPAService.setEntityManagerUtil(entityManagerUtil);

        assertEquals(p3, allJPAService.getEntity("Profile", p3.getId()));
    }

    /**
     * Test for getting a message which doesn't exist
     */
    @Test(expected = NoResultException.class)
    public void testGetMessageForNoResultException() {

        TypedQuery mockedQuery = mock(TypedQuery.class);
        when(entityManagerUtil.getEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString(), any())).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenThrow(new NoResultException());
        allJPAService.setEntityManagerUtil(entityManagerUtil);
        allJPAService.getEntity("Profile", p1.getId());
    }

    /**
     * Test ifEmailExists method for true
     */
    @Test
    public void testIfEmailExists() {
        TypedQuery mockedQuery = mock(TypedQuery.class);
        when(entityManagerUtil.getEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        profileJPAService.setEntityManagerUtil(entityManagerUtil);
        when(entityManager.createQuery(anyString(),any())).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenReturn(p1);
        assertTrue(profileJPAService.ifEmailExists(p1.getEmail()));
    }

    /**
     * Test ifEmailExists method for false
     */
    @Test
    public void testIfEmailExistsForFalse() {
        TypedQuery mockedQuery = mock(TypedQuery.class);
        when(entityManagerUtil.getEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        profileJPAService.setEntityManagerUtil(entityManagerUtil);
        when(entityManager.createQuery(anyString(),any())).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenThrow(new NoResultException());
        assertFalse(profileJPAService.ifEmailExists(p1.getEmail()));
    }

}
