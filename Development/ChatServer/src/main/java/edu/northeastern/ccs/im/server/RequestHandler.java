package edu.northeastern.ccs.im.server;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.communication.*;

import java.nio.channels.SocketChannel;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class RequestHandler {
    private ThreadPoolExecutor executor;

    private RequestHandler() {
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
    }

    private static RequestHandler requestHandler;
    private RequestDispatcher requestDispatcher = RequestDispatcher.getInstance();

    static {
        requestHandler = new RequestHandler();
    }

    public static RequestHandler getInstance() {
        return requestHandler;
    }

    void handleRequest(SocketChannel socketChannel) {
        executor.execute(() -> {
            ChatLogger.info("Request made");
            NetworkRequest networkRequest = CommunicationUtils.networkRequestReadFromSocket(socketChannel);
            if (networkRequest != null) {
                NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, socketChannel);
                CommunicationUtils.writeToChannel(socketChannel, networkResponse);
            }
        });
    }
}
