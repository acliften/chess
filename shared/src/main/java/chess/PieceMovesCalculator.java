package chess;

public class PieceMovesCalculator {
    ChessBoard board;
    ChessPosition pos;

    public PieceMovesCalculator(ChessBoard board, ChessPosition pos) {
        this.board = board;
        this.pos = pos;
    }
}
