package edu.northeastern.ccs.im;

import edu.northeastern.ccs.im.server.ClientRunnable;
import edu.northeastern.ccs.im.server.ClientTimer;
import edu.northeastern.ccs.im.server.Prattle;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

import java.util.*;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Junit test suite for ClientRunnable class.
 *
 * @author virat02
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class ClientRunnableTest {

    /**
     * Mocks ClientRunnable
     */
    @InjectMocks
    private ClientRunnable clientRunnable;

    /**
     * Mocks NetworkConnection
     */
    @Mock
    private NetworkConnection networkConnection;

    /**
     * Mocks ClientTimer
     */
    @Mock
    private ClientTimer clientTimer;

    /**
     * Mocks Message
     */
    @Mock
    private Message msg;

    private final Logger logger = Logger.getLogger(ClientRunnableTest.class.getName());

    /**
     * Common helper method for various test methods
     * @param msgItr
     */
    private void runClientRunnable(Iterator<Message> msgItr) {
        when(networkConnection.iterator()).thenReturn(msgItr);
        clientRunnable = new ClientRunnable(networkConnection);
        clientRunnable.run();
    }

    /**
     * Test for Run method
     */
    @Test
    public void testRunMethod() {
        Iterator<Message> msgItr = new Iterator<Message>() {
            Message msg1 = Message.makeBroadcastMessage("virat02", "Hi, I'm Virat!");
            Message msg2 = Message.makeBroadcastMessage("sibendudey", "Hi, I'm Sibendu!");
            Message msg3 = Message.makeBroadcastMessage("sangeetha", "Hi, I'm Sangeetha!");
            List<Message> msgList = new ArrayList<>(Arrays.asList(msg1, msg2, msg3));

            int position = 0;

            @Override
            public boolean hasNext() {
                return position < msgList.size();
            }

            @Override
            public Message next() {
                if (hasNext()) {
                    return msgList.get(position++);
                } else {
                    throw new NoSuchElementException();
                }
            }
        };

        runClientRunnable(msgItr);
        //when(clientTimer.isBehind()).thenReturn(true);
        networkConnection.close();
    }

    /**
     * Test for case when isInitialized is True
     */
    @Test
    public void testIsInitiliazeMethodWhenTrue() {
        Iterator<Message> msgItr = new Iterator<Message>() {
            Message msg1 = Message.makeBroadcastMessage("jerry", "Hi, I'm Jerry!");
            Message msg2 = Message.makeBroadcastMessage("tarun", "Hi, I'm Tarun!");
            List<Message> msgList = new ArrayList<>(Arrays.asList(msg1, msg2));

            int position = 0;

            @Override
            public boolean hasNext() {
                return position < msgList.size();
            }

            @Override
            public Message next() {
                if (hasNext()) {
                    return msgList.get(position++);
                } else {
                    throw new NoSuchElementException();
                }
            }
        };

        runClientRunnable(msgItr);
        assertTrue(clientRunnable.isInitialized());
        Prattle.broadcastMessage(Message.makeBroadcastMessage("jerry", "Hi, I'm Jerry!"));
        networkConnection.close();
    }

    /**
     * Test for case when isInitialized is False
     */
    @Test
    public void testIsInitializedWhenFalse() {
        Iterator<Message> msgItr = new Iterator<Message>() {
            List<Message> msgList = new ArrayList<>();

            int position = 0;

            @Override
            public boolean hasNext() {
                return position < msgList.size();
            }

            @Override
            public Message next() {
                if (hasNext()) {
                    return msgList.get(position++);
                } else {
                    throw new NoSuchElementException();
                }
            }
        };

        runClientRunnable(msgItr);
        assertFalse(clientRunnable.isInitialized());
        networkConnection.close();
    }

    /**
     * Test for getUserId method
     */
    @Test
    public void testGetUserIdForNullUser() {
        Iterator<Message> msgItr = new Iterator<Message>() {
            Message msg1 = Message.makeBroadcastMessage(null, "Hi, I'm Jerry!");
            List<Message> msgList = new ArrayList<>(Arrays.asList(msg1));

            int position = 0;

            @Override
            public boolean hasNext() {
                return position < msgList.size();
            }

            @Override
            public Message next() {
                if (hasNext()) {
                    return msgList.get(position++);
                } else {
                    throw new NoSuchElementException();
                }
            }
        };

        runClientRunnable(msgItr);
        assertEquals(-1, clientRunnable.getUserId());
    }

    /**
     * Test for setFuture method
     */
    @Test
    public void testSetFutureMethod() {
        Iterator<Message> msgItr = new Iterator<Message>() {
            Message msg1 =  Message.makeSimpleLoginMessage("sangeetha");
            Message msg2 = Message.makeBroadcastMessage("tarun", "Hi, I'm Tarun!");
            Message msg3 = Message.makeQuitMessage("virat02");
            List<Message> msgList = new ArrayList<>(Arrays.asList(msg1, msg2, msg3));

            int position = 0;

            @Override
            public boolean hasNext() {
                return position < msgList.size();
            }

            @Override
            public Message next() {
                if (hasNext()) {
                    return msgList.get(position++);
                } else {
                    throw new NoSuchElementException();
                }
            }
        };

        runClientRunnable(msgItr);
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
        Runnable task = () -> logger.log(Level.INFO, "Inside testTerminateClientMethod");
        ScheduledFuture<?> sf = ses.schedule(task, 10, TimeUnit.SECONDS);
        clientRunnable.setFuture(sf);
        clientRunnable.run();
        networkConnection.close();
    }

    /**
     * Test for enqueueMessage method
     */
    @Test
    public void testEnqueueMessage() {
        Iterator<Message> msgItr = new Iterator<Message>() {
            Message msg1 =  Message.makeSimpleLoginMessage("virat");
            Message msg2 = Message.makeBroadcastMessage("jerry", "Hi, I'm Jerry!");
            Message msg3 = Message.makeQuitMessage("virat02");
            List<Message> msgList = new ArrayList<>(Arrays.asList(msg1, msg2, msg3));

            int position = 0;

            @Override
            public boolean hasNext() {
                return position < msgList.size();
            }

            @Override
            public Message next() {
                if (hasNext()) {
                    return msgList.get(position++);
                } else {
                    throw new NoSuchElementException();
                }
            }
        };

        when(networkConnection.iterator()).thenReturn(msgItr);
        when(networkConnection.sendMessage(any())).thenReturn(true);
        clientRunnable = new ClientRunnable(networkConnection);
        clientRunnable.run();
    }

    /**
     * Test for terminateClient method
     */
    @Test
    public void testTerminateClientMethod() {

        Iterator<Message> msgItr = new Iterator<Message>() {
            Message msg1 =  Message.makeSimpleLoginMessage("tarun");
            Message msg2 = Message.makeBroadcastMessage("sangeetha", "Hi, I'm Sangeetha!");
            Message msg3 = Message.makeQuitMessage("sibendudey");
            List<Message> msgList = new ArrayList<>(Arrays.asList(msg1, msg2, msg3));

            int position = 0;

            @Override
            public boolean hasNext() {
                return position < msgList.size();
            }

            @Override
            public Message next() {
                if (hasNext()) {
                    return msgList.get(position++);
                } else {
                    throw new NoSuchElementException();
                }
            }
        };

        runClientRunnable(msgItr);
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
        Runnable task2 = () -> logger.log(Level.INFO, "In testTerminateClient");
        ScheduledFuture<?> sf = ses.schedule(task2, 8, TimeUnit.SECONDS);
        clientRunnable.setFuture(sf);
        clientRunnable.terminateClient();
        networkConnection.close();
    }

    /**
     * Test for all quit type messages
     */
    @Test
    public void testAllQuitMessages() {
        Iterator<Message> msgItr = new Iterator<Message>() {
            Message msg1 =  Message.makeQuitMessage("tarun");
            Message msg2 = Message.makeQuitMessage("sangeetha");
            Message msg3 = Message.makeQuitMessage("sibendudey");
            List<Message> msgList = new ArrayList<>(Arrays.asList(msg1, msg2, msg3));

            int position = 0;

            @Override
            public boolean hasNext() {
                return position < msgList.size();
            }

            @Override
            public Message next() {
                if (hasNext()) {
                    return msgList.get(position++);
                } else {
                    throw new NoSuchElementException();
                }
            }
        };

        when(networkConnection.iterator()).thenReturn(msgItr);
        when(networkConnection.sendMessage(any())).thenReturn(false);
        clientRunnable = new ClientRunnable(networkConnection);
        ScheduledFuture<?> cf = Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(clientRunnable, 200,
                        200, TimeUnit.MILLISECONDS);
        clientRunnable.setFuture(cf);
        clientRunnable.run();
        clientRunnable.terminateClient();
    }

    /**
     * Test for all broadcast type messages
     */
    @Test
    public void testAllBroadcastMessages() {
        Iterator<Message> msgItr = new Iterator<Message>() {
            Message msg1 =  Message.makeBroadcastMessage("tarun", "Hi, I'm Tarun!");
            Message msg2 = Message.makeBroadcastMessage("sangeetha", "Hi. I'm Sangeetha!");
            Message msg3 = Message.makeBroadcastMessage("sibendudey", "Hi, I'm Sibendu!");
            List<Message> msgList = new ArrayList<>(Arrays.asList(msg1, msg2, msg3));

            int position = 0;

            @Override
            public boolean hasNext() {
                return position < msgList.size();
            }

            @Override
            public Message next() {
                if (hasNext()) {
                    return msgList.get(position++);
                } else {
                    throw new NoSuchElementException();
                }
            }
        };

        when(networkConnection.iterator()).thenReturn(msgItr);
        when(networkConnection.sendMessage(any())).thenReturn(false);
        clientRunnable = new ClientRunnable(networkConnection);
        ScheduledFuture<?> cf = Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(clientRunnable, 200,
                        200, TimeUnit.MILLISECONDS);
        clientRunnable.setFuture(cf);
        clientRunnable.run();
        clientRunnable.terminateClient();
    }

    /**
     * Test for terminate user when timer is behind
     */
    @Test
    public void testTerminateUserWhenTimerBehind() {
        Iterator<Message> msgItr = new Iterator<Message>() {
            Message msg1 = Message.makeBroadcastMessage("virat02", "Hi, I'm Virat!");
            Message msg2 = Message.makeSimpleLoginMessage("sibendudey");
            Message msg3 = Message.makeQuitMessage("sangeetha");
            List<Message> msgList = new ArrayList<>(Arrays.asList(msg1, msg2, msg3));

            int position = 0;

            @Override
            public boolean hasNext() {
                return position < msgList.size();
            }

            @Override
            public Message next() {
                if (hasNext()) {
                    return msgList.get(position++);
                } else {
                    throw new NoSuchElementException();
                }
            }
        };

        when(networkConnection.iterator()).thenReturn(msgItr);
        clientRunnable = new ClientRunnable(networkConnection);
        clientRunnable.setClientTimer(clientTimer);
        when(clientTimer.isBehind()).thenReturn(true);
        ScheduledFuture<?> cf = Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(clientRunnable, 200,
                        200, TimeUnit.MILLISECONDS);
        clientRunnable.setFuture(cf);
        clientRunnable.run();
        networkConnection.close();
    }
}