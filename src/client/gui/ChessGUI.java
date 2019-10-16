package client.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class ChessGUI extends Application {


    private GridPane gridPane; //to store the tiles
    private BorderPane borderPane; //to format the gui panes
    private Label pos; //coordinate to be clicked on
    private Label score; //score of the player

    /**
     * holds all the coordinates of a chess board
     */
    public static String[] coords = {"A8", "B8", "C8", "D8", "E8", "F8", "G8", "H8",
                                "A7", "B7", "C7", "D7", "E7", "F7", "G7", "H7",
                                "A6", "B6", "C6", "D6", "E6", "F6", "G6", "H6",
                                "A5", "B5", "C5", "D5", "E5", "F5", "G5", "H5",
                                "A4", "B4", "C4", "D4", "E4", "F4", "G4", "H4",
                                "A3", "B3", "C3", "D3", "E3", "F3", "G3", "H3",
                                "A2", "B2", "C2", "D2", "E2", "F2", "G2", "H2",
                                "A1", "B1", "C1", "D1", "E1", "F1", "G1", "H1"};


    private ChessClient client; //instance of the client
    private Button reverseButton; //button to reverse the board (flip)

    /**
     * initializes host and port information
     * creates an instance of the client
     */

    public void init(){
        // get the command line args
        List<String> args = getParameters().getRaw();

        // get host info and port from command line
        String host = args.get(0);
        int port = Integer.parseInt(args.get(1));
        client = new ChessClient(host,port, this);
    }


    /**
     * sets up how the gui looks like
     * layout of tiles are inside a gridpane
     * @param stage
     */
    @Override
    public void start(Stage stage){


        borderPane = new BorderPane();
        gridPane = new GridPane();
        pos = new Label();
        pos.setText("");

        pos.setTextAlignment(TextAlignment.CENTER);

        score = new Label();
        score.setText("Score: 0");
        score.setTextAlignment(TextAlignment.CENTER);

        reverseButton = new Button("Reverse the board");
        reverseButton.setOnAction(e->reverse());
        reverseButton.setMaxWidth(Double.MAX_VALUE);


        boolean whiteTile = true;

        int count = 0;

        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                int c = count;
                ImageView holeView = new ImageView(getTile(whiteTile));
                holeView.setOnMouseClicked(e ->{
                    System.out.println(coords[c]);
                                        this.client.send_click(coords[c]);
                });
                holeView.setFitHeight(50);
                holeView.setFitWidth(50);
                holeView.setPreserveRatio(true);
                gridPane.add(holeView, j,i);
                whiteTile = !whiteTile;
                count += 1;
            }
            whiteTile = !whiteTile;
        }

        stage.setScene(new Scene(borderPane));


        VBox vbox = new VBox();
        vbox.getChildren().addAll(score);
        vbox.getChildren().addAll(reverseButton);
        vbox.setAlignment(Pos.CENTER);

        borderPane.setTop(pos);
        borderPane.setCenter(gridPane);
        borderPane.setBottom(vbox);
        borderPane.setAlignment(pos, Pos.CENTER);
        borderPane.setAlignment(score, Pos.CENTER);

        stage.show();




        client.startListener();

    }

    /**
     * gets Tile Image (white or black)
     * @param b indicates which colored tile to get. (true == white, false == black)
     * @return appropriate tile image based on boolean b
     */

    public Image getTile(boolean b){
        if(b){
            Image tileImg = new Image(getClass().getResourceAsStream("white-tile.png"));
            return tileImg;
        }else{
            Image tileImg = new Image(getClass().getResourceAsStream("black-tile.png"));
            return tileImg;
        }
    }


    /**
     * updates the current coordinate to be clicked on
     * updates the score
     */

    private void refresh() {
        pos.setText(client.getData().getCoord());
        score.setText("Score: " + client.getData().getScore());
    }

    /**
     * calls for the gui to update
     */

    public void update() {
        if ( Platform.isFxApplicationThread() ) {
            this.refresh();
        }
        else {
            Platform.runLater( () -> this.refresh() );
        }
    }

    /**
     * reverse the coordinate tiles for the flipped board
     */
    public void reverse(){
        Collections.reverse(Arrays.asList(coords));
        int i = 0;
        for(Node node: gridPane.getChildren()){
            int j = i;
            node.setOnMouseClicked(e ->{
                System.out.println(coords[j]);
                this.client.send_click(coords[j]);
            });
            i+=1;
        }
    }

    /**
     * Launches the program if command line args of host and port is given
     * @param args
     */

    public static void main(String[] args){

        if (args.length != 2) {
            System.out.println("Usage: java ChessGUI host port");
            System.exit(-1);
        } else {
            Application.launch(args);
        }
    }
}
