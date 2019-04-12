package edu.northeastern.ccs.im.customexceptions;

/**
 * Exception thrown when URL entered is Invalid
 */
public class InvalidImageURLException extends Exception {

    /**
     * Constructor for InvalidEmailException
     * @param message the message attached to the exception.
     */
    public InvalidImageURLException(String message)
    {
        super(message);
    }
}
