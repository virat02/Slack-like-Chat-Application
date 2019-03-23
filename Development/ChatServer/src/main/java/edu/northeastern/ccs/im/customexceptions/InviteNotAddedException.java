package edu.northeastern.ccs.im.customexceptions;

public class InviteNotAddedException extends Exception {

    /**
     * Exception when an Invite is not Persisted.
     * @param error the message in the exception.
     */
    public InviteNotAddedException(String error) {
        super(error);
    }
}
