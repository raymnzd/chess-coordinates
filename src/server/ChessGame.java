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

        setCurrent_coord();
        for(ChessPlayer p : player_array){
            p.send_new_coord(getCurrent_coord());
        }


    }

    public void setCurrent_coord(){
        this.current_coord = ChessGUI.coords[(int)Math.random()*64];

    }

    public String getCurrent_coord(){
        return current_coord;
    }


}
