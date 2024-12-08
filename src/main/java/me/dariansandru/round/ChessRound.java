package me.dariansandru.round;

import me.dariansandru.domain.chess.chessEngine.ChessEngine;
import me.dariansandru.domain.chess.piece.Piece;
import me.dariansandru.domain.chess.piece.PieceColour;
import me.dariansandru.domain.validator.exception.ValidatorException;
import me.dariansandru.domain.Player;
import me.dariansandru.domain.validator.ChessValidator;
import me.dariansandru.domain.chess.piece.*;
import me.dariansandru.io.exception.InputException;
import me.dariansandru.utilities.ChessUtils;
import me.dariansandru.utilities.Pair;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import static me.dariansandru.utilities.ChessUtils.*;

/**
 * Using this object allows the user to set up a round of Chess.
 */
public class ChessRound implements GameRound{

    private final Player whitePiecesPlayer;
    private final Player blackPiecesPlayer;
    private final ChessEngine chessEngine = new ChessEngine();

    private final Piece[][] pieces = new Piece[8][8];

    public ChessRound(Player whitePiecesPlayer, Player blackPiecesPlayer) throws InputException {
        this.whitePiecesPlayer = whitePiecesPlayer;
        this.blackPiecesPlayer = blackPiecesPlayer;
        chessEngine.setChessRound(this);

        resetBoard();
    }

    /**
     * Resets the board of the Chess round.
     * @throws InputException Thrown when input validation fails.
     */
    @Override
    public void resetBoard() throws InputException {
        setPieceLocation();
    }

    /**
     * Sets the location of the pieces on the Chess board.
     * @throws InputException Thrown when input validation fails.
     */
    @Override
    public void setPieceLocation() throws InputException {
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

    /**
     * Gets the piece on a certain row and column.
     * @param row Row of the piece.
     * @param col Column of the piece.
     * @return Piece object on the specified square.
     */
    public Piece getPiece(int row, int col){
        return pieces[row][col];
    }

    /**
     * Gets the Chess board that is currently being played on.
     * @return Piece matrix representing the board.
     */
    public Piece[][] getPieces(){
        return pieces;
    }

    /**
     * Validates a move of a piece of a certain colour.
     * @param move Move that is played.
     * @param colour Colour of the piece.
     * @return Error string if validation failed, null otherwise.
     */
    public String handleMove(String move, PieceColour colour){
        try{
            if (!ChessValidator.validMoveNotation(move)){
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

    /**
     * Moves a piece on a certain square given by a move String.
     * @param move Move that is being played.
     * @param pieceColour Colour of the piece.
     * @return True is the move can was played, false otherwise.
     * @throws ValidatorException Thrown when the validator fails.
     * @throws InputException Thrown when the input validation fails.
     */
    public boolean movePiece(String move, PieceColour pieceColour) throws ValidatorException, InputException {
        int col = ChessUtils.getColRow(move).getValue1();
        int row = ChessUtils.getColRow(move).getValue2();

        if (col < 0 || row < 0) return false;

        String piece;
        if (ChessValidator.validMovePieceNotation(move.charAt(0))){
            if ('a' <= move.charAt(0) && move.charAt(0) <= 'h') piece = "P";
            else piece = String.valueOf(move.charAt(0));
        }
        else piece = "P";

        for (int currentRow = 0 ; currentRow < 8 ; currentRow++){
            for (int currentCol = 0 ; currentCol < 8 ; currentCol++){
                if (Objects.equals(pieces[currentRow][currentCol].getRepresentation(), piece)
                        && pieces[currentRow][currentCol].getColour() == pieceColour
                        && pieces[currentRow][currentCol].isLegalMove(this, currentRow, currentCol, move)
                        && ChessValidator.validateObstruction(this, piece, currentRow, currentCol, row, col)){
                    pieces[currentRow][currentCol] = new EmptyPiece();
                    pieces[row][col] = ChessUtils.getPiece(piece, pieceColour);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if a move can be played.
     * @param move Move that is played.
     * @param pieceColour Colour of the piece.
     * @return True if the move can be played, false otherwise.
     * @throws ValidatorException Thrown if the validation fails.
     */
    public boolean checkMovePiece(String move, PieceColour pieceColour) throws ValidatorException {
        int col = ChessUtils.getColRow(move).getValue1();
        int row = ChessUtils.getColRow(move).getValue2();

        if (row < 0 || col < 0) return false;

        String piece;
        if (ChessValidator.validMovePieceNotation(move.charAt(0))){
            if ('a' <= move.charAt(0) && move.charAt(0) <= 'h') piece = "P";
            else piece = String.valueOf(move.charAt(0));
        }
        else piece = "P";

        AtomicBoolean OK = new AtomicBoolean(false);
        var threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        var latch = new CountDownLatch(8);

        for (int currentRow = 0 ; currentRow < 8 ; currentRow++){
            int finalCurrentRow = currentRow;
            threadPool.execute(() -> {
                for (int currentCol = 0 ; currentCol < 8 ; currentCol++){
                    if (Objects.equals(pieces[finalCurrentRow][currentCol].getRepresentation(), piece)
                            && pieces[finalCurrentRow][currentCol].getColour() == pieceColour
                            && pieces[finalCurrentRow][currentCol].isLegalMove(this, finalCurrentRow, currentCol, move)
                            && ChessValidator.validateObstruction(this, piece, finalCurrentRow, currentCol, row, col)){
                        OK.set(true);
                        latch.countDown();
                        return;
                    }
                }
                latch.countDown();
            });
        }

        try{
            latch.await();
        }
        catch (InterruptedException e){
            throw new RuntimeException(e);
        }
        threadPool.shutdownNow();

        return OK.get();
    }

    public Pair<Integer, Integer> getStartLocation(String move, PieceColour pieceColour) throws ValidatorException, InputException {
        int col = ChessUtils.getColRow(move).getValue1();
        int row = ChessUtils.getColRow(move).getValue2();

        String piece;
        if (ChessValidator.validMovePieceNotation(move.charAt(0))){
            if ('a' <= move.charAt(0) && move.charAt(0) <= 'h') piece = "P";
            else piece = String.valueOf(move.charAt(0));
        }
        else piece = "P";

        for (int currentRow = 0 ; currentRow < 8 ; currentRow++){
            for (int currentCol = 0 ; currentCol < 8 ; currentCol++){
                if (Objects.equals(pieces[currentRow][currentCol].getRepresentation(), piece)
                        && pieces[currentRow][currentCol].getColour() == pieceColour
                        && pieces[currentRow][currentCol].isLegalMove(this, currentRow, currentCol, move)
                        && ChessValidator.validateObstruction(this, piece, currentRow, currentCol, row, col)){
                    return new Pair<>(currentRow, currentCol);
                }
            }
        }
        return new Pair<>(-1, -1);
    }

    /**
     * Checks if the king is checked (can be seen by enemy piece).
     * @param kingRow Row the king is on.
     * @param kingCol Column the king is on.
     * @param pieceColour Colour of the king.
     * @return True if the king is checked, false otherwise.
     * @throws ValidatorException Thrown if the validation fails.
     * @throws InputException Thrown if the input validator fails.
     */
    public boolean isKingChecked(int kingRow, int kingCol, PieceColour pieceColour) throws ValidatorException, InputException {
        PieceColour oppositeColour = (pieceColour == PieceColour.WHITE) ? PieceColour.BLACK : PieceColour.WHITE;
        if (kingCol < 0 || kingRow < 0) return false;

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

    /**
     * Gets the valid move of a king of a certain colour.
     * @param chessRound ChessRound that the game is being played on.
     * @param pieceColour Colour of the king.
     * @return Set of strings representing the moves.
     * @throws ValidatorException Thrown if the validator fails.
     * @throws InputException Thrown when input validation fails.
     */
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

        if (kingRow == 0 || kingCol == 0) return validKingMoves;

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

    /**
     * Checks if a game ended in checkmate by checking if one king is checkmated.
     * @param pieceColour Colour of the king that is being checkmated.
     * @return True if the king is checkmated, false otherwise.
     * @throws ValidatorException Thrown if the validator fails.
     * @throws InputException Thrown if the input validation fails.
     */
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

    /**
     * Checks if a game ended in checkmate by checking if either king is checkmated.
     * @return True if the game is in checkmate, false otherwise.
     */
    public boolean isCheckmate() throws ValidatorException, InputException {
        return isCheckmate(PieceColour.WHITE) || isCheckmate(PieceColour.BLACK);
    }

    /**
     * Checks if a given colour king is stalemated.
     * @param pieceColour Colour of the king that is checked.
     * @return True if given king is stalemated, false otherwise.
     * @throws ValidatorException Thrown if validator fails.
     * @throws InputException Thrown if input validation fails.
     */
    public boolean isStalemate(PieceColour pieceColour) throws ValidatorException, InputException {
        return false;
    }

    /**
     * Checks if a game ended in stalemate.
     * @return True if the game is in stalemate, false otherwise.
     */
    public boolean isStalemate() throws ValidatorException, InputException {
        return isStalemate(PieceColour.WHITE) || isStalemate(PieceColour.BLACK);
    }

    public int computeAdvantage() throws ValidatorException, InputException {
        int whitePiecesPlayerScore = chessEngine.evaluatePosition(PieceColour.WHITE);
        int blackPiecesPlayerScore = -chessEngine.evaluatePosition(PieceColour.BLACK);

        return whitePiecesPlayerScore - blackPiecesPlayerScore;
    }

}