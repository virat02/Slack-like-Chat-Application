package edu.northeastern.ccs.im.customexceptions;

/**
 * Exception thrown when a user tries to unfollow a user who they are not following
 */
public class UnfollowNotFollowingUserException extends Exception {
    /**
     * Constructor for UnfollowNotFollowingUserException
     * @param message
     */
    public UnfollowNotFollowingUserException(String message)
    {
        super(message);
    }
}
