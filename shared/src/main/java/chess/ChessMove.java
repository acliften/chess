package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    private ChessPosition sp;
    private ChessPosition ep;
    private ChessPiece.PieceType pp;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.sp = startPosition;
        this.ep = endPosition;
        this.pp = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return sp;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return ep;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return pp;
    }

    @Override
    public String toString() {
        return "ChessMove{" +
                "sp=" + sp +
                ", ep=" + ep +
                ", pp=" + pp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessMove chessMove = (ChessMove) o;
        return Objects.equals(sp, chessMove.sp) && Objects.equals(ep, chessMove.ep) && pp == chessMove.pp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sp, ep, pp);
    }
}
