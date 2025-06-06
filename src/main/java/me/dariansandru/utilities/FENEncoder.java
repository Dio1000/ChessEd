package me.dariansandru.utilities;

import me.dariansandru.domain.chess.piece.Piece;
import me.dariansandru.domain.chess.piece.PieceColour;
import me.dariansandru.domain.validator.exception.ValidatorException;
import me.dariansandru.io.exception.InputException;
import me.dariansandru.round.ChessRound;

public abstract class FENEncoder {

    /**
     * Utility class to encode a position from a given chess round to Forsyth–Edwards Notation.
     * @param chessRound Instance of a ChessRound with the given game.
     * @param currentTurn Colour of the player who is playing their move.
     * @return Forsyth–Edwards Notation for the given game.
     * @throws ValidatorException thrown when the move validation fails.
     * @throws InputException thrown when the input validation fails.
     */
    public static String encode(ChessRound chessRound, PieceColour currentTurn) throws ValidatorException, InputException {
        Piece[][] pieces = chessRound.getPieces();
        String currentColour = (currentTurn == PieceColour.WHITE) ? "w" : "b";
        StringBuilder encodingBuilder = new StringBuilder();

        String whiteCastleKingSide = chessRound.hasKingSideCastlePrivilege(PieceColour.WHITE) ? "K" : "";
        String whiteCastleQueenSide = chessRound.hasQueenSideCastlePrivilege(PieceColour.WHITE) ? "Q" : "";
        String blackCastleKingSide = chessRound.hasKingSideCastlePrivilege(PieceColour.BLACK) ? "k" : "";
        String blackCastleQueenSide = chessRound.hasQueenSideCastlePrivilege(PieceColour.BLACK) ? "q" : "";

        for (int row = 7 ; row >= 0 ; row--) {
            int emptyPieces = 0;
            for (int col = 0 ; col < 8 ; col++) {
                String piece = (pieces[row][col].getColour() == PieceColour.WHITE) ?
                        pieces[row][col].getRepresentation() : pieces[row][col].getRepresentation().toLowerCase();
                if (piece.equals("x")) emptyPieces++;
                else {
                    if (emptyPieces != 0) encodingBuilder.append(emptyPieces);
                    emptyPieces = 0;
                    encodingBuilder.append(piece);
                }
            }

            if (emptyPieces != 0) encodingBuilder.append(emptyPieces);
            if (row != 0) encodingBuilder.append("/");
        }

        encodingBuilder.append(" ").append(currentColour).append(" ");
        if (whiteCastleKingSide.isEmpty() && whiteCastleQueenSide.isEmpty() &&
            blackCastleKingSide.isEmpty() && blackCastleQueenSide.isEmpty())
            encodingBuilder.append(" - ");
        else {
            encodingBuilder.append(whiteCastleKingSide).append(whiteCastleQueenSide)
                    .append(blackCastleKingSide).append(blackCastleQueenSide);
        }

        return encodingBuilder.toString();
    }
}
