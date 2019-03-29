package edu.northeastern.ccs.im.customexceptions;

/**
 * Exception thrown when URL entered is Invalid
 */
public class InvalidImageURLException extends Exception {

    /**
     * Constructor for InvalidEmailException
     * @param message
     */
    public InvalidImageURLException(String message)
    {
        super(message);
    }
}
