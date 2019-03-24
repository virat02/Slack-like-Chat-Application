package edu.northeastern.ccs.im.communication;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.Message;
import edu.northeastern.ccs.im.readers.JsonBufferReader;
import edu.northeastern.ccs.im.readers.JsonBufferReaderImpl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Collections;
import java.util.List;

public class ClientMessageConnection implements MessageClientConnection {
  private final String hostName;
  private final int port;
  private final SocketFactory socketFactory;
  private SocketChannel socketChannel;
  private static final int BUFFER_SIZE = 64 * 1024;
  private ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

  public ClientMessageConnection(String hostName, int port, SocketFactory socketFactory) {
    this.hostName = hostName;
    this.port = port;
    this.socketFactory = socketFactory;
  }

  @Override
  public void connect() throws IOException {
    socketChannel = socketFactory.createSocket();
    socketChannel.connect(new InetSocketAddress(hostName, port));
  }

  @Override
  public void sendRequest(NetworkRequest networkRequest) throws IOException {
    if(!CommunicationUtils.writeToChannel(socketChannel, networkRequest))
      throw new IOException("Communication could not be successful");
  }

  @Override
  public NetworkResponse readResponse() throws IOException {
    ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
    socketChannel.read(byteBuffer);
    return CommunicationUtils.getObjectMapper().readValue(byteBuffer.array(), NetworkResponseImpl.class);
  }

  @Override
  public void close() throws IOException {
    socketChannel.close();
  }

  @Override
  public List<Message> readMessages() {
    List<Message> messages;
    try {
      socketChannel.read(buffer);
      buffer.flip();
      JsonBufferReader jsonBufferReader = new JsonBufferReaderImpl();
      messages = jsonBufferReader.messageList(ByteBuffer.wrap(buffer.array()));
      long start = jsonBufferReader.bytesRead();
      // Move any read messages out of the buffer so that we can add to the end.
      buffer.position((int) start);
      // Move all of the remaining data to the start of the buffer.
      buffer.compact();
    } catch (IOException e) {
      ChatLogger.error(e.getMessage());
      return Collections.emptyList();
    }

    return messages;
  }

  @Override
  public void sendMessage(Message message) {
    CommunicationUtils.writeToChannel(socketChannel, message);
  }
}