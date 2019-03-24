package edu.northeastern.ccs.im.communication;

/***
 * A Client connection factory.
 * Call this factory to obtain instance of ClientConnection
 */
public class ClientConnectionFactory {
    private SocketFactory socketFactory = new SocketFactory();
    /***
     *
     * @param hostName The hostname for the server
     * @param port The port of the server
     * @return a ClientConnection
     */
    public ClientConnection createClientConnection(String hostName, int port)  {
        return new ClientConnectionImpl(hostName, port, socketFactory);
    }

    public ClientConnection createMessageClientConnection(String hostName, int port)    {
        return new ClientMessageConnection(hostName, port, socketFactory);
    }
}