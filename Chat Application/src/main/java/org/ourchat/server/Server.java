package org.ourchat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{

    @Override
    public void run() {
        try {
            ServerSocket socket = new ServerSocket(9999);
            Socket client = socket.accept();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    class ConnectionHandler implements Runnable{
        private
        @Override
        public void run() {

        }
    }
}
