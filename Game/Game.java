package Game;

import GamePieces.*;
import Players.*;
import java.util.ArrayList;

public class Game {

    private GamePiece[] tiles = new GamePiece[16];

    private ArrayList<Player> players = new ArrayList<>();

    private Boolean gameIsNotOver = true;

    /** Game constructor
     * @param numberOfPlayers (int)
     */
    public Game(int numberOfPlayers) {
        for (int i = 21; i < (tiles.length + 21); i++) {                      // generate a full board with dominos
            Tile tile = new Tile(i);
            tiles[i-21] = tile;
        }
        int n = 1;
        while (n <= numberOfPlayers) {                             // add the number of players (specified in input parameter) to the players array
            Player player = new Player(n);
            players.add(player);                                                // number their id's starting at 1, incrementing for every player
            n += 1;
        }
    }

    /** Game tiles getter
     * @return tiles (GamePiece[])
     */
    public GamePiece[] getTiles() {
        return this.tiles;
    }

    /** Game tiles setter
     * @param tiles (GamePiece[])
     */
    public void setTiles(Tile[] tiles) {
        this.tiles = tiles;
    }

    /** Game players getter
     * @return players (Player[])
     */
    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    /** Game players setter
     * @param players (Player[])
     */
    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    /** gameIsNotOver getter
     * @return
     */
    public Boolean isNotOver() {
        return this.gameIsNotOver;
    }

    /** gameIsNotOver Reverser
     */
    public void reverseGameIsNotOver() {
        this.gameIsNotOver = !this.gameIsNotOver;
    }

    /** checkIfGameIsOver
     * - checks how many of the game's tiles are available. If none of them are, the gameIsNotOver boolean is reversed.
     */
    public void checkIfGameIsOver() {
        int availableTileCounter = 0;
        for (GamePiece tile : this.tiles) {
            if (tile.getIsAvailable()) {
                availableTileCounter += 1;
            }
        }
        if (availableTileCounter < 1) {
            reverseGameIsNotOver();
        }
    }

}
