package edu.northeastern.ccs.im.customexceptions;

/**
 * Exception thrown when an Invite is not found
 */
public class InviteNotFoundException extends Exception{

    /**
     * Exception when an Invite is not found.
     * @param error the message in the exception.
     */
    public InviteNotFoundException(String error) {
        super(error);
    }
}
