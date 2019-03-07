package edu.northeastern.ccs.im.communication;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ClientConnectionImpl implements ClientConnection {

    private SocketChannel socketChannel;
    private String hostName;
    private Integer port;
    private SocketFactory socketFactory;

    public ClientConnectionImpl(String hostName, Integer port, SocketFactory socketFactory) {
        this.hostName = hostName;
        this.port = port;
        this.socketFactory = socketFactory;
    }

    @Override
    public void connect() throws IOException {
        socketChannel = socketFactory.createSocket();
        socketChannel.connect(new InetSocketAddress(hostName, port));
    }

    @Override
    public void sendRequest(NetworkRequest networkRequest) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.wrap(CommunicationUtils.getObjectMapper().writeValueAsBytes(networkRequest));
        socketChannel.write(byteBuffer);
    }

    @Override
    public NetworkResponse readResponse() throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        socketChannel.read(byteBuffer);
        return CommunicationUtils.getObjectMapper().readValue(byteBuffer.array(), NetworkResponseImpl.class);
    }

    public static void main(String[] args) throws IOException {
        try (SocketChannel socketChannel = SocketChannel.open()) {
            socketChannel.connect(new InetSocketAddress("localhost", 4545));
            NetworkRequest networkRequest = new NetworkRequestFactory().createUserRequest("tarun", "tarungmailcom");
            ByteBuffer byteBuffer = ByteBuffer.wrap(new ObjectMapper().writeValueAsBytes(networkRequest));
            socketChannel.write(byteBuffer);
        }
    }

    @Override
    public void close() throws IOException {
        socketChannel.close();
    }
}
