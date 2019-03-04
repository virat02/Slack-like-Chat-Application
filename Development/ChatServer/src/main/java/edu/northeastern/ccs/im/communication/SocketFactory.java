package edu.northeastern.ccs.im.communication;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class SocketFactory {
    public SocketFactory()  {

    }
    public SocketChannel createSocket() throws IOException {
        return SocketChannel.open();
    }
}
