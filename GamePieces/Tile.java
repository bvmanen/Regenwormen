package GamePieces;

public final class Tile implements GamePiece {
    
    private int id;

    private int value;

    private Boolean isAvailable = true;

    /** DominoGamePiece constructor
     * @param id (int)
     */
    public Tile(int id) {
        setId(id);
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setId(int id) {
        this.id = id;                           // always re-set the value, gamePiecePileVisual and availableGamePieceVisual when setting the id
        setValue(id);
    }

    @Override
    public int getValue() {
        return this.value;
    }

    @Override
    public void setValue(int id) {
        if (id > 20 && id < 25) {               // if id is between 21 and 24 set value to 1
            this.value = 1;
        } else if (id > 24 && id < 29) {        // if id is between 25 and 28 set value to 2
            this.value = 2;
        } else if (id > 28 && id < 33) {        // if id is between 29 and 32 set value to 3
            this.value = 3;
        } else {                                // otherwise (id is between 33 and 36) set value to 4
            this.value = 4;
        }
    }

    @Override
    public boolean getIsAvailable() {
        return this.isAvailable;
    }

    @Override
    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

}
