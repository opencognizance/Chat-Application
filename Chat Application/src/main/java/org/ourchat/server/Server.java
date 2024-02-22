package org.ourchat.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable{
    private ArrayList<ConnectionHandler> clientList;
    private ServerSocket socket;
    private ExecutorService threadPool;
    private static final String ADMIN_KEY = "4216";

    public Server(){
        clientList = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            socket = new ServerSocket(9999);
            threadPool = Executors.newCachedThreadPool();
            Socket client = socket.accept();
            ConnectionHandler handler = new ConnectionHandler(client);
            clientList.add(handler);
            threadPool.execute(handler);
        } catch (IOException e) {
            System.out.println("Unable to connnect");
        }
    }

    public void broadcast(String message){
        for(ConnectionHandler ch : clientList){
            if(ch!=null){
                ch.sendMessage(message);
            }
        }
    }

    public void closeConnection(){
         if(!socket.isClosed()){
             try {
                 socket.close();
                 for(ConnectionHandler ch : clientList){
                     ch.shutDown();
                 }
             } catch (IOException e) {
                 throw new RuntimeException(e);
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
                    broadcast(nickname+" joined the chat");
                }else{
                    out.println("Wrong Passcode entered!");
                    shutDown();
                }
                String message;
                while((message = in.readLine()) != null){
                    if(message.equalsIgnoreCase("Quit")){
                        broadcast(nickname+" : left the chat");
                        shutDown();
                    } else if (message.equalsIgnoreCase("Server Shutdown")) {
                        out.println("Enter Admin Key to proceed..");
                        String key = in.readLine();
                        if(key.equalsIgnoreCase(ADMIN_KEY)){
                            closeConnection();
                        }
                    } else{
                        broadcast(nickname+" : "+message);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void sendMessage(String message){
            out.println(message);
        }

        public void shutDown(){
            try {
                out.close();
                in.close();
                if(!client.isClosed()){
                    client.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
