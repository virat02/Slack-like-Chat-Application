package edu.northeastern.ccs.im.customexceptions;

/**
 * Exception for an invalid email
 */
public class InvalidEmailException extends Exception {

    /**
     * Constructor for InvalidEmailException
     * @param message the message attached to the exception.
     */
    public InvalidEmailException(String message)
    {
        super(message);
    }
}
