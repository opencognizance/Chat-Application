package org.ourchat.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable{
    private ArrayList<ConnectionHandler> clientList;

    @Override
    public void run() {
        try {
            ServerSocket socket = new ServerSocket(9999);
            Socket client = socket.accept();
            ConnectionHandler handler = new ConnectionHandler(client);
            clientList.add(handler);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void broadcast(String message){
        for(ConnectionHandler ch : clientList){
            if(ch!=null){
                ch.sendMessage(message);
            }
        }
    }

    class ConnectionHandler implements Runnable{
        private Socket client;
        private BufferedReader in;
        private PrintWriter out;
        private String nickname;
        private static final String PASSCODE = "0343";

        public ConnectionHandler(Socket client){
            this.client = client;
        }

        @Override
        public void run() {
            try{
                out = new PrintWriter(client.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                out.println("Enter your nickname :: ");
                nickname = in.readLine();
                out.println("Enter passcode :: ");
                String passcode = in.readLine();
                if(passcode.equalsIgnoreCase(PASSCODE)){
                    out.println("Hello "+nickname+" welcome to chatbox");
                }else{
                    out.println("Wrong Passcode entered!");
                    client.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void sendMessage(String message){
            out.println(message);
        }
    }

}
