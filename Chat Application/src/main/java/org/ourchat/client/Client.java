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
            client = new Socket("127.0.0.1", 9999);
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(System.in));
        } catch (IOException e) {
            //TODO
        }
    }

    class InputHandler implements Runnable{

        @Override
        public void run() {
             try{
                 BufferedReader inReader = new BufferedReader
                         (new InputStreamReader(System.in));

                 while(!done){

                 }
             }catch (Exception e){
                 //TODO
             }
        }
    }
}