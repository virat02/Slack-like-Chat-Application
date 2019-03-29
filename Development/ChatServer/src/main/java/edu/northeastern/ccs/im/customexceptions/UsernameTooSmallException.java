package edu.northeastern.ccs.im.customexceptions;

/**
 * Exception thrown when Username length is less than 4
 */
public class UsernameTooSmallException extends Exception {

    /**
     * Constructor for UsernameTooSmallException
     * @param message
     */
    public UsernameTooSmallException(String message)
    {
        super(message);
    }
}
