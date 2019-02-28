package edu.northeastern.ccs.im.communication;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ClientConnectionImpl implements ClientConnection {

    private final NetworkRequest networkRequest;
    private SocketChannel socketChannel;

    public ClientConnectionImpl(NetworkRequest networkRequest) {
        this.networkRequest = networkRequest;
    }

    @Override
    public void connect() throws IOException {
        socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 4545));
        NetworkRequest networkRequest = new NetworkRequestFactory().createUserRequest("tarun", "tarungmailcom");
        ByteBuffer byteBuffer = ByteBuffer.wrap(new ObjectMapper().writeValueAsBytes(networkRequest));
        socketChannel.write(byteBuffer);
    }

    public static void main(String args[]) throws IOException {
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
