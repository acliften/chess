package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private int theRow;
    private int theColumn;

    public ChessPosition(int row, int col) {
        this.theRow = row;
        this.theColumn = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return theRow;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return theColumn;
    }

    @Override
    public String toString() {
        return String.format("[%d, %d]", theRow, theColumn);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPosition that = (ChessPosition) o;
        return theRow == that.theRow && theColumn == that.theColumn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(theRow, theColumn);
    }
}
