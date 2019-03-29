package edu.northeastern.ccs.im.communication;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.Message;
import edu.northeastern.ccs.im.readers.JsonBufferReader;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.List;

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
        ByteBuffer byteBuffer = ByteBuffer.allocate(64 * 2048);
        socketChannel.read(byteBuffer);
        return CommunicationUtils.getObjectMapper().readValue(byteBuffer.array(), NetworkResponseImpl.class);
    }

    @Override
    public void close() throws IOException {
        socketChannel.close();
    }
}
