
import Game.Game;
import Players.Player;

import UserInterfaces.GameSetup;
import UserInterfaces.PlayerView;
import UserInterfaces.WinnerScreen;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class Main extends Application {
    
    Stage window;

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        window = primaryStage;
        startGame(window);

    }

    public void startGame(Stage primaryStage) {
        new Thread(() -> {
            while (true) {
                // Setup Game Size
                GameSetup gameSizeSetup = new GameSetup();                          // Create a new GameSetup, and...
                Platform.runLater(() -> {                                           // Use it to set the scene
                    gameSizeSetup.setScene(primaryStage, gameSizeSetup.getFirstScene(primaryStage ));
                });
                while (gameSizeSetup.choiceIsNotMade()) {                           // While the users have not made a choice...
                    try {
                        Thread.sleep(100);                                   // Briefly put the therad to sleep, to avoid blocking the UI thread
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // Create Game
                Game game = new Game(gameSizeSetup.getGameSize());                  // Once they have made a choice, setup a Game
            
                // Setup Player Names
                for (int i = 0; i < game.getPlayers().size(); i++) {                // for each player in the game...
                    int index = i;                                                  // save the index
                    Player player = game.getPlayers().get(i);                       // and save the player into a variable
                    if (player.nameIsUnknown()) {                                   // If the player's name is unkown...
                        Platform.runLater(() -> {                                   // ... Update the UI for that player using a new GameSetup
                            GameSetup gamePlayersSetup = new GameSetup();
                            gamePlayersSetup.setScene(primaryStage, gamePlayersSetup.getSecondScene(game, index));
                        });
                        while (player.nameIsUnknown()) {                            // While the current player has not picked a name...
                            try {                                                   
                                Thread.sleep(100);                           // Briefly put the therad to sleep, to avoid blocking the UI thread
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                // Play Game
                while (game.isNotOver()) {                                          // While true...
                    for (int i = 0; i < game.getPlayers().size(); i++) {            // For each player in the game
                        Player player = game.getPlayers().get(i);                   // specify the player at the index, and set their turnIsNotFinished to true, to begin with.
                        player.setTurnIsNotFinished(true);
                        if (player.turnIsNotFinished()) {                           // If the player's turn is not finished...
                            Platform.runLater(() -> {                               // ... Update the UI for that player
                                PlayerView playerView = new PlayerView(game, player, primaryStage);
                                playerView.setScene(primaryStage, playerView.getScene(game));
                            });
                            while (player.turnIsNotFinished()) {                    // wait until the player's turn is finished
                                try {
                                    Thread.sleep(100);                       // Sleep for 100ms before checking again
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        if (!game.isNotOver()) {                                    // Break when the game is over
                            break;
                        }
                    }
                } 

                WinnerScreen winnerScreen = new WinnerScreen(game);
                if (!winnerScreen.wantsToPlayAgain()) {
                    Platform.runLater(() -> {                                       // then show the winners screen
                        winnerScreen.setScene(primaryStage, winnerScreen.getWinnerScreen(primaryStage));
                    });
                    while (!winnerScreen.wantsToPlayAgain()) {
                        try {
                            Thread.sleep(100);                               // Sleep for 100ms before checking again
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (winnerScreen.wantsToPlayAgain()) {
                        continue;
                    }
                }

            }

        }).start();
    }
}

