package client.gui;


/**
 * Class that keeps track of all the data
 *
 * @author Raymond Zhao
 *
 */

public class ChessData {

    private String coord;

    private int score;



    public void setCoord(String coord){
        this.coord = coord;
    }


    public String getCoord(){
        return coord;
    }


    public void setScore(int score){
        this.score = score;
    }

    public int getScore(){
        return score;
    }


}


