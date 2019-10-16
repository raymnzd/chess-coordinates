package client.gui;


/**
 * Class that keeps track of all the data
 *
 * @author Raymond Zhao
 *
 */

public class ChessData {

    //holds current coordinate
    private String coord;

    //holds player's score
    private int score;

    //sets new coordinate
    public void setCoord(String coord){
        this.coord = coord;
    }

    //gets current coordinate
    public String getCoord(){
        return coord;
    }

    //sets player's score
    public void setScore(int score){
        this.score = score;
    }

    //get player's score
    public int getScore(){
        return score;
    }


}


