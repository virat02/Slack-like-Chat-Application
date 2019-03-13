package edu.northeastern.ccs.im.service;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.Message;
import edu.northeastern.ccs.im.NetworkConnection;
import edu.northeastern.ccs.im.server.ClientRunnable;
import edu.northeastern.ccs.im.server.ServerConstants;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.*;

public class MessageBroadCastService implements BroadCastService {
    private static ConcurrentLinkedQueue<ClientRunnable> active = new ConcurrentLinkedQueue<>();
    private ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(10);
    private MessageService messageService = new MessageService();

    @Override
    public void addConnection(SocketChannel socketChannel) throws IOException {
        socketChannel.configureBlocking(false);
        ClientRunnable clientRunnable = new ClientRunnable(new NetworkConnection(socketChannel), this);
        active.add(clientRunnable);
        // Have the client executed by our pool of threads.
        ScheduledFuture<?> clientFuture = threadPool.scheduleAtFixedRate(clientRunnable, ServerConstants.CLIENT_CHECK_DELAY,
                ServerConstants.CLIENT_CHECK_DELAY, TimeUnit.MILLISECONDS);
        clientRunnable.setFuture(clientFuture);
    }

    @Override
    public boolean isClientActive() {
        return !active.isEmpty();
    }

    @Override
    public void broadcastMessage(Message message) {
        // TODO Handle exception, if message is not created, don't broadcast
//        if (message.isBroadcastMessage())   {
//            messageService.createMessage(message.getText(), message.getName(), message.groupCode());
//        }
        // Loop through all of our active threads
        for (ClientRunnable tt : active) {
            // Do not send the message if it's not ready to be send
            if (tt.isInitialized()) {
                tt.enqueueMessage(message);
            }
        }
    }

    @Override
    public void removeClient(ClientRunnable dead)  {
        if (!active.remove(dead)) {
            ChatLogger.info("Could not find a thread that I tried to remove!\n");
        }
    }
}
