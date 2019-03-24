package edu.northeastern.ccs.im.communication;

import java.io.IOException;

public interface ClientConnection {
    /***
     * Make an initial connection to the server
     * @throws IOException
     */
    void connect() throws IOException;

    /***
     * Send the network request to the server
     * @param networkRequest
     * @throws IOException
     */
    void sendRequest(NetworkRequest networkRequest) throws IOException;

    /***
     *
     * Receive the network response from server. It will block.
     * @return
     * @throws IOException
     */
    NetworkResponse readResponse() throws IOException;

    /***
     * Close the network connection.
     * @throws IOException
     */
    void close() throws IOException;
}
