package chess;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private PieceType pieceType;
    private ChessGame.TeamColor teamColor;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceType = type;
        this.teamColor = pieceColor;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        //check the position in the chessboard to see what piece it is
        //make methods to move in every direction
        //make methods to calculate how each piece can move in that position - create MoveCalculator class?

        if (board.getPiece(myPosition).getPieceType() == PieceType.KING){
            KingMovesCalculator kingMoves = new KingMovesCalculator();
            return kingMoves.calculateMoves(board, myPosition);
        }

        if (board.getPiece(myPosition).getPieceType() == PieceType.QUEEN){
            QueenMovesCalculator queenMoves = new QueenMovesCalculator();
            return queenMoves.calculateMoves(board, myPosition);
        }

        if (board.getPiece(myPosition).getPieceType() == PieceType.BISHOP){
            BishopMovesCalculator bishopMoves = new BishopMovesCalculator();
            return bishopMoves.calculateMoves(board, myPosition);
        }

        if (board.getPiece(myPosition).getPieceType() == PieceType.ROOK){
            RookMovesCalculator rookMoves = new RookMovesCalculator();
            return rookMoves.calculateMoves(board, myPosition);
        }

        if (board.getPiece(myPosition).getPieceType() == PieceType.KNIGHT){
            KnightMovesCalculator knightMoves = new KnightMovesCalculator();
            return knightMoves.calculateMoves(board, myPosition);
        }

        if (board.getPiece(myPosition).getPieceType() == PieceType.PAWN){
            PawnMovesCalculator pawnMoves = new PawnMovesCalculator();
            return pawnMoves.calculateMoves(board, myPosition);
        }

        throw new IllegalArgumentException("no piece to move at that position on the board");
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceType == that.pieceType && teamColor == that.teamColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceType, teamColor);
    }
}
