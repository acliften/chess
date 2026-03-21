package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

public class ChessBoard {

    public static void main(String[] args) {
        ChessBoard cb = new ChessBoard();
        ChessGame game = new ChessGame();

        cb.drawChessboard(game, 1);
    }

    public void drawChessboard(ChessGame game, int black){
        System.out.println(EscapeSequences.SET_BG_COLOR_WHITE  + "    a  b  c  d  e  f  g  h    " + EscapeSequences.RESET_BG_COLOR);
        for (int i = 8; i >= 1; i--){
            System.out.print(EscapeSequences.SET_BG_COLOR_WHITE  + " " + i + " " + EscapeSequences.RESET_BG_COLOR);
            for (int j = 1; j <= 8; j++){

                boolean isBlack = (i + j) % 2 == 0;
                String bg = isBlack ? EscapeSequences.SET_BG_COLOR_BLACK : EscapeSequences.SET_BG_COLOR_RED;

                ChessPiece piece = game.getBoard().getPiece(new ChessPosition(i, j));
                String pieceString = getPieceString(piece);
                String tc = "";
                if (piece != null){
                    tc = piece.getTeamColor() == BLACK? EscapeSequences.SET_TEXT_COLOR_DARK_GREY : EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY;
                }
                System.out.print(bg + tc + pieceString + EscapeSequences.RESET_BG_COLOR);
            }
            System.out.println(EscapeSequences.SET_BG_COLOR_WHITE  + " " + i + " " + EscapeSequences.RESET_BG_COLOR);
        }
        System.out.println(EscapeSequences.SET_BG_COLOR_WHITE  + "    a  b  c  d  e  f  g  h    " + EscapeSequences.RESET_BG_COLOR);
    }

    private String getPieceString(ChessPiece piece) {
        if (piece == null) return EscapeSequences.EMPTY;

        return switch (piece.getPieceType()) {
            case KING -> piece.getTeamColor() == WHITE ? EscapeSequences.WHITE_KING : EscapeSequences.BLACK_KING;
            case QUEEN -> piece.getTeamColor() == WHITE ? EscapeSequences.WHITE_QUEEN : EscapeSequences.BLACK_QUEEN;
            case ROOK -> piece.getTeamColor() == WHITE ? EscapeSequences.WHITE_ROOK : EscapeSequences.BLACK_ROOK;
            case BISHOP -> piece.getTeamColor() == WHITE ? EscapeSequences.WHITE_BISHOP : EscapeSequences.BLACK_BISHOP;
            case KNIGHT -> piece.getTeamColor() == WHITE ? EscapeSequences.WHITE_KNIGHT : EscapeSequences.BLACK_KNIGHT;
            case PAWN -> piece.getTeamColor() == WHITE ? EscapeSequences.WHITE_PAWN : EscapeSequences.BLACK_PAWN;
        };
    }

}
