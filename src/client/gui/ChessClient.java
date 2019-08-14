package client.gui;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static common.ChessProtocol.*;


public class ChessClient {

    private Socket clientSocket;
    private Scanner networkIn;
    private PrintStream networkOut;

    private ChessGUI gui;


    public ChessClient(String host, int port, ChessGUI gui){
        try {
            this.clientSocket = new Socket(host, port);
            this.networkIn = new Scanner(clientSocket.getInputStream());
            this.networkOut = new PrintStream(clientSocket.getOutputStream());
            this.gui = gui;



            String arguments = this.networkIn.nextLine();
            System.out.println(arguments);



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void startListener() {
        new Thread(() -> this.run()).start();
    }

    private void run(){
        while(true){
            try {
                String request = this.networkIn.next();
                String arguments = this.networkIn.nextLine();
                String[] splitArgs = arguments.split(" ");
                switch ( request ) {
                    case NEW_COORD:

                }
            }
            catch( NoSuchElementException nse ) {
                // Looks like the connection shut down.

            }
            catch( Exception e ) {

            }


        }

    }

    public void send_click(String coord){
        this.networkOut.println(CLICK);
        gui.update();
    }




}
