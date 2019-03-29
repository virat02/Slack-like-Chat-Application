package edu.northeastern.ccs.im.customexceptions;

public class InviteNotUpdatedException extends Exception{
    /**
     * Exception when an Invite is not updated.
     * @param error the message in the exception.
     */
    public InviteNotUpdatedException(String error) {
        super(error);
    }
}
