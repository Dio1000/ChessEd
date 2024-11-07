package me.dariansandru.controller;

import me.dariansandru.domain.Player;
import me.dariansandru.domain.chess.Piece;
import me.dariansandru.domain.chess.PieceColour;
import me.dariansandru.io.OutputDevice;
import me.dariansandru.domain.chess.ChessRound;
import me.dariansandru.io.exception.InputException;

import java.util.ArrayList;
import java.util.List;

public class ChessController implements GameController {

    private int turn;
    private final List<String> turns;
    private final ChessRound chessRound;

    // TODO Review code further, variables are also in chessRound, breaks layered architecture
    // TODO In case of removal, don't forget about getters
    private final Player whitePiecesPlayer;
    private final Player blackPiecesPlayer;

    public ChessController(Player p1, Player p2) throws InputException {
        turns = new ArrayList<>();
        this.chessRound = new ChessRound(p1, p2);
        reset();

        whitePiecesPlayer = p1;
        blackPiecesPlayer = p2;
    }

    public List<String> getTurns(){
        return this.turns;
    }

    public Piece getPiece(int row, int col){
        return chessRound.getPiece(row, col);
    }

    @Override
    public void reset() throws InputException {
        chessRound.resetBoard();
        turn = 0;
        turns.clear();
    }

    public int getTurnCount(){
        return turn;
    }

    @Override
    public String play(String move){
        PieceColour colour;

        if (turn % 2 == 0) {
            colour = PieceColour.WHITE;
        }
        else {
            colour = PieceColour.BLACK;
        }

        String errorMaybe = chessRound.handleMove(move, colour);
        if (errorMaybe == null) turn++;
        return errorMaybe;
    }

    public void addTurn(String move){
        turns.add(move);
    }

    public boolean isGameFinished(){
        return chessRound.isCheckmate() || chessRound.isStalemate();
    }

    public Player getWhitePiecesPlayer(){
        return whitePiecesPlayer;
    }

    public Player getBlackPiecesPlayer(){
        return blackPiecesPlayer;
    }

    public ChessRound getChessRound(){
        return chessRound;
    }

}
