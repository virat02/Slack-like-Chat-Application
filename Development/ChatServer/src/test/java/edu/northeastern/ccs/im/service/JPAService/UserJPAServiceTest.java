package edu.northeastern.ccs.im.service.JPAService;


import edu.northeastern.ccs.im.userGroup.Profile;
import edu.northeastern.ccs.im.userGroup.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

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
    private User userTwo;
    private User userThree;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    /**
     * Sets up the variables for the UserJPAService.
     */
    @Before
    public void setUp() {
        userOne = new User();
        userOne.setUsername("Jalannin");
        userOne.setPassword("jjj");
        Profile profileOne = new Profile();
        profileOne.setEmail("jaa@.com");
        profileOne.setImageUrl("hhh.com");
        userOne.setProfileAccess(true);
        userOne.setProfile(profileOne);
        userTwo = new User();
        userTwo.setUsername("Joker");
        userTwo.setPassword("bomb");
        Profile profileTwo = new Profile();
        profileTwo.setEmail("pencil@.com");
        profileTwo.setImageUrl("haha.com");
        userTwo.setProfileAccess(false);
        userTwo.setProfile(profileTwo);
        userThree = new User();
        userThree.setUsername("Batman");
        userThree.setPassword("save");
        Profile profileThree = new Profile();
        profileThree.setEmail("rescue@.com");
        profileThree.setImageUrl("bats.com");
        userThree.setProfileAccess(true);
        userThree.setProfile(profileThree);

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
        TypedQuery mockedQuery = mock(TypedQuery.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString(), any())).thenReturn(mockedQuery);
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
        TypedQuery mockedQuery = mock(TypedQuery.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString(), any())).thenReturn(mockedQuery);
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
        when(mockedQuery.getSingleResult()).thenReturn(new User());
        userJPAService.setEntityManager(entityManager);
        User newUser = userJPAService.loginUser(userOne);
        assertNull(newUser);
    }

    /**
     * Tests the login functionality for a user interacting with the Database fails.
     */
    @Test
    public void testLoginFailTwo() {
        TypedQuery mockedQuery = mock(TypedQuery.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString(), any())).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenReturn(null);
        userJPAService.setEntityManager(entityManager);
        User newUser = userJPAService.loginUser(userOne);
        assertNull(newUser);
    }

    /**
     * A test to see if the user can get it's own followers.
     */
    @Test
    public void testGetFollowers() {
        List<User> listOfUsers = new ArrayList<User>();
        listOfUsers.add(userTwo);
        listOfUsers.add(userThree);
        userOne.setFollowing(listOfUsers);
        TypedQuery mockedQuery = mock(TypedQuery.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString(), any())).thenReturn(mockedQuery);
        when(mockedQuery.getResultList()).thenReturn(listOfUsers);
        userJPAService.setEntityManager(entityManager);
        List<User> newUsers = userJPAService.getFollowers(userOne);
        assertEquals(1, newUsers.size());
    }

    /**
     * A test to see if the user can't get it's own followers.
     */
    @Test
    public void testGetFollowersZero() {
        TypedQuery mockedQuery = mock(TypedQuery.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString(), any())).thenReturn(mockedQuery);
        when(mockedQuery.getResultList()).thenReturn(null);
        userJPAService.setEntityManager(entityManager);
        List<User> newUsers = userJPAService.getFollowers(userOne);
        assertEquals(0, newUsers.size());
    }

    /**
     * A test to ensure we can set a list of following.
     */
    @Test
    public void testGetFollowee() {
        List<User> listOfUsers = new ArrayList<User>();
        listOfUsers.add(userTwo);
        listOfUsers.add(userThree);
        userOne.setFollowing(listOfUsers);
        TypedQuery mockedQuery = mock(TypedQuery.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString(), any())).thenReturn(mockedQuery);
        when(mockedQuery.getResultList()).thenReturn(listOfUsers);
        userJPAService.setEntityManager(entityManager);
        List<User> newUsers = userJPAService.getFollowees(userOne);
        assertEquals(1, newUsers.size());
    }

    /**
     * A test to see if we can get a list of followees.
     */
    @Test
    public void testGetFolloweeZero() {
        TypedQuery mockedQuery = mock(TypedQuery.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString(), any())).thenReturn(mockedQuery);
        when(mockedQuery.getResultList()).thenReturn(null);
        userJPAService.setEntityManager(entityManager);
        List<User> newUsers = userJPAService.getFollowees(userOne);
        assertEquals(0, newUsers.size());
    }

}