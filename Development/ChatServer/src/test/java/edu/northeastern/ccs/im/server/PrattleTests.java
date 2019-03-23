package edu.northeastern.ccs.im.server;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.northeastern.ccs.im.service.MessageBroadCastService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.northeastern.ccs.im.Message;
import edu.northeastern.ccs.im.NetworkConnection;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * @author jerry
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class PrattleTests {

    private static final String ACTIVE = "active";
    private SocketChannel socketChannel;
    private NetworkConnection networkConnection;
    private ConcurrentLinkedQueue<ClientRunnable> clientRunnables;
    private ClientRunnable clientRunnable;
    private ClientRunnable clientRunnable2;

    /***
     * Mocks the ServerSocketChannel and its behaviors required by
     * NetworkConnection
     */
    @Mock
    private ServerSocketChannel serverSocketChannel;

    @Mock
    private ScheduledExecutorService scheduledExecutorService;
    /***
     * Mocks the socketChannel and its behaviors required by
     * NetworkConnection
     */
    @Mock
    private SocketChannel sC;
    /***
     * Mock the selector and its behaviors required by
     * Selector
     */
    @Mock
    private Selector selector;
    private NetworkConnection nC;

    /**
     * The setUp Method that will setup the socket channel, network channel, client runnable, and queue.
     */
    @Before
    public void setUp() throws IOException {
        when(serverSocketChannel.configureBlocking(anyBoolean())).thenReturn(serverSocketChannel);
        scheduledExecutorService.scheduleAtFixedRate(clientRunnable, ServerConstants.CLIENT_CHECK_DELAY,
                ServerConstants.CLIENT_CHECK_DELAY, TimeUnit.MILLISECONDS);
        socketChannel = SocketChannel.open();
        networkConnection = new NetworkConnection(socketChannel);
        clientRunnables = new ConcurrentLinkedQueue<>();
        clientRunnable = new ClientRunnable(networkConnection, new MessageBroadCastService());
        clientRunnable2 = new ClientRunnable(networkConnection, new MessageBroadCastService());
    }


    /**
     * Closes the Sockets and Channels after they have been opened.
     */
    @After
    public void closeSocketAndChannels() {
        try {
            networkConnection.close();
            socketChannel.close();
            serverSocketChannel.close();

        } catch (IOException e) {
            Logger logger = Logger.getGlobal();
            logger.log(Level.INFO, "Stack Trace Error: " + e.getStackTrace());
        }
    }

    /**
     * A test to see whether or not we can stop the server.
     */
    @Test
    public void testStop() {
        Prattle.stopServer();
    }
}