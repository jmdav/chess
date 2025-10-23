package model;

import chess.ChessGame;

public record GameRequestData(ChessGame.TeamColor playerColor, Integer gameID) {
}
