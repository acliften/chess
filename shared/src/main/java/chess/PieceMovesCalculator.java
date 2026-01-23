package chess;

import java.util.ArrayList;
import java.util.List;

public class PieceMovesCalculator {

    public List calculateMoves(ChessBoard board, ChessPosition pos){
        return List.of();
    }

}

//        [1,1] [1,2] [1,3] [1,4] [1,5] [1,6] [1,7] [1,8]
//        [2,1] [2,2] [2,3] [2,4] [2,5] [2,6] [2,7] [2,8]
//        [3,1] [3,2] [3,3] [3,4] [3,5] [3,6] [3,7] [3,8]
//        [4,1] [4,2] [4,3] [4,4] [4,5] [4,6] [4,7] [4,8]
//        [5,1] [5,2] [5,3] [5,4] [5,5] [5,6] [5,7] [5,8]
//        [6,1] [6,2] [6,3] [6,4] [6,5] [6,6] [6,7] [6,8]
//        [7,1] [7,2] [7,3] [7,4] [7,5] [7,6] [7,7] [7,8]
//        [8,1] [8,2] [8,3] [8,4] [8,5] [8,6] [8,7] [8,8]

class KingMovesCalculator extends PieceMovesCalculator{

    @Override
    public List calculateMoves(ChessBoard board, ChessPosition pos){
        List<ChessMove> moves = new ArrayList<>();
        List<ChessPosition> offset = List.of(new ChessPosition(-1, -1), new ChessPosition(-1, 0), new ChessPosition(-1, 1),
                                             new ChessPosition(0, -1), new ChessPosition(0, 1),
                                             new ChessPosition(1, -1), new ChessPosition(1, 0), new ChessPosition(1, 1));
        for (ChessPosition ofs : offset){
            //calculate possible moves
            int newRow = pos.getRow() + ofs.getRow();
            int newCol = pos.getColumn() + ofs.getColumn();
            //check if its out of bounds
            if (newRow >= 1 && newRow <= 8 && newCol >=1 && newCol <=8){
                ChessPosition np = new ChessPosition(newRow, newCol);
                //check if the space is available
                if (board.getPiece(np) == null || board.getPiece(np).getTeamColor() != board.getPiece(pos).getTeamColor()){
                    moves.add(new ChessMove(pos, np, null));
                }

            }

        }
        return moves;
    }

}

public class QueenMovesCalculator extends PieceMovesCalculator {

    @Override
    public List calculateMoves(ChessBoard board, ChessPosition pos){
        List<ChessMove> moves = new ArrayList<>();

    }

}

//public class BishopMovesCalculator extends PieceMovesCalculator {
//
//    @Override
//    public List calculateMoves(ChessBoard board, ChessPosition pos){
//        List<ChessMove> moves = new ArrayList<>();
//
//    }
//
//}
//
//public class KnightMovesCalculator extends PieceMovesCalculator {
//
//    @Override
//    public List calculateMoves(ChessBoard board, ChessPosition pos){
//        List<ChessMove> moves = new ArrayList<>();
//
//    }
//
//}
//
//public class RookMovesCalculator extends PieceMovesCalculator {
//
//    @Override
//    public List calculateMoves(ChessBoard board, ChessPosition pos){
//        List<ChessMove> moves = new ArrayList<>();
//
//    }
//
//}
//
//public class PawnMovesCalculator extends PieceMovesCalculator {
//
//    @Override
//    public List calculateMoves(ChessBoard board, ChessPosition pos){
//        List<ChessMove> moves = new ArrayList<>();
//
//    }
//
//}