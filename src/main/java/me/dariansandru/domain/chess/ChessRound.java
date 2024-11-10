package me.dariansandru.domain.chess;

import me.dariansandru.domain.validator.exception.ValidatorException;
import me.dariansandru.domain.Player;
import me.dariansandru.domain.validator.Validator;
import me.dariansandru.domain.chess.piece.*;
import me.dariansandru.io.exception.InputException;
import me.dariansandru.utilities.ChessUtils;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static me.dariansandru.utilities.ChessUtils.getLetter;

public class ChessRound {

    private final Player whitePiecesPlayer;
    private final Player blackPiecesPlayer;

    private final Piece[][] pieces = new Piece[8][8];

    public ChessRound(Player whitePiecesPlayer, Player blackPiecesPlayer) throws InputException {
        this.whitePiecesPlayer = whitePiecesPlayer;
        this.blackPiecesPlayer = blackPiecesPlayer;

        resetBoard();
    }

    public Piece getPiece(int row, int col){
        return pieces[row][col];
    }

    public Piece[][] getPieces(){
        return pieces;
    }

    public void resetBoard() throws InputException {
        setPieceLocation();
    }

    private void setPieceLocation() throws InputException {
        this.pieces[0][0] = new Rook(PieceColour.WHITE);
        this.pieces[0][1] = new Knight(PieceColour.WHITE);
        this.pieces[0][2] = new Bishop(PieceColour.WHITE);
        this.pieces[0][3] = new Queen(PieceColour.WHITE);
        this.pieces[0][4] = new King(PieceColour.WHITE);
        this.pieces[0][5] = new Bishop(PieceColour.WHITE);
        this.pieces[0][6] = new Knight(PieceColour.WHITE);
        this.pieces[0][7] = new Rook(PieceColour.WHITE);

        this.pieces[1][0] = new Pawn(PieceColour.WHITE);
        this.pieces[1][1] = new Pawn(PieceColour.WHITE);
        this.pieces[1][2] = new Pawn(PieceColour.WHITE);
        this.pieces[1][3] = new Pawn(PieceColour.WHITE);
        this.pieces[1][4] = new Pawn(PieceColour.WHITE);
        this.pieces[1][5] = new Pawn(PieceColour.WHITE);
        this.pieces[1][6] = new Pawn(PieceColour.WHITE);
        this.pieces[1][7] = new Pawn(PieceColour.WHITE);

        this.pieces[7][0] = new Rook(PieceColour.BLACK);
        this.pieces[7][1] = new Knight(PieceColour.BLACK);
        this.pieces[7][2] = new Bishop(PieceColour.BLACK);
        this.pieces[7][3] = new Queen(PieceColour.BLACK);
        this.pieces[7][4] = new King(PieceColour.BLACK);
        this.pieces[7][5] = new Bishop(PieceColour.BLACK);
        this.pieces[7][6] = new Knight(PieceColour.BLACK);
        this.pieces[7][7] = new Rook(PieceColour.BLACK);

        this.pieces[6][0] = new Pawn(PieceColour.BLACK);
        this.pieces[6][1] = new Pawn(PieceColour.BLACK);
        this.pieces[6][2] = new Pawn(PieceColour.BLACK);
        this.pieces[6][3] = new Pawn(PieceColour.BLACK);
        this.pieces[6][4] = new Pawn(PieceColour.BLACK);
        this.pieces[6][5] = new Pawn(PieceColour.BLACK);
        this.pieces[6][6] = new Pawn(PieceColour.BLACK);
        this.pieces[6][7] = new Pawn(PieceColour.BLACK);

        for (int row = 2 ; row < 6 ; row++){
            for (int col = 0; col < 8 ; col++){
                this.pieces[row][col] = new EmptyPiece();
            }
        }
    }

    public String handleMove(String move, PieceColour colour){
        try{
            if (!Validator.validMoveNotation(move)){
                return "Move is not valid!";
            }

            if (!movePiece(move, colour)){
                return "Move is illegal!";
            }
        }
        catch (ValidatorException | InputException ve){
            return ve.getMessage();
        }

        return null;
    }

    public boolean movePiece(String move, PieceColour pieceColour) throws ValidatorException, InputException {
        int col = ChessUtils.getColRow(move).getValue1();
        int row = ChessUtils.getColRow(move).getValue2();

        String piece;
        if (Validator.validMovePieceNotation(move.charAt(0))){
            if ('a' <= move.charAt(0) && move.charAt(0) <= 'h') piece = "P";
            else piece = String.valueOf(move.charAt(0));
        }
        else piece = "P";

        for (int currentRow = 0 ; currentRow < 8 ; currentRow++){
            for (int currentCol = 0 ; currentCol < 8 ; currentCol++){
                if (Objects.equals(pieces[currentRow][currentCol].getRepresentation(), piece)
                        && pieces[currentRow][currentCol].getColour() == pieceColour
                        && pieces[currentRow][currentCol].isLegalMove(this, currentRow, currentCol, move)
                        && Validator.validateObstruction(this, piece, currentRow, currentCol, row, col)){
                    pieces[currentRow][currentCol] = new EmptyPiece();
                    pieces[row][col] = ChessUtils.getPiece(piece, pieceColour);
                    return true;
                }
            }
        }
        return false;
    }

    //TODO Don't like this idea, will refactor later
    public boolean checkMovePiece(String move, PieceColour pieceColour) throws ValidatorException, InputException {
        int col = ChessUtils.getColRow(move).getValue1();
        int row = ChessUtils.getColRow(move).getValue2();

        String piece;
        if (Validator.validMovePieceNotation(move.charAt(0))){
            if ('a' <= move.charAt(0) && move.charAt(0) <= 'h') piece = "P";
            else piece = String.valueOf(move.charAt(0));
        }
        else piece = "P";

        for (int currentRow = 0 ; currentRow < 8 ; currentRow++){
            for (int currentCol = 0 ; currentCol < 8 ; currentCol++){
                if (Objects.equals(pieces[currentRow][currentCol].getRepresentation(), piece)
                        && pieces[currentRow][currentCol].getColour() == pieceColour
                        && pieces[currentRow][currentCol].isLegalMove(this, currentRow, currentCol, move)
                        && Validator.validateObstruction(this, piece, currentRow, currentCol, row, col)){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isKingChecked(int kingRow, int kingCol, PieceColour pieceColour) throws ValidatorException, InputException {
        PieceColour oppositeColour = (pieceColour == PieceColour.WHITE) ? PieceColour.BLACK : PieceColour.WHITE;

        for (int row = 0 ; row < 8 ; row++){
            for (int col = 0 ; col < 8 ; col++){
                if (!Objects.equals(pieces[row][col].getName(), "None")){
                    String move = pieces[row][col].getRepresentation() + getLetter(kingCol) + kingRow;
                    if (checkMovePiece(move, oppositeColour)) return true;
                }
            }
        }

        return false;
    }

    public Set<String> getKingValidMoves(ChessRound chessRound, PieceColour pieceColour) throws ValidatorException, InputException {
        int kingRow = -1;
        int kingCol = -1;

        Piece[][] pieces = chessRound.getPieces();

        for (int row = 0 ; row < 8 ; row++){
            for (int col = 0 ; col < 8 ; col++){
                if (Objects.equals(pieces[row][col].getName(), "King")
                    && pieces[row][col].getColour() == pieceColour){
                    kingRow = row;
                    kingCol = col;

                }
            }
        }

        Set<String> validKingMoves = new HashSet<>();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newRow = kingRow + j + 1;
                int newCol = kingCol + i;

                if (newCol < 0 || newRow < 0 || newCol > 7 || newRow > 7) continue;

                String move = "K" + ChessUtils.getLetter(newCol) + (newRow + 1);

                if (checkMovePiece(move, pieceColour)) validKingMoves.add(move);
            }
        }

        return validKingMoves;
    }

    public boolean isCheckmate(PieceColour pieceColour) throws ValidatorException, InputException {
        Set<String> kingValidMoves = getKingValidMoves(this, pieceColour);
        if (kingValidMoves.isEmpty()) return false;

        for (String move : kingValidMoves){
            int row = ChessUtils.getColRow(move).getValue2();
            int col = ChessUtils.getColRow(move).getValue1();

            if (!isKingChecked(row, col, pieceColour)) return false;
        }

        return true;
    }

    public boolean isCheckmate() throws ValidatorException, InputException {
        return isCheckmate(PieceColour.WHITE) || isCheckmate(PieceColour.BLACK);
    }

    public boolean isStalemate(){
        return false;
    }

}