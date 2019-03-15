package edu.northeastern.ccs.im.service;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.Message;
import edu.northeastern.ccs.im.NetworkConnection;
import edu.northeastern.ccs.im.server.ClientRunnable;
import edu.northeastern.ccs.im.server.ServerConstants;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * MessageBroadCastService takes care of handling messages within a
 * group
 */
public class MessageBroadCastService implements BroadCastService {
    private String groupCode;
    private ConcurrentLinkedQueue<ClientRunnable> active = new ConcurrentLinkedQueue<>();
    private ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(10);

    /**
     * Sets message service.
     *
     * @param messageService the message service
     */
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    private MessageService messageService = new MessageService();

    /**
     * Instantiates a new Message broad cast service.
     */
    public MessageBroadCastService() {

    }

    /**
     * Instantiates a new Message broad cast service.
     *
     * @param groupCode the group code
     */
    public MessageBroadCastService(String groupCode) {
        this.groupCode = groupCode;
    }

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
        if (message.isBroadcastMessage()
                && messageService.createMessage(message.getText(), message.getName(), message.groupCode())) {
            // Loop through all of our active threads
            for (ClientRunnable tt : active) {
                // Do not send the message if it's not ready to be send
                if (tt.isInitialized()) {
                    tt.enqueueMessage(message);
                }
            }
        }
    }

    @Override
    public void removeClient(ClientRunnable dead) {
        if (!active.remove(dead)) {
            ChatLogger.info("Could not find a thread that I tried to remove!\n");
        }
    }

    @Override
    public List<Message> getRecentMessages() {
        List<Message> messages = new ArrayList<>();
        try {
            return messageService.getTop15Messages(groupCode)
                    .stream()
                    .map(m -> Message.makeBroadcastMessage(m.getSender().getUsername(), m.getMessage(), m.getReceiver().getGroupCode()))
                    .collect(Collectors.toList());
        } catch (NullPointerException e) {
            ChatLogger.warning("Messages could not be retrieved for group having groupCode: " + groupCode);
        }

        return messages;
    }
}