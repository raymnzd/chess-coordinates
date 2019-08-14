package server;

import client.gui.ChessGUI;

public class ChessGame implements Runnable {



    private ChessPlayer[] player_array;

    private String current_coord;


    public ChessGame(ChessPlayer[] player_array){

        this.current_coord = "";
        this.player_array = player_array;
    }



    @Override
    public void run() {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    setCurrent_coord();
                    for(ChessPlayer p : player_array){
                        p.send_new_coord(getCurrent_coord());
                    }
                    try{
                        Thread.sleep(5000);
                    }catch (InterruptedException e){

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
                        String spot_clicked = player_array[x].test_whack();
                        if(!spot_clicked.equals("")){
                            System.out.println(spot_clicked);
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

    public void setCurrent_coord(){
        this.current_coord = ChessGUI.coords[(int)(Math.random()*64)];

    }

    public String getCurrent_coord(){
        return current_coord;
    }


}
