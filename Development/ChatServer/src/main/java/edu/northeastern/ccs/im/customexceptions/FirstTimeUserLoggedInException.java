package edu.northeastern.ccs.im.customexceptions;

/**
 * If it's a first time a user is logging in.
 */
public class FirstTimeUserLoggedInException extends Exception {
    /**
     * An exception if a user if logging in for the first time.
     * @param message the message attached to the exception.
     */
    public FirstTimeUserLoggedInException(String message)   {
        super(message);
    }
}
