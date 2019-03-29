package edu.northeastern.ccs.im.customexceptions;

/**
 * Exception thrown when Username length is larger than 20
 */
public class UsernameTooLongException extends Exception {

    /**
     * Constructor for UsernameTooLongException
     * @param message
     */
    public UsernameTooLongException(String message)
    {
        super(message);
    }
}
