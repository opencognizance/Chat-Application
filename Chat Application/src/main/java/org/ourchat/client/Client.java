package org.ourchat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable{
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private Boolean done;

    @Override
    public void run() {
        try {
            Socket client = new Socket("127.0.0.1", 9999);
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(System.in));
            InputHandler inputHandler = new InputHandler();
            Thread t = new Thread(inputHandler);
            t.start();

            String inMessage;
            while((inMessage = in.readLine())!=null){
                System.out.println(inMessage);
            }
        } catch (IOException e) {
            shutDown();
        }
    }

    public void shutDown(){
         done = true;
         try{
             in.close();
             out.close();
             if(!client.isClosed()){
                 client.close();
             }
         }catch (Exception e){
             //Ignore
         }
    }

    class InputHandler implements Runnable{

        @Override
        public void run() {
             try{
                 BufferedReader inReader = new BufferedReader
                         (new InputStreamReader(System.in));

                 while(!done){
                     String message = inReader.readLine();
                     if(message.equalsIgnoreCase("Quit")){
                         in.close();
                         shutDown();
                     }else{
                         out.println(message);
                     }
                 }
             }catch (Exception e){
                 shutDown();
             }
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }
}
