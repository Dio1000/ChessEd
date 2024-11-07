package me.dariansandru.domain.chess.piece;

import me.dariansandru.domain.chess.ChessRound;
import me.dariansandru.domain.chess.Piece;
import me.dariansandru.domain.chess.PieceColour;
import me.dariansandru.io.exception.InputException;
import me.dariansandru.utilities.Utilities;

import java.util.Objects;

public class King implements Piece {

    private final PieceColour colour;
    private String display = "K";
    private final int points = 1000;

    public King(PieceColour colour) throws InputException {
        this.colour = colour;
        setDisplay();
    }

    @Override
    public String getName() {
        return "King";
    }

    @Override
    public String getRepresentation() {
        return "K";
    }

    @Override
    public PieceColour getColour() {
        return colour;
    }

    @Override
    public boolean isLegalMove(ChessRound chessRound, int currentRow, int currentCol, String move) {
        Piece[][] pieces = chessRound.getPieces();

        int newRow = Utilities.getColRow(move).getValue2();
        int newCol = Utilities.getColRow(move).getValue1();

        // Case 0 - Illegal stay move / Illegal friend position
        if (currentRow == newRow && currentCol == newCol) return false;
        if (pieces[newRow][newCol].getColour() == pieces[currentRow][currentCol].getColour()) return false;

        // Case 1 - Move one position
        return (Math.abs(currentCol - newCol) <= 1 && Math.abs(currentRow - newRow) <= 1);
    }

    @Override
    public String getDisplay() {
        return display;
    }

    @Override
    public void setDisplay() throws InputException {
        String display = Utilities.getPieceDisplay(this.getName(), this.getColour());

        if (display.isEmpty() || Objects.equals(display, "Error")) return;
        this.display = Utilities.getPieceDisplay(this.getName(), this.getColour());
    }

    public boolean inCheck(ChessRound chessRound) {
        return true;
    }

    @Override
    public int getPoints() {
        return points;
    }

    @Override
    public int compareTo(Piece piece) {
        return Integer.compare(this.points, piece.getPoints());
    }
}
