package edu.northeastern.ccs.im.service.jpa_service;

import edu.northeastern.ccs.im.customexceptions.ListOfUsersNotFound;
import edu.northeastern.ccs.im.customexceptions.UserNotFoundException;
import edu.northeastern.ccs.im.customexceptions.UserNotPersistedException;
import edu.northeastern.ccs.im.service.EntityManagerUtil;
import edu.northeastern.ccs.im.user_group.Profile;
import edu.northeastern.ccs.im.user_group.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.EntityManager;
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
    private TypedQuery typedQuery;
    private EntityManagerUtil entityManagerUtil;

    /**
     * Sets up the variables for the UserJPAService.
     */
    @Before
    public void setUp() {
        userOne = new User();
        userOne.setUsername("Jalannin");
        userOne.setPassword("jjj");
        userOne.setId(123);
        Profile profileOne = new Profile();
        profileOne.setEmail("jaa@gmail.com");
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
        entityManagerUtil = mock(EntityManagerUtil.class);
        userJPAService = new UserJPAService();
        entityTransaction = mock(EntityTransaction.class);
        typedQuery = mock(TypedQuery.class);

    }

    /**
     * Tests the create entity method.
     */
    @Test
    public void testCreateEntity() throws UserNotPersistedException {
        when(entityManagerUtil.getEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        userJPAService.setEntityManagerUtil(entityManagerUtil);
        userJPAService.createUser(userOne);
        assertEquals(123, userOne.getId());
    }

    /**
     * Tests the delete entity method.
     */
    @Test
    public void testDeleteEntity() throws UserNotFoundException {
        when(entityManagerUtil.getEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(userOne);
        userJPAService.setEntityManagerUtil(entityManagerUtil);
        userJPAService.deleteUser(userOne);
        verify(typedQuery).getSingleResult();
    }

    /**
     * Tests the update entity method.
     */
    @Test
    public void testUpdateEntity() throws UserNotFoundException {
        when(entityManagerUtil.getEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.find(any(), anyInt())).thenReturn(userOne);
        userJPAService.setEntityManagerUtil(entityManagerUtil);
        userJPAService.updateUser(userOne);
    }

    /**
     * Tests the update entity method.
     */
    @Test (expected = UserNotFoundException.class)
    public void testUpdateEntityFail() throws UserNotFoundException {
        when(entityManagerUtil.getEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.find(any(), anyInt())).thenReturn(null);
        userJPAService.setEntityManagerUtil(entityManagerUtil);
        userJPAService.updateUser(userOne);
    }

    /**
     * Tests the search method in the JPA service.
     */
    @Test
    public void testSearch() throws UserNotFoundException {
        when(entityManagerUtil.getEntityManager()).thenReturn(entityManager);
        TypedQuery mockedQuery = mock(TypedQuery.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString(), any())).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenReturn(userOne);
        userJPAService.setEntityManagerUtil(entityManagerUtil);
        User newUser = userJPAService.search("ThisName");
        assertEquals(newUser, userOne);
    }

    /**
     * Tests the getUser method in the JPA service.
     */
    @Test
    public void testGetUser() throws UserNotFoundException {
        when(entityManagerUtil.getEntityManager()).thenReturn(entityManager);
        TypedQuery mockedQuery = mock(TypedQuery.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString(), any())).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenReturn(userOne);
        userJPAService.setEntityManagerUtil(entityManagerUtil);
        User newUser = userJPAService.getUser(99);
        assertEquals(newUser, userOne);
    }

    /**
     * Tests the login functionality for a user interacting with the Database.
     */
    @Test
    public void testLoginUser() throws UserNotFoundException {
        when(entityManagerUtil.getEntityManager()).thenReturn(entityManager);
        TypedQuery mockedQuery = mock(TypedQuery.class);
        userOne.setUsername("Jerry");
        userOne.setPassword("kk");
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString(), any())).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenReturn(userOne);
        userJPAService.setEntityManagerUtil(entityManagerUtil);
        User newUser = userJPAService.loginUser(userOne);
        assertEquals(userOne, newUser);
    }

    /**
     * Tests the login functionality for a user interacting with the Database fails.
     */
    @Test(expected = UserNotFoundException.class)
    public void testLoginFail() throws UserNotFoundException {
        when(entityManagerUtil.getEntityManager()).thenReturn(entityManager);
        TypedQuery mockedQuery = mock(TypedQuery.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString(), any())).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenReturn(new User());
        userJPAService.setEntityManagerUtil(entityManagerUtil);
        userJPAService.loginUser(userOne);
    }

    /**
     * Tests the login functionality for a user interacting with the Database fails.
     */
    @Test (expected = UserNotFoundException.class)
    public void testLoginFailTwo() throws UserNotFoundException {
        when(entityManagerUtil.getEntityManager()).thenReturn(entityManager);
        TypedQuery mockedQuery = mock(TypedQuery.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString(), any())).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenReturn(null);
        userJPAService.setEntityManagerUtil(entityManagerUtil);
        User newUser = userJPAService.loginUser(userOne);
        assertNull(newUser);
    }

    /**
     * A test to see if the user can get it's own followers.
     */
    @Test
    public void testGetFollowers() throws ListOfUsersNotFound {
        when(entityManagerUtil.getEntityManager()).thenReturn(entityManager);
        List<User> listOfUsers = new ArrayList<User>();
        listOfUsers.add(userTwo);
        listOfUsers.add(userThree);
        userOne.setFollowing(listOfUsers);
        TypedQuery mockedQuery = mock(TypedQuery.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString(), any())).thenReturn(mockedQuery);
        when(mockedQuery.getResultList()).thenReturn(listOfUsers);
        userJPAService.setEntityManagerUtil(entityManagerUtil);
        List<User> newUsers = userJPAService.getFollowers(userOne);
        assertEquals(2, newUsers.size());
    }

    /**
     * A test to see if the user can't get it's own followers.
     */
    @Test(expected = ListOfUsersNotFound.class)
    public void testGetFollowersZero() throws ListOfUsersNotFound {
        when(entityManagerUtil.getEntityManager()).thenReturn(entityManager);
        TypedQuery mockedQuery = mock(TypedQuery.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString(), any())).thenReturn(mockedQuery);
        userJPAService.setEntityManagerUtil(entityManagerUtil);
        userJPAService.getFollowers(userOne);
    }

    /**
     * A test to ensure we can set a list of following.
     */
    @Test
    public void testGetFollowee() throws UserNotFoundException, ListOfUsersNotFound {
        when(entityManagerUtil.getEntityManager()).thenReturn(entityManager);
        List<User> listOfUsers = new ArrayList<User>();
        listOfUsers.add(userTwo);
        listOfUsers.add(userThree);
        userOne.setFollowing(listOfUsers);
        TypedQuery mockedQuery = mock(TypedQuery.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString(), any())).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenReturn(userOne);
        when(mockedQuery.getResultList()).thenReturn(listOfUsers);
        userJPAService.setEntityManagerUtil(entityManagerUtil);
        List<User> newUsers = userJPAService.getFollowees(userOne);
        assertEquals(2, newUsers.size());
    }

    /**
     * A test to see if we can get a list of followees.
     */
    @Test
    public void testGetFolloweeZero() throws UserNotFoundException, ListOfUsersNotFound {
        when(entityManagerUtil.getEntityManager()).thenReturn(entityManager);
        TypedQuery mockedQuery = mock(TypedQuery.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString(), any())).thenReturn(mockedQuery);
        userJPAService.setEntityManagerUtil(entityManagerUtil);
        List<User> newUsers = userJPAService.getFollowees(userOne);
        assertEquals(0, newUsers.size());
    }

    /**
     * Tests to ensure a user not persisted exception can be thrown.
     * @throws UserNotPersistedException when a user is not persisted.
     */
    @Test (expected = UserNotPersistedException.class)
    public void testUserNotPersistedCreateUser() throws UserNotPersistedException {
        when(entityManagerUtil.getEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenThrow(UserNotPersistedException.class);
        userJPAService.setEntityManagerUtil(entityManagerUtil);
        userJPAService.createUser(userOne);
        assertEquals(123, userOne.getId());
    }

    /**
     * Tests to ensure a user not found exception can be thrown.
     * @throws UserNotFoundException when a user is not found in the DB.
     */
    @Test (expected = UserNotFoundException.class)
    public void testUserNotPersistedSearchUser() throws UserNotFoundException {
        String username = "username";
        when(entityManagerUtil.getEntityManager()).thenReturn(entityManager);
        TypedQuery mockedQuery = mock(TypedQuery.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString(), any())).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenThrow(IllegalArgumentException.class);
        userJPAService.setEntityManagerUtil(entityManagerUtil);
        userJPAService.search(username);
    }

    /**
     * Tests to ensure the user not found exception can be thrown
     * @throws UserNotFoundException when a user is not found.
     */
    @Test (expected = UserNotFoundException.class)
    public void testGetUserUserNotFound() throws UserNotFoundException {
        int userId = 123;
        when(entityManagerUtil.getEntityManager()).thenReturn(entityManager);
        TypedQuery mockedQuery = mock(TypedQuery.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString(), any())).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenThrow(IllegalArgumentException.class);
        userJPAService.setEntityManagerUtil(entityManagerUtil);
        userJPAService.getUser(userId);
    }

    /**
     * Tests to ensure the user not found exception can be thrown
     * @throws UserNotFoundException when a user is not found.
     */
    @Test
    public void testGetUserUsersNotFound() throws ListOfUsersNotFound, UserNotFoundException {
        when(entityManagerUtil.getEntityManager()).thenReturn(entityManager);
        List<User> listOfUsers = mock(List.class);
        listOfUsers.add(userTwo);
        listOfUsers.add(userThree);
        userOne.setFollowing(listOfUsers);
        TypedQuery mockedQuery = mock(TypedQuery.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString(), any())).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenReturn(userOne);
        when(mockedQuery.getResultList()).thenReturn(listOfUsers);
        userJPAService.setEntityManagerUtil(entityManagerUtil);
        List<User> newUsers = userJPAService.getFollowees(userOne);
        assertEquals(0, newUsers.size());
    }

}