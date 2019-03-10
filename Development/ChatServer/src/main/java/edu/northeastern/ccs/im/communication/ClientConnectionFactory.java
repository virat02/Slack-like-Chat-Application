package edu.northeastern.ccs.im.communication;

public class ClientConnectionFactory {
    private SocketFactory socketFactory = new SocketFactory();

    public ClientConnectionFactory()    {

    }

    public ClientConnection createClientConnection(String hostName, int port)  {
        return new ClientConnectionImpl(hostName, port, socketFactory);
    }
}
