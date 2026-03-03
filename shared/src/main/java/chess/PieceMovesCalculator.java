package chess;

import java.util.ArrayList;
import java.util.List;

public class PieceMovesCalculator {

    public List<ChessMove> calculateMoves(ChessBoard board, ChessPosition pos){
        return List.of();
    }

    protected void addDirectionalMoves(ChessBoard board, ChessPosition start, List<ChessMove> moves, int rowDir, int colDir) {
        int newRow = start.getRow();
        int newCol = start.getColumn();

        while (true) {
            newRow += rowDir;
            newCol += colDir;

            if (newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8) {
                break;
            }

            ChessPosition np = new ChessPosition(newRow, newCol);
            ChessPiece target = board.getPiece(np);
            ChessPiece startPiece = board.getPiece(start);

            if (target == null) {
                moves.add(new ChessMove(start, np, null));
            } else {
                if (target.getTeamColor() != startPiece.getTeamColor()) {
                    moves.add(new ChessMove(start, np, null));
                }
                break;
            }
        }
    }

    public void oneLoopMoves(List<ChessPosition> offset, ChessBoard board, ChessPosition pos, List<ChessMove> moves){
        for (ChessPosition dir : offset){
            int newRow = pos.getRow() + dir.getRow();
            int newCol = pos.getColumn() + dir.getColumn();
            if (newRow >= 1 && newRow <= 8 && newCol >=1 && newCol <=8){
                ChessPosition np = new ChessPosition(newRow, newCol);
                if (board.getPiece(np) == null || board.getPiece(np).getTeamColor() != board.getPiece(pos).getTeamColor()){
                    moves.add(new ChessMove(pos, np, null));
                }

            }

        }
    }

}

class KingMovesCalculator extends PieceMovesCalculator{

    @Override
    public List<ChessMove> calculateMoves(ChessBoard board, ChessPosition pos){
        List<ChessMove> moves = new ArrayList<>();
        List<ChessPosition> offset = List.of(new ChessPosition(-1, -1), new ChessPosition(-1, 0),
                                             new ChessPosition(-1, 1), new ChessPosition(0, -1),
                                             new ChessPosition(0, 1), new ChessPosition(1, -1),
                                             new ChessPosition(1, 0), new ChessPosition(1, 1));

        oneLoopMoves(offset, board, pos, moves);

        return moves;
    }

}

class QueenMovesCalculator extends PieceMovesCalculator {

    @Override
    public List<ChessMove> calculateMoves(ChessBoard board, ChessPosition pos){
        List<ChessMove> moves = new ArrayList<>();

        int[][] directions = {
                {-1,-1}, {-1,0}, {-1,1},
                {0,-1},           {0,1},
                {1,-1},  {1,0},  {1,1}
        };

        for (int[] dir : directions) {
            addDirectionalMoves(board, pos, moves, dir[0], dir[1]);
        }

        return moves;
    }

}

class BishopMovesCalculator extends PieceMovesCalculator {

    @Override
    public List<ChessMove> calculateMoves(ChessBoard board, ChessPosition pos){
        List<ChessMove> moves = new ArrayList<>();

        int[][] directions = {
                {1,1}, {1,-1}, {-1,1}, {-1,-1}
        };

        for (int[] dir : directions) {
            addDirectionalMoves(board, pos, moves, dir[0], dir[1]);
        }

        return moves;
    }

}

class KnightMovesCalculator extends PieceMovesCalculator {

    @Override
    public List<ChessMove> calculateMoves(ChessBoard board, ChessPosition pos){
        List<ChessMove> moves = new ArrayList<>();

        List<ChessPosition> offset = List.of(new ChessPosition(2, 1), new ChessPosition(2, -1),
                                             new ChessPosition(-2, 1), new ChessPosition(-2, -1),
                                             new ChessPosition(1, 2), new ChessPosition(-1, 2),
                                             new ChessPosition(1, -2),new ChessPosition(-1, -2));

        oneLoopMoves(offset, board, pos, moves);

        return moves;
    }

}

class RookMovesCalculator extends PieceMovesCalculator {

    @Override
    public List<ChessMove> calculateMoves(ChessBoard board, ChessPosition pos){
        List<ChessMove> moves = new ArrayList<>();

        int[][] directions = {
                {-1,0}, {1,0}, {0,1}, {0,-1}
        };

        for (int[] dir : directions) {
            addDirectionalMoves(board, pos, moves, dir[0], dir[1]);
        }

        return moves;
    }

}

class PawnMovesCalculator extends PieceMovesCalculator {

    @Override
    public List<ChessMove> calculateMoves(ChessBoard board, ChessPosition pos){
        List<ChessMove> moves = new ArrayList<>();
        int colorOffset;
        int row = pos.getRow();
        int col = pos.getColumn();

        //find out which color while also handling 2 space first move
        if (board.getPiece(pos).getTeamColor() == ChessGame.TeamColor.BLACK){
            colorOffset = -1;
            if (row == 7
                    && board.getPiece(new ChessPosition(row + colorOffset, col)) == null
                    && board.getPiece(new ChessPosition(row -2, col)) == null){
                moves.add(new ChessMove(pos, new ChessPosition(row - 2, col), null));
            }
        } else {
            colorOffset = 1;
            if (row == 2
                    && board.getPiece(new ChessPosition(row + colorOffset, col)) == null
                    && board.getPiece(new ChessPosition(row + 2, col)) == null){
                moves.add(new ChessMove(pos, new ChessPosition(row + 2, col), null));
            }
        }

        //move forward
        if (board.getPiece(new ChessPosition(row + colorOffset, col)) == null
                && row +colorOffset < 8
                && row + colorOffset > 1){
            moves.add(new ChessMove(pos, new ChessPosition(row + colorOffset, col), null));
        }

        //diagonal take
        if (col + 1 <= 8
                && board.getPiece(new ChessPosition(row + colorOffset, col + 1)) != null
                && board.getPiece(new ChessPosition(row + colorOffset, col + 1)).getTeamColor() != board.getPiece(pos).getTeamColor()){
            //check for promotion capture
            if (row + colorOffset == 8 || row + colorOffset == 1){
                moves.add(new ChessMove(pos, new ChessPosition(row + colorOffset, col + 1), ChessPiece.PieceType.QUEEN));
                moves.add(new ChessMove(pos, new ChessPosition(row + colorOffset, col + 1), ChessPiece.PieceType.ROOK));
                moves.add(new ChessMove(pos, new ChessPosition(row + colorOffset, col + 1), ChessPiece.PieceType.KNIGHT));
                moves.add(new ChessMove(pos, new ChessPosition(row + colorOffset, col + 1), ChessPiece.PieceType.BISHOP));
            } else {
                moves.add(new ChessMove(pos, new ChessPosition(row + colorOffset, col + 1), null));
            }
        } else if (col - 1 >= 1
                   && board.getPiece(new ChessPosition(row + colorOffset, col - 1)) != null
                   && board.getPiece(new ChessPosition(row + colorOffset, col - 1)).getTeamColor() != board.getPiece(pos).getTeamColor()){
            //check for promotion capture
            if (row + colorOffset == 8 || row + colorOffset == 1){
                moves.add(new ChessMove(pos, new ChessPosition(row + colorOffset, col - 1), ChessPiece.PieceType.QUEEN));
                moves.add(new ChessMove(pos, new ChessPosition(row + colorOffset, col - 1), ChessPiece.PieceType.ROOK));
                moves.add(new ChessMove(pos, new ChessPosition(row + colorOffset, col - 1), ChessPiece.PieceType.KNIGHT));
                moves.add(new ChessMove(pos, new ChessPosition(row + colorOffset, col - 1), ChessPiece.PieceType.BISHOP));
            } else {
                moves.add(new ChessMove(pos, new ChessPosition(row + colorOffset, col -1), null));
            }
        }

        //promote
        if (row + colorOffset == 8 || row + colorOffset == 1){
            moves.add(new ChessMove(pos, new ChessPosition(row + colorOffset, col), ChessPiece.PieceType.QUEEN));
            moves.add(new ChessMove(pos, new ChessPosition(row + colorOffset, col), ChessPiece.PieceType.ROOK));
            moves.add(new ChessMove(pos, new ChessPosition(row + colorOffset, col), ChessPiece.PieceType.KNIGHT));
            moves.add(new ChessMove(pos, new ChessPosition(row + colorOffset, col), ChessPiece.PieceType.BISHOP));
        }

        return moves;
    }

}