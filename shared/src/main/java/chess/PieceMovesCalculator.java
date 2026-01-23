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
        for (ChessPosition dir : offset){
            //calculate possible moves
            int newRow = pos.getRow() + dir.getRow();
            int newCol = pos.getColumn() + dir.getColumn();
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

class QueenMovesCalculator extends PieceMovesCalculator {

    @Override
    public List calculateMoves(ChessBoard board, ChessPosition pos){
        List<ChessMove> moves = new ArrayList<>();
        List<ChessPosition> offset = List.of(new ChessPosition(-1, -1), new ChessPosition(-1, 0), new ChessPosition(-1, 1),
                                             new ChessPosition(0, -1), new ChessPosition(0, 1),
                                             new ChessPosition(1, -1), new ChessPosition(1, 0), new ChessPosition(1, 1));

        for (ChessPosition dir : offset){
            int newRow = pos.getRow();
            int newCol = pos.getColumn();
            while (true){
                newRow += dir.getRow();
                newCol += dir.getColumn();
                if (newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8){
                    break;
                }
                ChessPosition np = new ChessPosition(newRow, newCol);
                //check if the space is available
                if (board.getPiece(np) == null){
                    moves.add(new ChessMove(pos, np, null));
                } else {
                    if (board.getPiece(np).getTeamColor() != board.getPiece(pos).getTeamColor()){
                        moves.add(new ChessMove(pos, np, null));
                    }
                    break;
                }
            }
        }

        return moves;
    }

}

class BishopMovesCalculator extends PieceMovesCalculator {

    @Override
    public List calculateMoves(ChessBoard board, ChessPosition pos){
        List<ChessMove> moves = new ArrayList<>();
        List<ChessPosition> offset = List.of(new ChessPosition(1, 1), new ChessPosition(1, -1), new ChessPosition(-1, 1), new ChessPosition(-1, -1));

        for (ChessPosition dir : offset){
            int newRow = pos.getRow();
            int newCol = pos.getColumn();
            while (true){
                newRow += dir.getRow();
                newCol += dir.getColumn();
                if (newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8){
                    break;
                }
                ChessPosition np = new ChessPosition(newRow, newCol);
                //check if the space is available
                if (board.getPiece(np) == null){
                    moves.add(new ChessMove(pos, np, null));
                } else {
                    if (board.getPiece(np).getTeamColor() != board.getPiece(pos).getTeamColor()){
                        moves.add(new ChessMove(pos, np, null));
                    }
                    break;
                }
            }
        }

        return moves;
    }

}

class KnightMovesCalculator extends PieceMovesCalculator {

    @Override
    public List calculateMoves(ChessBoard board, ChessPosition pos){
        List<ChessMove> moves = new ArrayList<>();

        List<ChessPosition> offset = List.of(new ChessPosition(2, 1), new ChessPosition(2, -1), new ChessPosition(-2, 1), new ChessPosition(-2, -1),
                                             new ChessPosition(1, 2), new ChessPosition(-1, 2), new ChessPosition(1, -2),new ChessPosition(-1, -2));
        for (ChessPosition dir : offset){
            //calculate possible moves
            int newRow = pos.getRow() + dir.getRow();
            int newCol = pos.getColumn() + dir.getColumn();
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

class RookMovesCalculator extends PieceMovesCalculator {

    @Override
    public List calculateMoves(ChessBoard board, ChessPosition pos){
        List<ChessMove> moves = new ArrayList<>();

        List<ChessPosition> offset = List.of(new ChessPosition(-1, 0), new ChessPosition(0, 1), new ChessPosition(0, -1), new ChessPosition(1, 0));

        for (ChessPosition dir : offset){
            int newRow = pos.getRow();
            int newCol = pos.getColumn();
            while (true){
                newRow += dir.getRow();
                newCol += dir.getColumn();
                if (newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8){
                    break;
                }
                    ChessPosition np = new ChessPosition(newRow, newCol);
                    //check if the space is available
                    if (board.getPiece(np) == null){
                        moves.add(new ChessMove(pos, np, null));
                    } else {
                        if (board.getPiece(np).getTeamColor() != board.getPiece(pos).getTeamColor()){
                            moves.add(new ChessMove(pos, np, null));
                        }
                        break;
                    }
            }
        }

        return moves;
    }

}

class PawnMovesCalculator extends PieceMovesCalculator {

    @Override
    public List calculateMoves(ChessBoard board, ChessPosition pos){
        List<ChessMove> moves = new ArrayList<>();
        int colorOffset;
        int row = pos.getRow();
        int col = pos.getColumn();

        //find out which color while also handling 2 space first move
        if (board.getPiece(pos).getTeamColor() == ChessGame.TeamColor.BLACK){
            colorOffset = -1;
            if (row == 7 && board.getPiece(new ChessPosition(row + colorOffset, col)) == null && board.getPiece(new ChessPosition(row -2, col)) == null){
                moves.add(new ChessMove(pos, new ChessPosition(row - 2, col), null));
            }
        } else {
            colorOffset = 1;
            if (row == 2 && board.getPiece(new ChessPosition(row + colorOffset, col)) == null && board.getPiece(new ChessPosition(row + 2, col)) == null){
                moves.add(new ChessMove(pos, new ChessPosition(row + 2, col), null));
            }
        }

        //foward
        if (board.getPiece(new ChessPosition(row + colorOffset, col)) == null && row +colorOffset < 8 && row + colorOffset > 1){
            moves.add(new ChessMove(pos, new ChessPosition(row + colorOffset, col), null));
        }

        //diagonal take
        if (col + 1 <= 8 && board.getPiece(new ChessPosition(row + colorOffset, col + 1)) != null && board.getPiece(new ChessPosition(row + colorOffset, col + 1)).getTeamColor() != board.getPiece(pos).getTeamColor()){
            if (row + colorOffset == 8 || row + colorOffset == 1){
                moves.add(new ChessMove(pos, new ChessPosition(row + colorOffset, col + 1), ChessPiece.PieceType.QUEEN));
                moves.add(new ChessMove(pos, new ChessPosition(row + colorOffset, col + 1), ChessPiece.PieceType.ROOK));
                moves.add(new ChessMove(pos, new ChessPosition(row + colorOffset, col + 1), ChessPiece.PieceType.KNIGHT));
                moves.add(new ChessMove(pos, new ChessPosition(row + colorOffset, col + 1), ChessPiece.PieceType.BISHOP));
            } else {
                moves.add(new ChessMove(pos, new ChessPosition(row + colorOffset, col + 1), null));
            }
        } else if (col - 1 >= 1 && board.getPiece(new ChessPosition(row + colorOffset, col - 1)) != null && board.getPiece(new ChessPosition(row + colorOffset, col - 1)).getTeamColor() != board.getPiece(pos).getTeamColor()){
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