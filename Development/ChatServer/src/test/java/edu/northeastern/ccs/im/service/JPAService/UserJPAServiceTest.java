package edu.northeastern.ccs.im.service.JPAService;


import edu.northeastern.ccs.im.userGroup.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

/**
 * The UserJPAService class will test the UserJPA methods.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserJPAServiceTest {

    private UserJPAService userJPAService;
    private User userOne;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    /**
     * Sets up the variables for the UserJPAService.
     */
    @Before
    public void setUp() {
        userOne = new User();
        entityManager = mock(EntityManager.class);
        userJPAService = new UserJPAService();
        entityTransaction = mock(EntityTransaction.class);
    }

    /**
     * Tests the create entity method.
     */
    @Test
    public void testCreateEntity() {
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        userJPAService.setEntityManager(entityManager);
        userJPAService.createUser(userOne);
    }

    /**
     * Tests the delete entity method.
     */
    @Test
    public void testDeleteEntity() {
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        userJPAService.setEntityManager(entityManager);
        userJPAService.deleteUser(userOne);
    }

    /**
     * Tests the update entity method.
     */
    @Test
    public void testUpdateEntity() {
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.find(any(), anyInt())).thenReturn(userOne);
        userJPAService.setEntityManager(entityManager);
        userJPAService.updateUser(userOne);
    }

    /**
     * Tests the update entity method.
     */
    @Test (expected = EntityNotFoundException.class)
    public void testUpdateEntityFail() {
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.find(any(), anyInt())).thenReturn(null);
        userJPAService.setEntityManager(entityManager);
        userJPAService.updateUser(userOne);
    }

    /**
     * Tests the search method in the JPA service.
     */
    @Test
    public void testSearch() {
        Query mockedQuery = mock(Query.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString())).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenReturn(userOne);
        userJPAService.setEntityManager(entityManager);
        User newUser = userJPAService.search("ThisName");
        assertEquals(newUser, userOne);
    }

    /**
     * Tests the getUser method in the JPA service.
     */
    @Test
    public void testGetUser() {
        Query mockedQuery = mock(Query.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString())).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenReturn(userOne);
        userJPAService.setEntityManager(entityManager);
        User newUser = userJPAService.getUser(99);
        assertEquals(newUser, userOne);
    }

    /**
     * Tests the login functionality for a user interacting with the Database.
     */
    @Test
    public void testLoginUser() {
        TypedQuery mockedQuery = mock(TypedQuery.class);
        userOne.setUsername("Jerry");
        userOne.setPassword("kk");
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString(), any())).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenReturn(userOne);
        userJPAService.setEntityManager(entityManager);
        User newUser = userJPAService.loginUser(userOne);
        assertEquals(userOne, newUser);
    }

    /**
     * Tests the login functionality for a user interacting with the Database fails.
     */
    @Test
    public void testLoginFail() {
        TypedQuery mockedQuery = mock(TypedQuery.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString(), any())).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenReturn(userOne);
        userJPAService.setEntityManager(entityManager);
        User newUser = userJPAService.loginUser(userOne);
        assertNull(newUser);
    }
}