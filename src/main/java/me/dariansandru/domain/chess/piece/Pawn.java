package me.dariansandru.domain.chess.piece;

import me.dariansandru.domain.chess.ChessRound;
import me.dariansandru.domain.chess.Piece;
import me.dariansandru.domain.chess.PieceColour;
import me.dariansandru.io.exception.InputException;
import me.dariansandru.utilities.Utilities;

import java.util.Objects;

public class Pawn implements Piece {

    private final PieceColour colour;
    private String display = "P";
    private final int points = 1;

    public Pawn(PieceColour colour) throws InputException {
        this.colour = colour;
        setDisplay();
    }

    @Override
    public String getName() {
        return "Pawn";
    }

    @Override
    public String getRepresentation() {
        return "P";
    }

    @Override
    public PieceColour getColour() {
        return colour;
    }

    @Override
    public boolean isLegalMove(ChessRound chessRound, int currentRow, int currentCol, String move){
        Piece[][] pieces = chessRound.getPieces();

        int newRow = Utilities.getColRow(move).getValue2();
        int newCol = Utilities.getColRow(move).getValue1();

        // Case 0 - Illegal stay move
        if (currentRow == newRow && currentCol == newCol) return false;

        // White pieces
        if (this.colour == PieceColour.WHITE){
            // Case 1 - Move from row 1
            if (currentRow == 1){
                // Case 1.1 - Move forward 1 or 2 spaces
                if (currentCol == newCol) return newRow <= 3;
                // Case 1.2 - Takes another piece
                if (currentCol + 1 == newCol)
                    return Utilities.isValidPiece(pieces[currentRow - 1][currentCol + 1].getRepresentation());
                else if (currentCol - 1 == newCol)
                    return Utilities.isValidPiece(pieces[currentRow - 1][currentCol - 1].getRepresentation());
                else return false;
            }

            // Case 2 - Move from another row
            // Case 2.1 - Move forward 1 space
            if (currentCol == newCol) return newRow - currentRow == 1;

            // Case 2.2 - Takes another piece
            if (currentRow + 1 == newRow){
                if (currentCol + 1 == newCol && Utilities.isValidPiece(pieces[currentRow + 1][currentCol + 1].getRepresentation())) return true;
                else return currentCol - 1 == newCol && Utilities.isValidPiece(pieces[currentRow + 1][currentCol - 1].getRepresentation());
            }
        }

        // Black pieces
        if (this.colour == PieceColour.BLACK){
            // Case 1 - Move from row 6
            if (currentRow == 6){
                // Case 1.1 - Move forward 1 or 2 spaces
                if (currentCol == newCol) return newRow >= 4;
                // Case 1.2 - Takes another piece
                if (currentCol + 1 == newCol)
                    return Utilities.isValidPiece(pieces[currentRow][currentCol + 1].getRepresentation());
                else if (currentCol - 1 == newCol)
                    return Utilities.isValidPiece(pieces[currentRow][currentCol + 1].getRepresentation());
                else return false;
            }

            // Case 2 - Move from another row
            // Case 2.1 - Move forward 1 space
            if (currentCol == newCol) return currentRow - newRow == 1;

            // Case 2.2 - Takes another piece
            if (currentRow - 1 == newRow){
                if (currentCol + 1 == newCol && Utilities.isValidPiece(pieces[currentRow][currentCol + 1].getRepresentation())) return true;
                else return currentCol - 1 == newCol && Utilities.isValidPiece(pieces[currentRow][currentCol + 1].getRepresentation());
            }
        }
        
        return false;
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

    @Override
    public int getPoints() {
        return points;
    }

    @Override
    public int compareTo(Piece piece) {
        return Integer.compare(this.points, piece.getPoints());
    }
}
