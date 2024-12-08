package me.dariansandru.domain.chess.chessEngine;

import me.dariansandru.domain.chess.piece.Piece;
import me.dariansandru.domain.chess.piece.PieceColour;
import me.dariansandru.domain.validator.exception.ValidatorException;
import me.dariansandru.io.exception.InputException;
import me.dariansandru.round.ChessRound;
import me.dariansandru.utilities.ChessUtils;

public class ChessEngine {
    private static ChessEngineUtils chessEngineUtils = new ChessEngineUtils();
    private static ChessRound chessRound;
    private static Piece[][] pieces;

    public ChessEngine() {}

    public static void setChessEngineUtils(ChessEngineUtils chessEngineUtils) {
        ChessEngine.chessEngineUtils = chessEngineUtils;
    }

    public void setChessRound(ChessRound chessRound) {
        ChessEngine.chessRound = chessRound;
        pieces = chessRound.getPieces();
        chessEngineUtils.setChessRound(chessRound);
    }

    private int evaluateMaterial(PieceColour colour) {
        int playerMaterialAdvantage = ChessUtils.getColourMaterialAdvantage(chessRound, colour);
        return (colour == PieceColour.WHITE) ? playerMaterialAdvantage : -playerMaterialAdvantage;
    }

    private int evaluateAvailableCaptures(PieceColour colour) {
        int totalPlayer = 0;
        int totalOpponent = 0;
        int total;
        int weight = 3;

        for (int row = 0 ; row < 8 ; row++){
            for (int col = 0 ; col < 8 ; col++){
                if (pieces[row][col].getColour() == colour)
                    totalPlayer += chessEngineUtils.numberOfPossibleCaptures(pieces[row][col], row, col);
                else if (pieces[row][col].getColour() != colour)
                    totalOpponent += chessEngineUtils.numberOfPossibleCaptures(pieces[row][col], row, col);
            }
        }
        total = totalPlayer - totalOpponent;

        return (colour == PieceColour.WHITE) ? total * weight : -total * weight;
    }

    private int evaluateKingSafety(PieceColour colour) throws ValidatorException, InputException {
        if (chessEngineUtils.isKingAttacked(colour) && colour == PieceColour.WHITE) return -10000;
        else if (chessEngineUtils.isKingAttacked(colour) && colour == PieceColour.BLACK) return 10000;

        int safetyScore = 0;
        int kingRow = chessEngineUtils.getKingLocation(colour).getValue1();
        int kingCol = chessEngineUtils.getKingLocation(colour).getValue2();
        int weight = 2;

        if (chessEngineUtils.isOpenFile(kingCol)) safetyScore += 4 * weight;
        if (kingRow > 0 && chessEngineUtils.isOpenFile(kingRow - 1)) safetyScore += 2 * weight;
        if (kingRow < 7 && chessEngineUtils.isOpenFile(kingRow + 1)) safetyScore += 2 * weight;

        return (colour == PieceColour.WHITE) ? safetyScore * weight : -safetyScore * weight;
    }

    private int evaluatePieceActivity(PieceColour colour) {
        int score = 0;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = pieces[row][col];

                if (piece.getColour() == colour) {
                    if ((row == 3 || row == 4) && (col == 3 || col == 4)) {
                        score += 5;
                    }
                    if (piece.getName().equals("Rook") && chessEngineUtils.isOpenFile(col)) {
                        score += 10;
                    }
                }
            }
        }

        return (colour == PieceColour.WHITE) ? score : -score;
    }

    private int evaluatePawnStructure(PieceColour colour) {
        int penalty = 0;

        for (int col = 0; col < 8; col++) {
            boolean foundPawn = false;
            boolean isDoubled = false;

            for (int row = 0; row < 8; row++) {
                Piece piece = pieces[row][col];
                if (piece.getName().equals("Pawn") && piece.getColour() == colour) {
                    if (foundPawn) {
                        isDoubled = true;
                    }
                    foundPawn = true;

                    if (col > 0 && !chessEngineUtils.isOpenFile(col - 1)) {
                        penalty += 10;
                    }
                    if (col < 7 && !chessEngineUtils.isOpenFile(col + 1)) {
                        penalty += 10;
                    }
                }
            }

            if (isDoubled) {
                penalty += 15;
            }
        }
        return (colour == PieceColour.WHITE) ? -penalty : penalty;
    }

    private int evaluateMobility(PieceColour colour) {
        int totalMoves = 0;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = pieces[row][col];
                if (piece.getColour() == colour) {
                    totalMoves += chessEngineUtils.numberOfPossibleMoves(piece, row, col);
                }
            }
        }
        int weight = 1;
        return (colour == PieceColour.WHITE) ? totalMoves * weight : -totalMoves * weight;
    }

    public int evaluatePosition(PieceColour colour) throws ValidatorException, InputException {
        if (chessRound.isCheckmate(colour) && colour == PieceColour.WHITE) return -10000;
        else if (chessRound.isCheckmate(colour) && colour == PieceColour.BLACK) return 10000;

        int materialScore = evaluateMaterial(colour);
        int captureScore = evaluateAvailableCaptures(colour);
        int kingSafetyScore = evaluateKingSafety(colour);
        int mobilityScore = evaluateMobility(colour);
        int pawnStructureScore = evaluatePawnStructure(colour);
        int pieceActivityScore = evaluatePieceActivity(colour);

        return materialScore + captureScore + kingSafetyScore
                + mobilityScore + pawnStructureScore + pieceActivityScore;
    }

}
