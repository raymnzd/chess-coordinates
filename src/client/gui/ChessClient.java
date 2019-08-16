package client.gui;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static protocol.ChessProtocol.*;


public class ChessClient {

    private Socket clientSocket;
    private Scanner networkIn;
    private PrintStream networkOut;

    private ChessData data;

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
        this.data = new ChessData();
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
                        System.out.println("getting coord");
                        data.setCoord(splitArgs[1]);
                        gui.update();
                        break;
                    case SCORE:
                        System.out.println("getting score");
                        data.setScore(Integer.parseInt(splitArgs[1]));
                        gui.update();
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
        this.networkOut.println(CLICK + " " + coord);
        gui.update();
    }


    public ChessData getData(){
        return data;
    }


}
