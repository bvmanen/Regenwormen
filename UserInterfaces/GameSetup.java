package UserInterfaces;

import Game.Game;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import java.util.ArrayList;
import javafx.util.Duration;

public class GameSetup {

    private ArrayList<Button> gameSizeOptions = new ArrayList<>();

    private int gameSize;

    private boolean choiceIsNotMade = true;

    private boolean nameIsUnknown = true;

    
    /** GameSetup Constructor
     */
    public GameSetup() {
        setGameSizeOptions();
    }

    /** gameSize Getter
     * @return gameSize (int)
     */
    public int getGameSize() {
        return this.gameSize;
    }

    /** choiceIsNotMade Getter
     * @return choiceIsNotMade (boolean)
     */
    public boolean choiceIsNotMade() {
        return this.choiceIsNotMade;
    }

    /** choiceIsNotMade Reverser
     */
    public void reverseChoiceIsNotMade() {
        this.choiceIsNotMade = !this.choiceIsNotMade;
    }

    /** nameIsUnkown Getter
     * @return
     */
    public boolean nameIsUnknown() {
        return this.nameIsUnknown();
    }
    
    /** nameIsUnknown Reverser
     */
    public void reverseNameIsUnknown() {
        this.nameIsUnknown = !this.nameIsUnknown;
    }

    /** gameTiles Setter
     * @param game (Game)
     */
    public void setGameSizeOptions() {
        for (int i = 0; i < 8; i++) {                                              // For each tile in the input game...
            int gameSize = i + 2;
            Button button = new Button(String.valueOf(gameSize));                                           // Create a button (with desired sizing and alignment) with that tile's id and value on it
            button.setPrefSize(35, 35);
            button.setAlignment(Pos.CENTER);
            button.setOnMouseClicked(e -> {
                this.gameSize = (gameSize);
                reverseChoiceIsNotMade();
            });
            this.gameSizeOptions.add(button);                                             // Add the button to this.gameTiles
        }
    }
    
    public Scene getFirstScene(Stage primaryStage) {
        VBox screen = new VBox(20);
        screen.setAlignment(Pos.CENTER);

        Label title = new Label("REGENWORMEN");
        title.setAlignment(Pos.CENTER);
        title.setPadding(new Insets(20));
        title.getStyleClass().add("title-1");
        BooleanProperty toggle = new SimpleBooleanProperty(true);
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(600), e -> {
            if (toggle.get()) {
                title.getStyleClass().remove("title-1");
                title.getStyleClass().remove("title-2");
                title.getStyleClass().add("title-2");
            } else {
                title.getStyleClass().remove("title-1");
                title.getStyleClass().remove("title-2");
                title.getStyleClass().add("title-1");
            }
            toggle.set(!toggle.get());
        }));
        timeline.setCycleCount(Timeline.INDEFINITE); // Repeat forever
        timeline.play();
        screen.getChildren().add(title);

        Label label = new Label("Let's play!\n\nWith how many people would you like to play?'\n");
        label.setAlignment(Pos.CENTER);
        label.setPadding(new Insets(0, 0, 25, 0));
        screen.getChildren().add(label);

        HBox rowOne = new HBox(20);                                             // Create 2 HBoxes, one each for holding 2 halves of the dice
        rowOne.setAlignment(Pos.CENTER);
        HBox rowTwo = new HBox(20);
        rowTwo.setAlignment(Pos.CENTER);
        for (int i = 0; i < 8; i++) {                                               // Distribute the dice buttons over the two HBoxes
            if (i < 4) {
                rowOne.getChildren().add(gameSizeOptions.get(i));
            } else {
                rowTwo.getChildren().add(gameSizeOptions.get(i));
            }
        }

        HBox rules = new HBox(20);
        rules.setAlignment(Pos.CENTER);
        rules.setPadding(new Insets(25));
        Button rulesButton = new Button("Rules");
        rulesButton.setOnMouseClicked(e -> {
            Popup rulesPopup = rulesWindow();
            
            double stageWidth = primaryStage.getWidth();
            double stageHeight = primaryStage.getHeight();
            double stageX = primaryStage.getX();
            double stageY = primaryStage.getY();

            double xPosition = stageX + (stageWidth / 2) - 273;
            double yPosition = stageY + (stageHeight / 2) - 140;

            rulesPopup.show(primaryStage, xPosition,  yPosition);
        });
        rules.getChildren().add(rulesButton);

        screen.getChildren().addAll(rowOne, rowTwo, rules);

        Scene scene = new Scene(screen, 1280, 640);                                 // Create a new Scene using that VBox, and set its .css style
        scene.getStylesheets().add("RegenWormen.css");
        return scene;
    }

    public Scene getSecondScene (Game game, int i) {
        VBox screen = new VBox(20);
        screen.setAlignment(Pos.CENTER);

        Label label = new Label("Player " + (i+ 1) + "\nWhat is your name?");
        label.setAlignment(Pos.CENTER);

        TextField nameField = new TextField();
        nameField.setAlignment(Pos.CENTER);
        nameField.setMaxWidth(300);

        Button button = new Button("Enter");
        button.setAlignment(Pos.CENTER);
        button.setOnAction(e -> {
            if (nameField.getText() == null || nameField.getText().isEmpty()) {
                PauseTransition pause = new PauseTransition(Duration.millis(50));           // Set a new pause of 50 ms
                button.getStyleClass().add("button-click");
                pause.play();                                                               // Play the pause
                pause.setOnFinished(ev -> {                                                 // When the pause finishes...
                    button.getStyleClass().remove("button-click");                                   // replace the button's style back to the previous one
                });
            } else {
                game.getPlayers().get(i).setName(nameField.getText());
                game.getPlayers().get(i).reverseNameIsUnknown();
            }
        });

        screen.getChildren().addAll(label, nameField, button);

        Scene scene = new Scene(screen, 1280, 640);                                 // Create a new Scene using that VBox, and set its .css style
        scene.getStylesheets().add("RegenWormen.css");

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                button.fire();  // Simulate a button click
            }
        });

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



    public Popup rulesWindow() {
        Popup popup = new Popup();
        
        TextArea popupText = new TextArea("RULEBOOK\r\n\n" + //
            "\r\n" + //
            "Regenwormen is a fun dice game where players try to collect tiles with different combinations of dice rolls. The goal is to be the first to collect the most valuable tiles while avoiding bad rolls!\r\n" + //
            "\r\n\n" + //
            "Setup\r\n\n" + //
            "The game is played with 8 dice and a set of numbered tiles, ranging from 21 to 36. Each tile corresponds to a combination of dice that can be rolled to claim it.\r\n" + //
            "\r\n\n" + //
            "Gameplay\r\n\n" + //
            "On your turn, you roll all 8 dice and then decide which dice to keep and whether you want to re-roll the rest. After each roll, you must take at least one die. If you manage to roll a combination that matches a tile (e.g., a pair of 4s, a 5, and a 6), you can claim that tile. As you claim more tiles, you stack them up (newest tile on top).\r\n" + //
            "\r\n\n" + //
            "Rolling Rules\r\n\n" + //
            "With each roll, you pick one value you want to take, and if multiple dice of that value have been rolled, you must take each of them. In consequent rolls you are not permitted to take dice with the value(s) of those you have already taken. Note that the worms on the dice have a value of 5, and you may grab 5s if you already have worms and vice versa. You can stop at any time, but if you re-roll and don’t get a valid combination, you lose the turn: If you don’t have any tiles the turn is simply passed on to the next player, otherwise the tile at the top of your stack is placed back on the board. If this tile has the highest value of those left on the board, it is left open. Otherwise the tile with the highest value left on the board is taken out of the game.\r\n" + //
            "\r\n\n" + //
            "Tiles\r\n\n" + //
            "Each tile has a value on it between 1 and 4. Tiles with higher values logically require higher dice combinations to claim. Players can collect tiles over several turns, but once a tile is claimed, it’s no longer available. The tiles left open on the table are available to anyone, if the correct combination is rolled. Additionally, if you have rolled the combination of a tile that is no longer available, but the tile matching your combination minus 1 is, you may take this tile. Finally, when rolling the exact combination of the tile on top of another player’s stack, you may claim that tile.\r\n" + //
            "\r\n\n" + //
            "Winning\r\n\n" + //
            "The game ends when all the tiles have been claimed and/or taken out of the game. The player with the most valuable collection of tiles at the end of the game wins. In case of a tie, the player with the most valuable tile in their collection wins.\r\n" + //
            "\r\n" + //
            "");

            popupText.setWrapText(true);
        
        HBox options = new HBox(20);
        options.setAlignment(Pos.CENTER);

        Button yes = new Button("I Understand");
        yes.setAlignment(Pos.CENTER);
        yes.setOnAction(e -> {
            popup.hide();
        });

        options.getChildren().addAll(yes);

        VBox popupView = new VBox(20);
        popupView.setPadding(new Insets(20));
        popupView.setAlignment(Pos.CENTER);
        popupView.getChildren().addAll(popupText, options);

        popup.getContent().add(popupView);

        return popup;
    }
}


