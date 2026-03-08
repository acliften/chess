package model;

import chess.ChessBoard;
import chess.ChessGame;

public record ChessGameData(ChessBoard gameBoard, ChessGame.TeamColor turn) {
}
