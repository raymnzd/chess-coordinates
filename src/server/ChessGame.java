package server;

import client.gui.ChessGUI;

import java.util.concurrent.atomic.AtomicBoolean;

public class ChessGame implements Runnable {



    private ChessPlayer[] player_array;

    private String current_coord;

    private Boolean found;

    private final AtomicBoolean foundspot = new AtomicBoolean();

    public ChessGame(ChessPlayer[] player_array){

        this.current_coord = "";
        this.player_array = player_array;
        found = false;
        foundspot.set(false);
    }



    @Override
    public void run() {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {

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


                    }
                }

            }
        });


        t.start();


        Thread[] playerThreads = new Thread[player_array.length];
        for(int i = 0; i < playerThreads.length; i++){
            int x = i;
            playerThreads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                        while(true){
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

//        for(ChessPlayer p : player_array){
//            p.close();
//        }


    }



    public void nxt(){
        synchronized (foundspot){
            foundspot.notifyAll();
            System.out.println("notified");
        }
    }


    public void setCurrent_coord(){
        this.current_coord = ChessGUI.coords[(int)(Math.random()*64)];

    }

    public String getCurrent_coord(){
        return current_coord;
    }

}
