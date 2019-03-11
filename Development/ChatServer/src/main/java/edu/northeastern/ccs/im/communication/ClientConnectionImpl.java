package edu.northeastern.ccs.im.communication;

import edu.northeastern.ccs.im.Message;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;

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

    public static void main(String[] args) throws IOException, InterruptedException {
        try (SocketChannel socketChannel = SocketChannel.open()) {
            socketChannel.connect(new InetSocketAddress("localhost", 4545));

            NetworkRequest networkRequest = new NetworkRequestFactory().createJoinGroup();
            ByteBuffer byteBuffer = ByteBuffer.wrap(CommunicationUtils.getObjectMapper().writeValueAsBytes(networkRequest));
            socketChannel.write(byteBuffer);

            Thread t = new Thread(() -> {
                boolean isRunning = true;
                while (socketChannel.isConnected() && isRunning) {
                    ByteBuffer byteBuffer1 = ByteBuffer.allocate(1024);
                    try {
                        socketChannel.read(byteBuffer1);
                    } catch (IOException e) {
                        e.printStackTrace();
                        isRunning = false;
                    }
                }
            });

            t.start();
            byte[] message = CommunicationUtils.getObjectMapper().writeValueAsBytes(Message.makeBroadcastMessage("sibendu", "Helllooooo"));
            ByteBuffer byteBuffer1 = ByteBuffer.wrap(message);
            socketChannel.write(byteBuffer1);
            byteBuffer1 = ByteBuffer.wrap(message);
            socketChannel.write(byteBuffer1);
            socketChannel.write(ByteBuffer.wrap("\"{\"".getBytes()));
            t.join();
        }
    }

    @Override
    public void close() throws IOException {
        socketChannel.close();
    }
}
