package server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

import static protocol.ChessProtocol.*;

public class ChessPlayer {

    private final int num_players;
    /**
     * The {@link Socket} used to communicate with the client.
     */
    private Socket sock;

    /**
     * The {@link Scanner} used to read responses from the client.
     */
    private Scanner scanner;

    /**
     * The {@link PrintStream} used to send requests to the client.
     */
    private PrintStream printer;

    private int player_number;

    private int score;


    public ChessPlayer(Socket sock, int num_players, int player_number){

        this.sock = sock;

        this.num_players = num_players;
        this.player_number = player_number;
        this.score = 0;
        try {
            scanner = new Scanner(sock.getInputStream());
            printer = new PrintStream(sock.getOutputStream());
        }
        catch (IOException e) {

        }
    }

    void connect() {
        printer.println(WELCOME + " there are " + num_players + " players and you are player " +  player_number);
    }


    void send_new_coord(String coord){
        printer.println(NEW_COORD + " " + coord);
    }


    void send_score(int score){
        printer.println(SCORE + " " + score);
    }



    public String get_clicked(){
        while(scanner.hasNextLine()){
            String response = scanner.nextLine();
            if(response.startsWith(CLICK)){
                return response.split(" ")[1];
            }
        }
        return "";
    }

    public void close() {
        try {
            sock.close();
        }
        catch(IOException ioe) {
            // squash
        }
    }

    public int getScore(){
        return score;
    }

    public void addScore(){
        score += 1;
    }

    public void subtractScore(){
        score -=1;
    }


}
