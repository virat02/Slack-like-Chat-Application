package edu.northeastern.ccs.im.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.northeastern.ccs.im.communication.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class RequestHandler {
    private static final int BUFFER_SIZE = 2048;
    private ThreadPoolExecutor executor;

    private RequestHandler() {
        executor = (ThreadPoolExecutor) Executors.newScheduledThreadPool(10);
    }

    private static RequestHandler requestHandler;
    private RequestDispatcher requestDispatcher = RequestDispatcher.getInstance();

    static {
        requestHandler = new RequestHandler();
    }

    public static RequestHandler getInstance() {
        return requestHandler;
    }

    public void handleRequest(SocketChannel socketChannel) {
        executor.execute(() -> {
            NetworkRequest networkRequest = parseNetworkRequest(socketChannel);
            if (networkRequest != null) {
                NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest);
                byte[] networkResponseByteEncoded = getNetworkResponseAsBytes(networkResponse);
                try {
                    socketChannel.write(ByteBuffer.wrap(networkResponseByteEncoded));
                    socketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private byte[] getNetworkResponseAsBytes(NetworkResponse networkResponse) {
        try {
            return CommunicationUtils.getObjectMapper().writeValueAsBytes(networkResponse);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new byte[]{};
        }
    }

    private NetworkRequest parseNetworkRequest(SocketChannel socketChannel) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        try {
            socketChannel.read(byteBuffer);
            return CommunicationUtils.getObjectMapper().readValue(byteBuffer.array(), NetworkRequestImpl.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
