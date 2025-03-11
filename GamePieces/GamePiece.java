package GamePieces;

public interface GamePiece {
    
    /** GamePiece id getter
     * @return id (int)
     */
    public int getId();

    /** Gamepiece id setter
     * @param id (int)
     */
    public void setId(int id);

    /** GamePiece value getter
     * @return value (int)
     */
    public int getValue();

    /** GamePiece value setter
     * @param id (int)
     */
    public void setValue(int id);

    /** GamePiece isAvailable getter
     * @return isAvailable (boolean)
     */
    public boolean getIsAvailable();

    /** GamePiece isAvailable setter
     * @param availability (boolean)
     */
    public void setIsAvailable(boolean availability);
    
}
