package ui;

import chess.*;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

public class ChessBoard {

    public ChessBoard(){

    }

    public void drawChessboard(ChessGame game, boolean blackPOV){
        String colLetters;
        int rowStart;
        int rowEnd;
        int colStart;
        int colEnd;
        int rowDirection;
        int colDirection;

        if (blackPOV){
            colLetters = " h  g  f  e  d  c  b  a ";
            rowStart = 1;
            rowEnd = 8;
            colStart = 8;
            colEnd = 1;
            rowDirection = 1;
            colDirection = -1;
        } else {
            colLetters = " a  b  c  d  e  f  g  h ";
            rowStart = 8;
            rowEnd = 1;
            colStart = 1;
            colEnd = 8;
            rowDirection = -1;
            colDirection = 1;
        }

        System.out.println(" ");
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + "   ");
        System.out.print(EscapeSequences.SET_BG_COLOR_WHITE  + colLetters);
        System.out.println(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + "   " + EscapeSequences.RESET_BG_COLOR);

        for (int i = rowStart; i != rowEnd + rowDirection; i += rowDirection){
            System.out.print(EscapeSequences.SET_BG_COLOR_WHITE  + " " + i + " " + EscapeSequences.RESET_BG_COLOR);
            for (int j = colStart; j != colEnd + colDirection; j += colDirection){
                boolean isDark = (i + j) % 2 == 0;
                String backgroundColor = isDark ? EscapeSequences.SET_BG_COLOR_BLACK : EscapeSequences.SET_BG_COLOR_RED;

                ChessPiece piece = game.getBoard().getPiece(new ChessPosition(i, j));
                String pieceString = getPieceString(piece);
                String textColor = "";
                if (piece != null){
                    textColor = piece.getTeamColor() == BLACK?
                                                        EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY : EscapeSequences.SET_TEXT_COLOR_WHITE;
                }
                System.out.print(backgroundColor + textColor + pieceString +
                                 EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR);
            }
            System.out.println(EscapeSequences.SET_BG_COLOR_WHITE  + " " + i + " " + EscapeSequences.RESET_BG_COLOR);
        }

        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + "   ");
        System.out.print(EscapeSequences.SET_BG_COLOR_WHITE  + colLetters);
        System.out.println(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + "   " + EscapeSequences.RESET_BG_COLOR + "\n");
    }

    private String getPieceString(ChessPiece piece) {
        if (piece == null){
            return EscapeSequences.EMPTY;
        }

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
