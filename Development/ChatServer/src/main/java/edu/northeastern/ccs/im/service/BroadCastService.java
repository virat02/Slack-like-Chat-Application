package edu.northeastern.ccs.im.service;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public interface BroadCastService {
    void addConnection(SocketChannel socketChannel) throws IOException;
}
