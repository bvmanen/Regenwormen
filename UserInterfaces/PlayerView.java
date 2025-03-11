package UserInterfaces;

import Game.Game;
import GamePieces.GamePiece;
import Players.Player;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import java.util.ArrayList;
import javafx.util.Duration;

public class PlayerView {

    private String playersTurn = new String();
    
    private ArrayList<Button> gameTiles = new ArrayList<>();

    private ArrayList<Label> playerNames = new ArrayList<>();

    private ArrayList<Button> playerTiles = new ArrayList<>();

    private ArrayList<Button> rolledDice = new ArrayList<>();

    private ArrayList<Button> takenDice = new ArrayList<>();

    private ArrayList<Button> gamePlayButtons = new ArrayList<>();

    private boolean canRollDice = true;

    private boolean canTakeDice = false;

    private boolean canTakeTile = true;


    /** PlayerInterface Constructor
     * @param game
     * @param player
     */
    public PlayerView(Game game, Player player, Stage primaryStage) {
        setPlayersTurn(player);
        setGameTiles(game, player);
        setPlayerNames(game);
        setPlayerTiles(game, player);
        setRolledDice(player);
        setTakenDice(player);
        setGamePlayButtons(game, player, primaryStage);
    }

    /** playersTurn Setter
     * @param player (Player)
     */
    public void setPlayersTurn(Player player) {
        this.playersTurn = player.getName() + "'s turn";
    }

    /** playersTurn getter
     * @return playersTurn (String)
     */
    public String getPlayersTurn() {
        return this.playersTurn;
    }

    /** gameTiles Setter
     * @param game (Game)
     */
    public void setGameTiles(Game game, Player player) {
        for (int i = 0; i < 16; i++) {                                              // For each tile in the input game...
            Button button = new Button();                                           // Create a button (with desired sizing and alignment) with that tile's id and value on it
            button.setAlignment(Pos.CENTER);
            button.setPrefSize(50, 80);
            this.gameTiles.add(button);                                             // Add the button to this.gameTiles
        }
        resetGameTiles(game, player);
    }

    /** gameTiles Resetter
     * - resets the game tile visuals based on the tiles in the game
     * @param game (Game)
     * @param player (Player)
     */
    public void resetGameTiles(Game game, Player player) {
        for (int i = 0; i < 16; i++) {
            GamePiece tile = game.getTiles()[i];
            Button button = this.gameTiles.get(i);
            if (!tile.getIsAvailable()) {                                           // If the tile isn't available...
                button.getStyleClass().add("button-taken");                       // Alter formatting
                button.setOnMouseClicked(e -> buttonReaction(button, "button-unavailable-taken"));
            } else {                                                                // Otherwise...
                button.setText(tile.getId() + "\n-\n" + tile.getValue());           // Format the button depending on the tile's id and value
                button.setOnMouseClicked(e -> {                                     // When clicked...
                    buttonReaction(button, "button-click");                   // Set the unavailable click reaction
                    if (player.getAvailableTiles().contains(tile.getId())              // If tha player's available tiles contains their diceTotal...
                    && canTakeTile) {                    // ... and their takenDice contains a worm, and they canTakeTile...
                        player.takeGameTile(game, tile.getId());                    // player takes the tile
                        resetGameBoard(game, player);
                        reverseCanRollDice();
                        player.reverseTurnIsNotFinished();
                    }
                });
            }
        }
    }

    /** gameTiles Getter
     * @return gameTiles (ArrayList<Button>)
     */
    public ArrayList<Button> getGameTiles() {
        return this.gameTiles;
    }

    /** playerNames Setter
     * @param game (Game)
     */
    public void setPlayerNames(Game game) {
        for (int i = 0; i < game.getPlayers().size(); i++) {
            Label label = new Label();
            label.setPrefWidth(150);
            playerNames.add(label);
        }
        resetPlayerNames(game);
    }

    /** playerNames Resetter
     * - resets the playerName label visuals to include their tileStack size.
     * @param game (Game)
     */
    public void resetPlayerNames(Game game) {
        for (int i = 0; i < game.getPlayers().size(); i++) {
            Player player = game.getPlayers().get(i);
            Label label = this.playerNames.get(i);
            label.setText(player.getName() + "\n(" + player.getTileStack().size() + ")");
        }
    }

    /** playerNames Getter
     * @return
     */
    public ArrayList<Label> getPlayerNames() {
        return this.playerNames;
    }

    /** playerTiles Setter
     * @param game (Game)
     */
    public void setPlayerTiles(Game game, Player player) {
        for (int i = 0; i < game.getPlayers().size(); i++) {                        // For each player in the input game...
            Button button = new Button();                                           // Create a button and set desired sizing and alignment
            button.setAlignment(Pos.CENTER);
            button.setPrefSize(50, 80);
            this.playerTiles.add(button);                                           // Add the button to this.playerTiles
        }
        resetPlayerTiles(game, player);
    }

    /** playerTiles Resetter
     * - resets the player tile visuals based on the player's tileStacks
     * @param game (Game)
     */
    public void resetPlayerTiles(Game game, Player player) {
        for (int i = 0; i < game.getPlayers().size(); i++) {                        // For each player in the game
            Player gamePlayer = game.getPlayers().get(i);                           // set the player based on index
            Button button = this.getPlayerTiles().get(i);                           // set the button based on index
            if (gamePlayer.getTileStack().isEmpty()) {                              // If the player's tileStack is empty, reformat the button accordingly
                button.getStyleClass().add("button-taken");
                button.setOnMouseClicked(e -> buttonReaction(button, "button-unavailable-taken"));  // Alter formatting
            } else {                                                                // Otherwise set the button with the id and value of the top tile of the player's tileStack
                GamePiece tile = gamePlayer.getTileStack().peek();                  // set variables for readability
                button.setText(tile.getId() + "\n-\n" + tile.getValue());           // set the tile's text
                button.getStyleClass().remove(button.getStyleClass().size() - 1);
                button.getStyleClass().add("button");
                button.setOnMouseClicked(e -> {                                     // When clicked...
                    buttonReaction(button, "button-click");                   // Add button reaction
                    System.out.println("canTakeTile: " + canTakeTile);
                    System.out.println("AvailableTiles: " + player.getAvailableTiles());
                    System.out.println("This tile's ID: "+ tile.getId());
                    System.out.println("Dice total: " + player.getDiceTotal());
                    System.out.println();
                    if (player.getAvailableTiles().contains(tile.getId())           // If tha player's available tiles contains their diceTotal...
                    && canTakeTile) {                                               // ... and they canTakeTile...
                        player.takePlayerTile(game, tile.getId());                  // Player takes the other player's tile
                        resetGameBoard(game, player);
                        reverseCanRollDice();
                        player.reverseTurnIsNotFinished();
                    }
                });
            }
        }
    }

    /** playerTiles Getter
     * @return playerTiles (ArrayList<Button>)
     */
    public ArrayList<Button> getPlayerTiles() {
        return this.playerTiles;
    }

    /** rolledDice Setter
     * @param player (Player)
     */
    public void setRolledDice(Player player) {
        for (GamePiece die : player.getRolledDice()) {                              // For each die in the player's rolledDice...
            Button button = new Button();                                           // Create a button using the id variable and set desired sizing and alignment
            setDieButton(die, button);
            button.setAlignment(Pos.CENTER);
            button.setPrefSize(35, 35);
            this.rolledDice.add(button);                                            // Add the button to this.rolledDice
            button.getStyleClass().add("button-taken");                           // Alter formatting and reaction
            button.setOnMouseClicked(e -> buttonReaction(button, "button-unavailable-taken"));
        }
    }

    /** rolledDice resetter
     * - resets the rolled dice visuals based on the dice in the player's rolledDice
     * @param player (Player)
     */
    public void resetRolledDice(Player player, Game game) {
        // For the rolled dice
        for (int i = 0; i < 8; i++) {                                               // For each die in player's rolledDice...
            GamePiece die = player.getRolledDice()[i];                              // Define the die and button at index i
            Button button = this.getRolledDice().get(i);
            if (die.getIsAvailable()) {                                             // If the die is available...
                button.getStyleClass().remove(button.getStyleClass().size() - 1);   // remove the class style
                if (!player.getAvailableDice().contains(die.getId())) {             // if the die is not in the player's available dice...
                    setDieButton(die, button);                                      // set the button and formatting so it can't be taken
                    button.getStyleClass().add("button");
                    button.setOnMouseClicked(e -> buttonReaction(button, "button-click"));  // Add the click reaction
                }  else {                                                           // Otherwise...
                    setDieButton(die, button);                  	                // Set the button and formatting
                    button.getStyleClass().add("button");
                    button.setOnMouseClicked(e -> {                                 // If the button is clicked...
                        buttonReaction(button, "button-click");               // Add the click reaction
                        if (canTakeDice) {
                                player.takeDice(die.getId(), game);                 // player takes the die...
                                resetRolledDice(player, game);                      // ... and the interface's rolleDice and takenDice are reset
                                resetTakenDice(player);
                                reverseCanRollDice();
                                reverseCanTakeDice();
                                reverseCantakeTile();
                        }
                    });
                }
            } else {
                button.getStyleClass().remove(button.getStyleClass().size() - 1);   // replace th class style, and add the click reaction
                button.getStyleClass().add("button-taken");
                button.setOnMouseClicked(e -> buttonReaction(button, "button-unavailable-taken"));
            }
        }
    }

    /** rolledDice getter
     * @return rolledDice (ArrayList<Button>)
     */
    public ArrayList<Button> getRolledDice() {
        return this.rolledDice;
    }

    /** takenDice Setter
     * @param player (Player)
     */
    public void setTakenDice(Player player) {
        for (int i = 0; i < 8; i++) {                                               // For each die...
            takenDice.add(new Button());                                            // Create a button, set size and style (default = unavailable)
            takenDice.get(i).getStyleClass().add("button-taken");
        }
        resetTakenDice(player);                                                     // Implement resetTakenDice()
    }

    /** takenDice resetter
     * - resets the taken dice visuals based on the dice in the player's takenDice
     * @param player
     */
    public void resetTakenDice(Player player) {
        for (int i = 0; i < 8; i++) {                                               // For each die...
            Button button = this.takenDice.get(i);                                       // Create a button, set size and style (default = unavailable)
            button.setPrefSize(35, 35);
            button.setText("0");
            button.getStyleClass().remove(button.getStyleClass().size() - 1);
            button.getStyleClass().add("button-taken");
        }
        if (!player.getTakenDice().isEmpty()) {                                     // if player's takenDice is not empty
            for (int i = 0; i < player.getTakenDice().size(); i++) {                // for each die in player's takenDice
                GamePiece die = player.getTakenDice().get(i);                       // Define the current die
                Button button = this.takenDice.get(i);                              // Define the current (corresponding) button 
                setDieButton(die, button);
                button.getStyleClass().remove("button-taken");                    // Remove the button-taken style, and add that of an available button
                button.getStyleClass().add("button");
            }
        }
        for (int i = 0; i < 8; i++) {                                               // For all the die buttons
            Button button = this.takenDice.get(i);
            if (i > (player.getTakenDice().size() - 1)) {                           // If the button doesn't contain an actual die
                button.setOnMouseClicked(e -> buttonReaction(button, "button-unavailable-taken"));  // Alter formatting
            } else {                                                                // otherwise set it to button-click
                button.setOnMouseClicked(e -> buttonReaction(button, "button-click"));  // Alter formatting
            }
        }
    }

    /** takenDice Getter
     * @return takenDice (ArrayList<Button>)
     */
    public ArrayList<Button> getTakenDice() {
        return this.takenDice;
    }

    /** gamePlayButtons Setter
     * @param game (Game)
     */
    public void setGamePlayButtons(Game game, Player player, Stage primaryStage) {
        Button rollDice = new Button("Roll dice");                             // Create a rollDice Button and set alignment
        rollDice.setAlignment(Pos. CENTER);
        rollDice.setOnMouseClicked(e -> {
            buttonReaction(rollDice, "button-click");
            if (canRollDice) {
                player.rollDice(game);
                resetRolledDice(player, game);
                reverseCanRollDice();
                reverseCanTakeDice();
                reverseCantakeTile();
            }
        });

        Button forfeit = new Button("Forfeit");                             // Create a next Button and set alignment
        forfeit.setOnMouseClicked(e -> {
            buttonReaction(forfeit, "button-click");
            Popup popup = areYouSure(player, game);

            double stageWidth = primaryStage.getWidth();
            double stageHeight = primaryStage.getHeight();
            double stageX = primaryStage.getX();
            double stageY = primaryStage.getY();

            double xPosition = stageX + (stageWidth / 2) - 95;
            double yPosition = stageY + (stageHeight / 2) - 40;

            popup.show(primaryStage, xPosition, yPosition);
            
            // popup.show(primaryStage, 671, 350);
        });

        gamePlayButtons.add(rollDice);                                              // Add the buttons to this.gamePlayButtons
        gamePlayButtons.add(forfeit);
    }

    /** gamePlayButtons Getter
     * @return gamePlayButtons (ArrayList<Button>)
     */
    public ArrayList<Button> getGamePlayButtons() {
        return this.gamePlayButtons;
    }

    /** canRollDice Reverser
     * - reverses the boolean state of canRollDice
     */
    public void reverseCanRollDice() {
        this.canRollDice = !this.canRollDice;
    }

    /** canTakeDice Reverser
     * - reverses the boolean state of canTakeDice
     */
    public void reverseCanTakeDice() {
        this.canTakeDice = !this.canTakeDice;
    }

    /** canTakeTile Reverser
     * - reverses the boolean state of canTakeTile
     */
    public void reverseCantakeTile() {
        this.canTakeTile = !this.canTakeTile;
    }
    

    // Setting the scene

    /** getTopRow
     * - creates a VBox, with a title defining which player's turn it is, and all the tiles still in the game
     * @return topRow (VBox)
     */
    public VBox getTopRow() {
        HBox title = new HBox(20);                                                  // Create HBox, set alignment, and add a label with input from getPlayerTurn to it
        title.setAlignment(Pos.CENTER);
        Label label = new Label(getPlayersTurn());
        title.getChildren().addAll(label);

        HBox tiles = new HBox(20);                                                  // Create a VBox to hold all the buttons in gameTiles
        tiles.setAlignment(Pos.CENTER);
        for (Button button : this.getGameTiles()) {                                 // For each button in gameTiles, add that button to the tiles VBox
            tiles.getChildren().add(button);
        }

        VBox topRow = new VBox(20);                                                 // Create VBox for top row and set alignment and padding
        topRow.setAlignment(Pos.CENTER);
        topRow.setPadding(new Insets(20));
        topRow.getChildren().addAll(title, tiles);                                  // add the title HBox and the tiles VBox to the topRow VBox
        
        return topRow;
    }

    /** getMIddleRow
     * - creates an HBox, with every player's name, tileStackSize and corresponding button from this.playerTiles
     * @param game
     * @return
     */
    public HBox getMiddleRow(Game game) {
        HBox middleRow = new HBox(20);                                              // Create an HBox for the middle row and set alignment and padding
        middleRow.setAlignment(Pos.CENTER);
        middleRow.setPadding(new Insets(20));
        Region space = new Region();                                                // Create a space and add it to the middleRow HBox
        HBox.setHgrow(space, Priority.ALWAYS);
        middleRow.getChildren().add(space);

        for (int i = 0; i < game.getPlayers().size(); i++) {                        // For all the players in the game
            // Player player = game.getPlayers().get(i);                               // Set current player

            VBox playerTileStack = new VBox(20);                                    // Create a VBox to add each player's name and tile to, and set its alignment
            playerTileStack.setAlignment(Pos.CENTER);

            // Label playerName = new Label(player.getName() + "\n(" + player.getTileStack().size() + ")");    // Create a label with the player's name and the size of their tileStack, ...
            Label playerName = this.playerNames.get(i);                             // identify the correct label from this.playerNames
            playerName.setAlignment(Pos.CENTER);                                    // ... set alignment, and add the label and the player's corresponding button from playerTiles ...
            playerTileStack.getChildren().addAll(playerName, this.playerTiles.get(i));  // ... to the playerTileStack VBox

            Region anotherSpace = new Region();                                     // Create another space Region
            HBox.setHgrow(anotherSpace, Priority.ALWAYS);

            middleRow.getChildren().addAll(playerTileStack, anotherSpace);          // add the current playerTileStack VBox to the middleRow HBox, along with anotherSpace

        }

        return middleRow;
    }

    /** setupDice
     * - disrtibutes the dice over two rows in a VBox, and returns it
     * @param label (String)
     * @param diceButtons (ArrayList<Button>)
     * @return dice (VBox)
     */
    public VBox setupDice(String label, ArrayList<Button> diceButtons) {
        VBox dice = new VBox(20);                                                   // Create a VBox for the dice and set alignment and padding
        dice.setAlignment(Pos.CENTER);
        dice.setPadding(new Insets(40));

        HBox labelDiceHBox = new HBox(20);                                          // Create an HBox, create a label, and add the label to the HBox
        labelDiceHBox.setAlignment(Pos.CENTER);
        Label labelDice = new Label(label);
        labelDiceHBox.getChildren().add(labelDice);

        HBox diceRowOne = new HBox(20);                                             // Create 2 HBoxes, one each for holding 2 halves of the dice
        diceRowOne.setAlignment(Pos.CENTER);
        HBox diceRowTwo = new HBox(20);
        diceRowTwo.setAlignment(Pos.CENTER);
        for (int i = 0; i < 8; i++) {                                               // Distribute the dice buttons over the two HBoxes
            if (i < 4) {
                diceRowOne.getChildren().add(diceButtons.get(i));
            } else {
                diceRowTwo.getChildren().add(diceButtons.get(i));
            }
        }

        dice.getChildren().addAll(labelDiceHBox, diceRowOne, diceRowTwo);           // Add the initial label and both HBoxes to the playerDice VBox

        return dice;
    }

    /** getBottomRow
     * - Returns an HBox with the player's takenDice, rolledDice and "roll dice" and "next" buttons
     * @return bottomRow (HBox)
     */
    public HBox getBottomRow() {
        // For the playerDice (player's takenDice)
        VBox playerDice = setupDice("Your dice:", this.takenDice);            // Create a VBox for the player's taken dice using setupDice()

        // For the rolledDice (player's rolledDice)
        VBox rolledDice = setupDice("Available dice:", this.rolledDice);      // Create a VBox for the player's rolled dice using setupDice()

        // For the rollDice and Next buttons
        VBox diceButtons = new VBox(20);                                            // Create a VBox for the "Roll dice" and "Next" buttons, and set alignment and padding
        diceButtons.setAlignment(Pos. CENTER);
        diceButtons.setPadding(new Insets(75, 140, 40, 35));
        for (Button button : this.gamePlayButtons) {                                // For each button in this.gamePlayButtons, add that button to the VBox
            diceButtons.getChildren().add(button);
        }

        // Finally
        HBox bottomRow = new HBox(20);                                              // Create an HBox to hold all these buttons, set allignment and padding
        bottomRow.setAlignment(Pos.CENTER);
        bottomRow.setPadding(new Insets(-20, 0, -20, 0));
        bottomRow.getChildren().addAll(playerDice, rolledDice, diceButtons);        // Add all these elemenets to that HBox
        return bottomRow;
    }

    /** getScene
     * - Returns the entire scene of the player's view
     * @param game (Game)
     * @return scene (Scene)
     */
    public Scene getScene(Game game) {
        VBox screen = new VBox(20);                                                 // Create a VBox to hold every aspect
        screen.setAlignment(Pos.CENTER);
        screen.getChildren().addAll(getTopRow(), getMiddleRow(game), getBottomRow());   // Add the topRow, middleRow and bottomRow to the VBox using their functions

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


    // Resetting button actions and formats

    /** GamePiece Button Availability setter
     * - sets the formatting reaction for a button
     * @param gamepiece (GamePiece)
     * @param button (Button)
     */
    public void buttonReaction(Button button, String style) {
        PauseTransition pause = new PauseTransition(Duration.millis(50));           // Set a new pause of 50 ms
        button.getStyleClass().add(style);
        pause.play();                                                               // Play the pause
        pause.setOnFinished(ev -> {                                                 // When the pause finishes...
            button.getStyleClass().remove(style);                                   // replace the button's style back to the previous one
        });
    }

    /** setDieButton
     * - Set the value shown on the input Die Button, based on the id of the input Die
     * @param die (Die)
     * @param button (Button)
     */
    public void setDieButton(GamePiece die, Button button) {
        if (die.getId() == 6) {                                                     // If its id is 6, make the text of the die in the interface's takenDice at that ... "W"
            button.setText("W");
        } else {                                                                    // Otherwaise make it the die's id
            button.setText(String.valueOf(die.getValue()));
        }
    }

    public void resetGameBoard(Game game, Player player) {
        resetGameTiles(game, player);                                               // and the gameTile, playerNames and playerTile buttons/labels are reset
        resetPlayerNames(game);
        resetPlayerTiles(game, player);
        resetRolledDice(player, game);
        resetTakenDice(player);
    }

    public Popup areYouSure(Player player, Game game) {
        Popup popup = new Popup();
        
        Label popupText = new Label("are you sure?");
        popupText.setAlignment(Pos.CENTER);
        
        HBox options = new HBox(20);
        options.setAlignment(Pos.CENTER);

        Button yes = new Button("Yes");
        yes.setAlignment(Pos.CENTER);
        yes.setOnAction(e -> {
            buttonReaction(yes, "button-click");
            
            player.returnTile(game);
            resetGameTiles(game, player);                                       // and the gameTile, playerNames and playerTile buttons/labels are reset
            resetPlayerNames(game);
            resetPlayerTiles(game, player);
            player.reverseTurnIsNotFinished();

            popup.hide();
        });

        Button no = new Button("No");
        no.setAlignment(Pos.CENTER);
        no.setOnMouseClicked(e -> {
            buttonReaction(no, "button-click");
            popup.hide();
        });

        options.getChildren().addAll(yes, no);

        VBox popupView = new VBox(20);
        popupView.setPadding(new Insets(20));
        popupView.setAlignment(Pos.CENTER);
        popupView.getChildren().addAll(popupText, options);

        popup.getContent().add(popupView);

        return popup;
    }


}
