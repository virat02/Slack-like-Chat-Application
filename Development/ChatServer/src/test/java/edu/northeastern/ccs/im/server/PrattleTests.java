package edu.northeastern.ccs.im.server;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.persistence.sessions.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
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
    public void setUp() throws IOException{
        when(serverSocketChannel.configureBlocking(anyBoolean())).thenReturn(serverSocketChannel);
        scheduledExecutorService.scheduleAtFixedRate(clientRunnable, ServerConstants.CLIENT_CHECK_DELAY,
                ServerConstants.CLIENT_CHECK_DELAY, TimeUnit.MILLISECONDS);
        socketChannel = SocketChannel.open();
        networkConnection = new NetworkConnection(socketChannel);
        clientRunnables = new ConcurrentLinkedQueue<>();
        clientRunnable = new ClientRunnable(networkConnection);
        clientRunnable2 = new ClientRunnable(networkConnection);
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

    @Test
    public void testCreateThread() throws IOException{
        when(serverSocketChannel.accept()).thenReturn(socketChannel);
        Prattle.createClientThread(serverSocketChannel, scheduledExecutorService);
    }


    /**
     * A test to see whether or not we can stop the server.
     */
    @Test
    public void testStop() {
        Prattle.stopServer();
    }

    /**
     *
     *
     * @throws IllegalAccessException if we can't access the field
     * @throws NoSuchFieldException If there are no such fields
     */
    @Test
    public void testBroadcast() throws IllegalAccessException,
            NoSuchFieldException {
        clientRunnables.add(clientRunnable);
        Field thisField = Prattle.class.getDeclaredField(ACTIVE);
        thisField.setAccessible(true);

        Prattle.broadcastMessage(Message.makeBroadcastMessage("Jerry",
                "Hello World"));

    }

    /**
     *
     *
     * @throws IllegalAccessException if we can't access the field
     * @throws NoSuchFieldException If there are no such fields
     */
    @Test
    public void testBroadcastFail() throws IllegalAccessException,
            NoSuchFieldException {
        clientRunnables.add(clientRunnable);
        Field thisField = Prattle.class.getDeclaredField(ACTIVE);
        thisField.setAccessible(true);
        thisField.set(null, clientRunnables);

        Prattle.broadcastMessage(Message.makeBroadcastMessage("Jerry",
                ""));

    }



    /**
     * What happens when we give it someone who doesn't exist? it should throw an error
     *
     * @throws IllegalAccessException access to field is denied
     * @throws NoSuchFieldException no such field exists
     */
    @Test
    public void testFakeClient() throws IllegalAccessException, NoSuchFieldException {
        clientRunnables.add(clientRunnable);
        Field thisField = Prattle.class.getDeclaredField(ACTIVE);
        thisField.setAccessible(true);
        thisField.set(null, clientRunnables);

        Object returned = thisField.get(null);
        Prattle.removeClient(clientRunnable);
        assertEquals("[]", returned.toString());

        Prattle.removeClient(clientRunnable);
        Prattle.removeClient(clientRunnable2);
        Prattle.removeClient(clientRunnable2);
        Prattle.stopServer();
    }

    /**
     * If there are no clients in the clientRunnables queue, then the string should be empty.
     *
     * @throws IllegalAccessException the illegal access exception
     * @throws NoSuchFieldException the no such field exception
     */
    @Test
    public void testRemove() throws IllegalAccessException, NoSuchFieldException {
        clientRunnables.add(clientRunnable);
        Field thisField = Prattle.class.getDeclaredField(ACTIVE);
        thisField.setAccessible(true);
        thisField.set(null, clientRunnables);
        Object returned = thisField.get(null);
        Prattle.removeClient(clientRunnable);
        assertEquals("[]", returned.toString());
    }





}