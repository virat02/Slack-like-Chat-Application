package edu.northeastern.ccs.im.customexceptions;

/**
 * Exception thrown when an Invite is not deleted
 */
public class InviteNotDeletedException extends Exception {
    /**
     * Exception when an Invite is not deleted.
     * @param error the message in the exception.
     */
    public InviteNotDeletedException(String error) {
        super(error);
    }
}
