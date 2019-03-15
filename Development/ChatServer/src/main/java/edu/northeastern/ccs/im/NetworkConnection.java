package edu.northeastern.ccs.im;

import edu.northeastern.ccs.im.communication.CommunicationUtils;
import edu.northeastern.ccs.im.readers.JsonBufferReader;
import edu.northeastern.ccs.im.readers.JsonBufferReaderImpl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This class is similar to the java.io.PrintWriter class, but this class's
 * methods work with our non-blocking Socket classes. This class could easily be
 * made to wait for network output (e.g., be made &quot;non-blocking&quot; in
 * technical parlance), but I have not worried about it yet.
 * <p>
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-sa/4.0/. It is based on work
 * originally written by Matthew Hertz and has been adapted for use in a class
 * assignment at Northeastern University.
 *
 * @version 1.4
 */
public class NetworkConnection implements Iterable<Message> {

    /**
     * The size of the incoming buffer.
     */
    private static final int BUFFER_SIZE = 64 * 1024;

    /**
     * Channel over which we will send and receive messages.
     */
    private final SocketChannel channel;

    /**
     * Selector for this client's connection.
     */
    private Selector selector;

    /**
     * Selection key for this client's connection.
     */
    private SelectionKey key;

    /**
     * Byte buffer to use for incoming messages to this client.
     */
    private ByteBuffer buff;

    /**
     * Queue of messages for this client.
     */
    private Queue<Message> messages;

    /**
     * Creates a new instance of this class. Since, by definition, this class sends
     * output over the network, we need to supply the non-blocking Socket instance
     * to which we will write.
     *
     * @param sockChan Non-blocking SocketChannel instance to which we will send all
     *                 communication.
     */
    public NetworkConnection(SocketChannel sockChan) {
        // Create the queue that will hold the messages received from over the network
        messages = new ConcurrentLinkedQueue<>();
        // Allocate the buffer we will use to read data
        buff = ByteBuffer.allocate(BUFFER_SIZE);
        // Remember the channel that we will be using.
        // Set up the SocketChannel over which we will communicate.
        channel = sockChan;
        try {
            channel.configureBlocking(false);
            // Open the selector to handle our non-blocking I/O
            selector = Selector.open();
            // Register our channel to receive alerts to complete the connection
            key = channel.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            // For the moment we are going to simply cover up that there was a problem.
            ChatLogger.error(e.toString());
            assert false;
        }
    }

    public void setSelector(Selector selector) {
        this.selector = selector;
    }

    /**
     * Send a Message over the network. This method performs its actions by printing
     * the given Message over the SocketNB instance with which the PrintNetNB was
     * instantiated. This returns whether our attempt to send the message was
     * successful.
     *
     * @param msg Message to be sent out over the network.
     * @return True if we successfully send this message; false otherwise.
     */
    public boolean sendMessage(Message msg) {
        return CommunicationUtils.writeToChannel(channel, msg);
    }

    /**
     * Close this client network connection.
     */
    public void close() {
        try {
            selector.close();
            channel.close();
        } catch (IOException e) {
            ChatLogger.error("Caught exception: " + e.toString());
            assert false;
        }
    }

    @Override
    public Iterator<Message> iterator() {
        return new MessageIterator();
    }

    /**
     * Private class that helps iterate over a Network Connection.
     *
     * @author Riya Nadkarni / Sibendu Dey
     * @version 03-11-2019
     */
    private class MessageIterator implements Iterator<Message> {

        /**
         * Default constructor.
         */
        public MessageIterator() {
            // nothing to do here
        }

        @Override
        public boolean hasNext() {
            boolean result = false;
            try {
                // If we have messages waiting for us, return true.
                if (!messages.isEmpty()) {
                    result = true;
                }
                // Otherwise, check if we can read in at least one new message
                else if (selector.selectNow() != 0) {
                    assert key.isReadable();
                    // Read in the next set of commands from the channel.
                    channel.read(buff);
                    selector.selectedKeys().remove(key);
                    buff.flip();
                    JsonBufferReader jsonBufferReader = new JsonBufferReaderImpl();
                    List<Message> incomingMessageList = jsonBufferReader.messageList(ByteBuffer.wrap(buff.array()));
                    long start = jsonBufferReader.bytesRead();
                    // Move any read messages out of the buffer so that we can add to the end.
                    // Assuming buffer will read all the data which is incorrect, need to fix this one
                    buff.position((int) start);
                    // Move all of the remaining data to the start of the buffer.
                    buff.compact();
                    // No messages has been parsed, return false indicating the non availability
                    // of a message.
                    if (incomingMessageList.isEmpty())
                        return false;

                    messages.addAll(incomingMessageList);
                    result = true;
                }
            } catch (IOException ioe) {
                // For the moment, we will cover up this exception and hope it never occurs.
                assert false;
            }
            // Do we now have any messages?
            return result;
        }

        @Override
        public Message next() {
            if (messages.isEmpty()) {
                throw new NoSuchElementException("No next line has been typed in at the keyboard");
            }
            Message msg = messages.remove();
            ChatLogger.info(msg.toString());
            return msg;
        }
    }
}
