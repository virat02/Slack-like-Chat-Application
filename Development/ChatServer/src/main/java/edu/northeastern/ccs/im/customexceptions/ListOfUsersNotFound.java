package edu.northeastern.ccs.im.customexceptions;

/**
 * Exception thrown when List of users are not found
 */
public class ListOfUsersNotFound extends Exception {

    /**
     * Constructor for ListOfUsersNotFound
     * @param message
     */
    public ListOfUsersNotFound(String message)
    {
        super(message);
    }
}
