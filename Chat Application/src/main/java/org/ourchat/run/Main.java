package org.ourchat.run;

import org.ourchat.server.Server;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }
}
