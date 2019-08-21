package server;

import client.gui.ChessGUI;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ChessGame implements Runnable {



    private ChessPlayer[] player_array;

    private String current_coord;

    private final AtomicBoolean foundspot = new AtomicBoolean();
    private final AtomicInteger rounds;


    public ChessGame(ChessPlayer[] player_array, int rounds){

        this.current_coord = "";
        this.player_array = player_array;
        this.rounds = new AtomicInteger(rounds);
        foundspot.set(false);
    }



    @Override
    public void run() {
        Thread[] playerThreads = new Thread[player_array.length];

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while(rounds.get() > 0) {
                    System.out.println("ROUNDS IS " + rounds);

                    synchronized (foundspot) {

                        //Set the next spot for the player to click
                        setCurrent_coord();
                        foundspot.set(false);
                        for (ChessPlayer p : player_array) {
                            p.send_new_coord(getCurrent_coord());
                        }
                        while(foundspot.get() == false){
                            try {
                                foundspot.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        rounds.decrementAndGet();

                    }
                }
                for(ChessPlayer p : player_array){
                    p.close();
                }
                for(Thread x: playerThreads){
                    x.interrupt();

                }

            }
        });


        t.start();

        for(int i = 0; i < playerThreads.length; i++){
            int x = i;
            playerThreads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                        while(t.isAlive()){
                            System.out.println(rounds.get() + " rounds.get");
                            String spot_clicked = player_array[x].get_clicked();
                            if(!spot_clicked.equals("")){
                                System.out.println(spot_clicked);
                                if(spot_clicked.equals(getCurrent_coord())){
                                    player_array[x].addScore();
                                    foundspot.set(true);
                                    player_array[x].send_score(player_array[x].getScore());
                                    nxt();
                                }else{
                                    player_array[x].subtractScore();
                                    player_array[x].send_score(player_array[x].getScore());
                                }
                            }



                        }

                }
            });
        }
        for(Thread pt : playerThreads){
            pt.start();
        }



    }



    public void nxt(){
        synchronized (foundspot){
            foundspot.notifyAll();
        }
    }


    public void setCurrent_coord(){
        this.current_coord = ChessGUI.coords[(int)(Math.random()*64)];

    }

    public String getCurrent_coord(){
        return current_coord;
    }

}
