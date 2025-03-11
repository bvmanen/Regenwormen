package Players;

import Game.*;
import GamePieces.*;
import java.util.ArrayList;
import java.util.Stack;

public class Player {

    private int id;

    private boolean nameIsUnknown = true;

    private String name;

    private boolean turnIsNotFinished = true;

    private Stack<GamePiece> tileStack = new Stack<>();

    private GamePiece[] rolledDice = new GamePiece[8];

    private ArrayList<GamePiece> takenDice = new ArrayList<>();

    private ArrayList<Integer> takenDiceIds = new ArrayList<>();

    private int score = 0;

    private int diceTotal = 0;

    private ArrayList<Integer> availableDice = new ArrayList<>();

    private ArrayList<Integer> availableTiles = new ArrayList<>();


    /** Player contsructor
     * @param id (int)
     */
    public Player(int id) {
        this.id = id;
        for (int i = 0; i < this.rolledDice.length; i++) {
            Die dieGamePiece = new Die();
            this.rolledDice[i] = dieGamePiece;                                       // create dice so rolledDice array is not empty
        }
        setAvailableDice();
    }

    /** Player id getter
     * @return id (int)
     */
    public int getId() {
        return this.id;
    }

    /** Player id setter
     * @param id (int)
     */
    public void setId(int id) {
        this.id = id;
    }

    /** nameIsUnkown getter
     * @return nameIsUnkown (boolean)
     */
    public boolean nameIsUnknown() {
        return this.nameIsUnknown;
    }

    /** nameIsUnkown Setter
     * @param nameIsUnknown (boolean)
     */
    public void setNameIsUnknown(boolean nameIsUnknown) {
        this.nameIsUnknown = nameIsUnknown;
    }

    /** nameIsUnkown Reverser
     */
    public void reverseNameIsUnknown() {
        this.nameIsUnknown = !this.nameIsUnknown;
    }

    /** Player name getter
     * @return name (String)
     */
    public String getName() {
        return this.name;
    }

    /** Player name setter
     * @param name (String)
     */
    public void setName(String name) {
        this.name = String.format("%1.16s", name);
    }

    /** turnIsNotFinished getter
     * @return turnIsNotFinished (boolean)
     */
    public boolean turnIsNotFinished() {
        return this.turnIsNotFinished;
    }

    /** turnIsNotFinished setter
     * @param turnIsNotFinished
     */
    public void setTurnIsNotFinished(boolean turnIsNotFinished) {
        this.turnIsNotFinished = turnIsNotFinished;
    }

    /** turnIsNotFinished reverser
     */
    public void reverseTurnIsNotFinished() {
        this.turnIsNotFinished = !this.turnIsNotFinished;
    }

    /** Player tileStack getter
     * @return tileStack (ArrayList<GamePiece>)
     */
    public Stack<GamePiece> getTileStack() {
        return this.tileStack;
    }

    /** Player tileStack setter
     * @param tiles (ArrayList<GamePiece>)
     */
    public void setTileStack(Stack<GamePiece> tiles) {
        this.tileStack = tiles;
    }

    /** Player rolledDice getter
     * @return rolledDice (GamePiece[])
     */
    public GamePiece[] getRolledDice() {
        return this.rolledDice;
    }

    /** Player rollDice
     * - replaces setRolledDice()
     */
    public void rollDice(Game game) {
        for (GamePiece die : this.rolledDice) {
            id = (int)(Math.random()*6+1);
            die.setId(id);
        }
        setAvailableDice();
    }

    /** Player takenDice getter
     * @return takenDice (ArrayList<GamePiece>)
     */
    public ArrayList<GamePiece> getTakenDice() {
        return this.takenDice;
    }

        /** Player takenDice setter
     * @param dice (ArrayList<GamePiece>)
     */
    public void setTakenDice(ArrayList<GamePiece> dice) {
        this.takenDice = dice;
    }

    /** Player takenDiceIds setter
     * @param dice (ArrayList<Integer>)
     */
    public void setTakenDiceIds(ArrayList<Integer> dice) {
        this.takenDiceIds = dice;
    }

    /** Player takenDiceIds getter
     * @return takenDice (ArrayList<Integer>)
     */
    public ArrayList<Integer> getTakenDiceIds() {
        return this.takenDiceIds;
    }

    /** Player score getter
     * @return score (int)
     */
    public int getScore() {
        return this.score;
    }

    /** Player score setter
     * @param score (int)
     */
    public void setScore(int score) {
        this.score = score;
    }

    /** Player score updater
     * - to add or subtract the score of a taken or lost tile
     * @param score (int)
     */
    public void updateScore(int score) {
        this.score += score;
    }

    /** Player diceTotal getter
     * @return diceTotal (int)
     */
    public int getDiceTotal() {
        return this.diceTotal;
    }

    /** Player diceTotal setter
     * @param diceTotal (int)
     */
    public void setDiceTotal(int diceTotal) {
        this.diceTotal = diceTotal;
    }

    /** Player availableDice (re)setter
     */
    public void setAvailableDice() {
        if (!this.availableDice.isEmpty()) {
            this.availableDice.clear();
        }
        for (int i = 0; i < rolledDice.length; i++) {
            GamePiece die = this.rolledDice[i];
            if (die.getIsAvailable() && !this.takenDiceIds.contains(die.getId())) {
                this.availableDice.add(die.getId());
            }
        }
    }

    /** Player availableDice getter
     * @return availableDice (ArrayList<Integer>)
     */
    public ArrayList<Integer> getAvailableDice() {
        return this.availableDice;
    }

    /** Player availableTiles (re)setter
     */
    public void setAvailableTiles(Game game) {
        if (!this.availableTiles.isEmpty()) {this.availableTiles.clear();}
        if (this.takenDiceIds.contains(6)) {
            for (int i = 0; i < game.getTiles().length; i++) {
                GamePiece tile = game.getTiles()[i];
                if (tile.getIsAvailable() && (tile.getId() == this.diceTotal || (tile.getId() == this.diceTotal - 1 && i < game.getTiles().length-1 && !game.getTiles()[i+1].getIsAvailable()))) {
                    this.availableTiles.add(tile.getId());
                }
            }
            for (Player player : game.getPlayers()) {
                if (player.getId() != this.id && !player.getTileStack().empty() && player.getTileStack().peek().getId() == this.diceTotal) {
                    this.availableTiles.add(this.diceTotal);
                }
            }
        }
    }

    /** Player availableTiles getter
     * @return availableTiles (ArrayList<Integer>)
     */
    public ArrayList<Integer> getAvailableTiles() {
        return this.availableTiles;
    }

    /** Player takeDice
     * - moves dice from the rolledDice array to the takenDice array
     * @param id (int)
     */
    public void takeDice(int id, Game game) {
        for (GamePiece die : this.getRolledDice()) {                                      // loop through rolledDice, for every dice which is avbailable, and where input id is in rolledDice:
            // add comparison to player's takenDice here <--------------
            if (die.getIsAvailable() && die.getId() == id) {
                Die newDie = new Die();
                newDie.setId(id);
                this.takenDice.add(newDie);                                        // add a die with the input id to player's takenDice
                this.takenDiceIds.add(id);
                die.setIsAvailable(false);                         // set the dice in rolledDice to unavailable
                this.diceTotal += die.getValue();
                setAvailableTiles(game);
            }
        }
    }

    /** Player takePlayerTile
     * - player takes a tile from the top of another players tileStack
     * @param game (Game)
     * @param id (int)
     */
    public void takePlayerTile(Game game, int id) {
        for (Player player : game.getPlayers()) {                               // Loop through all items in the game's player list...
            Stack<GamePiece> tileStack = player.getTileStack();
            if (!tileStack.isEmpty()) {                                         // if current player's tileStack is not empty:
                GamePiece topTile = tileStack.peek();                           // get the top tile in the stack
                if (player.getId() != this.id && topTile.getId() == id) {       // if the top tile in thier stack has the same id as the input id, and the current player's id doesn't match that of the taking player's id...
                    GamePiece tile = tileStack.pop();                           // ... Remove that tile from the player's stack
                    this.tileStack.add(new Tile(tile.getId()));                 // and add it to this players stack
                    player.setScore(player.getScore() - tile.getValue());       // remove the tile's value from this player's score
                    this.score += tile.getValue();                              // add the tile's value to the taking player's score
                    this.setDiceTotal(0);                             // set player's dice total back to 0
                    this.takenDice.clear();
                    this.takenDiceIds.clear();
                    for (GamePiece die : this.rolledDice) {
                        die.setIsAvailable(true);
                    }
                    break;
                }
            } 
        }
    }

    /** Player takeGameTile
     * - player takes a tile from the input game's available tiles
     * @param game (Game)
     * @param id (int)
     */
    public void takeGameTile(Game game, int id) {
        for (GamePiece tile : game.getTiles()) {                                // Loop through the game's available tiles
            if (tile.getId() == id && tile.getIsAvailable()) {                  // if the current tile's id matches the input id, and the tile is available:
                this.tileStack.add(new Tile(tile.getId()));                                       // add that tile to the player's tileStack
                tile.setIsAvailable(false);                        // set the current tile's availablity to unavailable
                this.score += tile.getValue();                                  // add the tile's value to the player's score
                this.setDiceTotal(0);                                 // set dice total back to 0
                this.takenDice.clear();
                this.takenDiceIds.clear();
                game.checkIfGameIsOver();
                for (GamePiece die : this.rolledDice) {
                    die.setIsAvailable(true);
                }
                break;
            }
        }
    }

    /** Player returnTile
     * - places a tile back on the game's tile array, and removes the highest available tile from that array, if a player's round is unsuccesful
     * @param game (Game)
     */
    public void returnTile(Game game) {
        if (!this.tileStack.isEmpty()) {                                        // If there is actually something in player's tileStack...
            int tileId = this.getTileStack().peek().getId();                    // get's the id from the top tile from the player's tileStack
            this.score -= this.tileStack.pop().getValue();                      // pops the top tile from the player's stack and subtracts its value from the player's score
            for (GamePiece tile : game.getTiles()) {                            // loops through the game's tiles
                if (tile.getId() == tileId) {
                    tile.setIsAvailable(true);                     // sets the tile with the popped tile's id to available
                }
            }
            for (int i = game.getTiles().length-1; i >= 0; i--) {               // Loop through the game's tileStack backwards...
                if (game.getTiles()[i].getIsAvailable() && game.getTiles()[i].getId() > tileId) {   // if the current tile is available, and higher than the tile just placed back...
                    game.getTiles()[i].setIsAvailable(false);      // ... set it to unavailable
                    break;                                                      // then break
                }
            }
        }
        this.setDiceTotal(0);                                         // set dice total back to 0
        this.takenDice.clear();                                                 // clear all takenDice and takenDiceIds
        this.takenDiceIds.clear();
        for (GamePiece die : this.rolledDice) {                                 // Set all rolled dice back to available
            die.setIsAvailable(true);
        }
    }

}