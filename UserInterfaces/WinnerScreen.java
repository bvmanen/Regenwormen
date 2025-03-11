package UserInterfaces;

import Game.Game;
import Players.Player;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Comparator;

public class WinnerScreen {
    
    private ArrayList<ArrayList<Object>> playerData = new ArrayList<>();

    private Boolean wantsToPlayAgain = false;


    /** GameRules Constructor
     */
    public WinnerScreen(Game game) {
        setPlayerData(game);
    }

    /** playerData Getter
     * @return playerData (ArrayList<ArrayList<Object>>)
     */
    public ArrayList<ArrayList<Object>> getPlayerData() {
        return this.playerData;
    }

    /** playerData Setter
     * @param game (Game)
     */
    public void setPlayerData(Game game) {
        for (Player player : game.getPlayers()) {
            ArrayList<Object> thisPlayerData = new ArrayList<>();
            thisPlayerData.add(player.getName());
            thisPlayerData.add(player.getScore());
            int maxTileScore = 0;
            while (!player.getTileStack().isEmpty()) {
                int tileScore = player.getTileStack().pop().getValue();
                if (tileScore > maxTileScore) {
                    maxTileScore = tileScore;
                }
            }
            thisPlayerData.add(maxTileScore);
            this.playerData.add(thisPlayerData);
        }
        
        // SORTING THE LIST BEFORE ASSIGNING PLACES
        int totalScoreIndex = 1;
        int highestTileIndex = 2;
        playerData.sort(new Comparator<ArrayList<Object>>() {
            @Override
            public int compare(ArrayList<Object> currentIndex, ArrayList<Object> nextIndex) {
                // First, compare based on the value at compareIndex1 (index 1)
                Integer value1 = (Integer) currentIndex.get(totalScoreIndex);
                Integer value2 = (Integer) nextIndex.get(totalScoreIndex);
                int firstComparison = value2.compareTo(value1); // Reverse the order for descending

                // If the first comparison is equal, compare based on the value at compareIndex2 (index 2)
                if (firstComparison == 0) {
                    value1 = (Integer) currentIndex.get(highestTileIndex);
                    value2 = (Integer) nextIndex.get(highestTileIndex);
                    return value2.compareTo(value1); // Reverse the order for descending
                }

                return firstComparison; // Reverse the order for descending
            }
        });


        int place = 1;
        for (ArrayList<Object> thisPlayerData : this.playerData) {
            thisPlayerData.add(place);
            place += 1;
        }
    }

    /** wantsToPlayAgain getter
     * @return
     */
    public Boolean wantsToPlayAgain() {
        return this.wantsToPlayAgain;
    }

    /** wantsToPlayAgain resetter
     */
    public void resetWantsToPlayAgain() {
        this.wantsToPlayAgain = !this.wantsToPlayAgain;
    }

    /** getWinnerScreen
     * @return scene (Scene)
     */
    public Scene getWinnerScreen(Stage primaryStage) {
        Label winner = new Label(playerData.get(0).get(0) + " wins!");
        winner.setAlignment(Pos.CENTER);
        winner.setPadding(new Insets(20));
        winner.getStyleClass().add("winner-1");
        BooleanProperty toggle = new SimpleBooleanProperty(true);
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(600), e -> {
            if (toggle.get()) {
                winner.getStyleClass().remove("winner-1");
                winner.getStyleClass().remove("winner-2");
                winner.getStyleClass().add("winner-2");
            } else {
                winner.getStyleClass().remove("winner-1");
                winner.getStyleClass().remove("winner-2");
                winner.getStyleClass().add("winner-1");
            }
            toggle.set(!toggle.get());
        }));
        timeline.setCycleCount(Timeline.INDEFINITE); // Repeat forever
        timeline.play();
        
        HBox places = new HBox(20);
        places.setAlignment(Pos.CENTER);
        // HBox legend = new VBox(20);
        VBox place = new VBox(20);
        place.getChildren().add(new Label("        Place        "));
        place.setAlignment(Pos.CENTER);
        VBox name = new VBox(20);
        name.getChildren().add(new Label("        Name        "));
        name.setAlignment(Pos.CENTER);
        VBox score = new VBox(20);
        score.getChildren().add(new Label("        Score        "));
        score.setAlignment(Pos.CENTER);
        VBox maxTileValue = new VBox(20);
        maxTileValue.getChildren().add(new Label("     Highest Tile     "));
        maxTileValue.setAlignment(Pos.CENTER);

        // legend.getChildren().addAll(new Label("Place"), new Label("Name"), new Label("Score"), new Label("Max. tile value"));

        for (int i = 0; i < this.playerData.size(); i++) {
            ArrayList<Object> player = this.playerData.get(i);
            place.getChildren().add(new Label(String.valueOf(player.get(3))));
            name.getChildren().add(new Label((String)(player.get(0))));
            score.getChildren().add(new Label(String.valueOf(player.get(1))));
            maxTileValue.getChildren().add(new Label(String.valueOf(player.get(2))));
        }

        VBox playAgain = new VBox(20);
        playAgain.setAlignment(Pos.CENTER);
        Label play = new Label("Would you like to play again?");
        play.setAlignment(Pos.CENTER);
        play.setPadding(new Insets(20));

        HBox yesOrNo = new HBox(20);
        yesOrNo.setAlignment(Pos.CENTER);
        Button yes = new Button("Yes");
        yes.setAlignment(Pos.CENTER);
        yes.setOnMouseClicked(e -> {
            resetWantsToPlayAgain();
        });
        Button no = new Button("No");
        no.setAlignment(Pos.CENTER);
        no.setOnMouseClicked(e -> {
            primaryStage.close();
        });
        yesOrNo.getChildren().addAll(yes, no);
        playAgain.getChildren().addAll(play, yesOrNo);

        places.getChildren().addAll(place, name, score, maxTileValue);

        VBox screen = new VBox(20);
        screen.setAlignment(Pos.CENTER);
        screen.getChildren().addAll(winner, places, playAgain);
        
        Scene scene = new Scene(screen, 1280, 640);                                 // Create a new Scene using that VBox, and set its .css style
        scene.getStylesheets().add("RegenWormen.css");
        return scene;
    }

    /** setScene
     * - Sets the scene onto the primaryStage
     * @param primaryStage (Stage)
     * @param scene (Scene)
     */
    public void setScene(Stage primaryStage, Scene scene) {
        Stage window;                                                               // Create a new stage, and set it to primaryStage
        window = primaryStage;
        window.setTitle("Regenwormen");                                             // Set the title
        window.setScene(scene);                                                     // Set the scene onto it
        window.show();
    }


    public static void swapPlayers(ArrayList<ArrayList<Object>> list, int index1, int index2) {
        ArrayList<Object> tempItem = list.get(index1);
        list.set(index1, list.get(index2));
        list.set(index2, tempItem);
    }

}
