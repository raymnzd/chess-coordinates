package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChessServer {


    private ServerSocket server;
    private static int player_count;
    private ChessPlayer[] player_array;



    private ChessServer(int port){
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {

        }
        player_array = new ChessPlayer[player_count];
    }


    public void run() {
        try {
            for(int i = 0; i < player_count; i++) {
                System.out.println("Waiting for player " + i + "...");
                Socket playerSocket = server.accept();
                ChessPlayer player =
                        new ChessPlayer(playerSocket,player_count, i);
                player.connect();
                System.out.println("Player " + i + " connected!");
                player_array[i] = player;

            }
            System.out.println("Starting game!");

            ChessGame game = new ChessGame(player_array);
            new Thread(game).run();


        } catch (IOException e) {
            System.err.println("Something has gone horribly wrong!");
            e.printStackTrace();
        }
    }




    public static void main(String[] args){

        if (args.length != 2) {
            System.out.println("Usage: java ChessServer <port> <playercount>");
            System.exit(5);
        }

        int port = Integer.parseInt(args[0]);
        player_count = Integer.parseInt(args[1]);
        ChessServer server = new ChessServer(port);
        server.run();
    }



}
