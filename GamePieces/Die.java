package GamePieces;

public final class Die implements GamePiece {
    
    private int id;

    private int value;

    private Boolean isAvailable = true;

    /** DieGamePiece constructor
     */
    public Die() {
        super();
        this.id = (int)(Math.random()*6+1);
        setId(id);
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setId(int id) {
        this.id = id;                           // always re-set the value, gamePiecePileVisual and availableGamePieceVisual when setting the id
        setValue(this.id);
    }

    @Override
    public int getValue() {
        return this.value;
    }

    @Override
    public void setValue(int id) {
        if (id == 6) {                          // if die id is 6, die value is 5
            this.value = 5;
        } else {                                // otherwise its value is equal top its id
            this.value = id;
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
