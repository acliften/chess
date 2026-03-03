package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;


/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame implements Cloneable{

    private TeamColor turn;
    private ChessBoard gameBoard;

    public ChessGame() {
        turn = TeamColor.WHITE;
        gameBoard = new ChessBoard();
        gameBoard.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        //return a set of all moves that are actually valid in a game
        // make sure its your turn
        // if you are in check then your only leagal moves are too get out of check
        // if its not your turn throw away all the moves, if you are in check throw away all the moves that don't get you out of check

        //start with all valid moves
        // if you determined you are in check
        //clone ur board and try the move on the clone and seeing if you are still in check in that board
        // if valid you keep the move if not throw it away
        if (gameBoard.getPiece(startPosition) == null){
            return null;
        }
        Collection<ChessMove> allMoves = gameBoard.getPiece(startPosition).pieceMoves(gameBoard, startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();
        TeamColor teamTurn = gameBoard.getPiece(startPosition).getTeamColor();
        for (ChessMove move : allMoves){
            ChessGame copy = this.copy();
            copy.forceMove(move);
            if (!copy.isInCheck(teamTurn)){
                validMoves.add(move);
            }
        }
        return validMoves;
    }

    private void forceMove(ChessMove move){
        if (gameBoard.getPiece(move.getStartPosition()) != null){
            gameBoard.getBoard()[move.getEndPosition().getRow()-1][move.getEndPosition().getColumn()-1]
                    = gameBoard.getBoard()[move.getStartPosition().getRow()-1][move.getStartPosition().getColumn()-1];
            gameBoard.getBoard()[move.getStartPosition().getRow()-1][move.getStartPosition().getColumn()-1] = null;
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> vm = validMoves(move.getStartPosition());

        //make move if it's valid
        if (gameBoard.getPiece(move.getStartPosition()) != null
                && vm.contains(move)
                && turn == gameBoard.getPiece(move.getStartPosition()).getTeamColor()){
            //check if it's a pawn promotion else do normal move
            if (gameBoard.getPiece(move.getStartPosition()).getPieceType() == ChessPiece.PieceType.PAWN && move.getPromotionPiece() != null){
                gameBoard.getBoard()[move.getEndPosition().getRow()-1][move.getEndPosition().getColumn()-1]
                        = new ChessPiece(turn, move.getPromotionPiece());
            } else {
                gameBoard.getBoard()[move.getEndPosition().getRow()-1][move.getEndPosition().getColumn()-1]
                        = gameBoard.getBoard()[move.getStartPosition().getRow()-1][move.getStartPosition().getColumn()-1];
            }
            //clear starting position
            gameBoard.getBoard()[move.getStartPosition().getRow()-1][move.getStartPosition().getColumn()-1] = null;
        } else {
            throw new InvalidMoveException();
        }

        //change turn to other team
        if (turn == TeamColor.BLACK){
            setTeamTurn(TeamColor.WHITE);
        } else {
            setTeamTurn(TeamColor.BLACK);
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        //take all the moves your oppoenented can make if anyone of those is the position of your king then you are in check


        ChessPosition kpos = null;
        boolean inCheck = false;
        //find the king
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPiece piece = gameBoard.getPiece(new ChessPosition(i,j));
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor){
                    kpos = new ChessPosition(i,j);
                    break;
                }
            }
        }
        if (kpos == null){
            return false;
        }

        //see if any pieces can take the king
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition pos = new ChessPosition(i,j);
                ChessPiece piece = gameBoard.getPiece(pos);
                if (piece == null){
                    continue;
                }
                if (piece.getTeamColor() != teamColor){
                    Collection<ChessMove> pm = piece.pieceMoves(gameBoard, pos);
                    for (ChessMove move : pm){
                        if (move.getEndPosition().equals(kpos)) {
                            inCheck = true;
                            break;
                        }
                    }
                }
            }
        }
        return inCheck;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        // if is incheck and no valid moves then checkmate
        // if (isincheck() && !validmoves()){}

        if (!isInCheck(teamColor)){
            return false;
        }

        boolean checkmate = true;

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition pos = new ChessPosition(i,j);
                ChessPiece piece = gameBoard.getPiece(pos);
                if (piece == null){
                    continue;
                }
                if (piece.getTeamColor() == teamColor){
                    Collection<ChessMove> pm = validMoves(pos);
                    if (pm != null && !pm.isEmpty()){
                        checkmate = false;
                    }
                }
            }
        }

        return checkmate;

    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        //if you are not in check but you don't have a legal move

        //can't be stalemate if ur in check
        if (isInCheck(teamColor)){
            return false;
        }

        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {

                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = gameBoard.getPiece(pos);

                if (piece == null || piece.getTeamColor() != teamColor) {
                    continue;
                }

                Collection<ChessMove> moves = validMoves(pos);

                if (moves != null && !moves.isEmpty()) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        gameBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
    }

    public ChessGame copy(){
        ChessGame copy = new ChessGame();
        copy.setBoard(this.getBoard().copy());
        copy.setTeamTurn(this.getTeamTurn());
        return copy;
    }


    @Override
    public String toString() {
        return "ChessGame{" +
                "turn=" + turn +
                ", gameBoard=" + gameBoard +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return turn == chessGame.turn && Objects.equals(gameBoard, chessGame.gameBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(turn, gameBoard);
    }
}
