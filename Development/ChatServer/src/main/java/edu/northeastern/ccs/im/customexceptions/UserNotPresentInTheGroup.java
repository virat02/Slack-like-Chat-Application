package edu.northeastern.ccs.im.customexceptions;

/**
 * Exception thrown when User is not present in a group
 */
public class UserNotPresentInTheGroup extends Exception {

    /**
     * Constructor for UserNotPresentInTheGroup
     * @param message the message attached to the exception.
     */
    public UserNotPresentInTheGroup(String message)
    {
        super(message);
    }
}
