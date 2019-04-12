package edu.northeastern.ccs.im.service;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.Message;
import edu.northeastern.ccs.im.NetworkConnection;
import edu.northeastern.ccs.im.customexceptions.GroupNotFoundException;
import edu.northeastern.ccs.im.customexceptions.MessageNotFoundException;
import edu.northeastern.ccs.im.customexceptions.UserNotFoundException;
import edu.northeastern.ccs.im.server.ClientRunnable;
import edu.northeastern.ccs.im.server.ServerConstants;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * MessageBroadCastService takes care of handling messages within a
 * group
 */
public class MessageBroadCastService implements BroadCastService {

    private static final Logger LOGGER = Logger.getLogger(MessageBroadCastService.class.getName());
    private static final String ERROR_MESSAGE_1 = "Group with group unique key: ";
    private static final String ERROR_MESSAGE_2 = " trying to be accessed does not exist!";
    private static final String ERROR_MESSAGE_3 = "Messages could not be retrieved for group having group unique key: ";
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

    private MessageService messageService;

    /**
     * Instantiates a new Message broad cast service.
     */
    public MessageBroadCastService() {
        messageService = MessageService.getInstance();
    }

    /**
     * Instantiates a new Message broad cast service.
     *
     * @param groupCode the group code
     */
    public MessageBroadCastService(String groupCode) {
        this();
        this.groupCode = groupCode;
    }

    /**
     * Adding a socket Connection.
     * @param socketChannel the socket channel
     * @throws IOException if there isn't a socet channel.
     */
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

    /**
     * Checks to see if the client is active.
     * @return boolean if the client is active or not.
     */
    @Override
    public boolean isClientActive() {
        return !active.isEmpty();
    }

    /**
     * Broadcasts a message.
     * @param message the message to be broadcasted.
     */
    @Override
    public void broadcastMessage(Message message) {
        try {
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
        } catch (UserNotFoundException e) {
            LOGGER.info("Could not find the user!");
            ChatLogger.info("Message will not be broadcast!");
        } catch (GroupNotFoundException e) {
            LOGGER.info("Could not find the group!");
            ChatLogger.info("Message won't be broadcasted!");
        }
    }

    /**
     * Removes a client from the thread.
     * @param dead the client runnable.
     */
    @Override
    public void removeClient(ClientRunnable dead) {
        if (!active.remove(dead)) {
            ChatLogger.info("Could not find a thread that I tried to remove!\n");
        }
    }

    /**
     * The list of recent messages.
     * @return list of messages.
     */
    @Override
    public List<Message> getRecentMessages() {
        List<Message> messages = new ArrayList<>();
        try {
            return messageService.getTop15Messages(groupCode)
                    .stream()
                    .filter(m -> !m.isDeleted())
                    .map(m -> Message.makeBroadcastMessage(m.getSender().getUsername(), m.getMessage(), m.getReceiver().getGroupCode()))
                    .collect(Collectors.toList());
        } catch (NoResultException e) {
            ChatLogger.warning(ERROR_MESSAGE_3 + groupCode);
        } catch (GroupNotFoundException e) {
            ChatLogger.warning(ERROR_MESSAGE_1 + groupCode + ERROR_MESSAGE_2);
        }

        return messages;
    }

    /**
     * Gets the unread messages.
     * @param userName of the user trying to get their unread messages.
     * @return list of messages.
     */
    @Override
    public List<Message> getUnreadMessages(String userName) {
        final String userNotFoundError = "User could not be found for the error";
        final String noResultFoundError = ERROR_MESSAGE_3 + groupCode;
        final String groupNotFoundError = ERROR_MESSAGE_1 + groupCode + ERROR_MESSAGE_2;
        List<Message> messages = new ArrayList<>();
        try {
            return messageService.getUnreadMessages(userName, groupCode)
                    .stream()
                    .map(m -> Message.makeBroadcastMessage(m.getSender().getUsername(), m.getMessage(), m.getReceiver().getGroupCode()))
                    .collect(Collectors.toList());
        } catch (NoResultException e) {
            ChatLogger.error(noResultFoundError);
        } catch (GroupNotFoundException e) {
            ChatLogger.error(groupNotFoundError);
        } catch (UserNotFoundException e) {
            ChatLogger.error(userNotFoundError);
        }

        return messages;
    }

    /**
     * Delete a messages.
     * @param groupId the group from which the message must be deleted
     * @param messageIndex the index of the message that must be deleted
     * @return boolean if the message was deleted or not.
     */
    @Override
    public boolean deleteMessage(String groupId, int messageIndex) {
        try {
            List<edu.northeastern.ccs.im.user_group.Message> messageList =
                    messageService.getAllMessages(groupCode).stream()
                            .filter(m -> !m.isDeleted()).collect(Collectors.toList());
            messageService.deleteMessage(messageList.get(messageList.size() - messageIndex - 1));
            return true;
        } catch (NoResultException e) {
            ChatLogger.warning(ERROR_MESSAGE_3 + groupCode);
        } catch (GroupNotFoundException | MessageNotFoundException e) {
            ChatLogger.warning(ERROR_MESSAGE_1+groupCode+ERROR_MESSAGE_2);
        }
        return false;
    }
}