package edu.northeastern.ccs.im.server;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.communication.*;

import java.nio.channels.SocketChannel;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/***
 * A singleton class to handle the requests
 * coming from the client side.
 */
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

    /***
     * Obtain a single instance of RequestHandler
     * @return A Single instance of this request handler.
     */
    public static RequestHandler getInstance() {
        return requestHandler;
    }

    /***
     * Handle the request coming from a client and use RequestDispatcher to
     * take care of it.
     * @param socketChannel The socket channel which establishes the connection with client.
     */
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
