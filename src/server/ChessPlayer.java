package server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

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



    public ChessPlayer(Socket sock, int num_players, int player_number){

        this.sock = sock;

        this.num_players = num_players;
        this.player_number = player_number;
        try {
            scanner = new Scanner(sock.getInputStream());
            printer = new PrintStream(sock.getOutputStream());
        }
        catch (IOException e) {

        }
    }

    void connect() {
        printer.println("Welcome there are " + num_players + " players and you are player " +  player_number);
    }
}
