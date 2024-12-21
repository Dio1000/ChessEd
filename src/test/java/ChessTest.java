import me.dariansandru.controller.ChessController;
import me.dariansandru.domain.Player;
import me.dariansandru.domain.chess.piece.*;
import me.dariansandru.domain.validator.exception.ValidatorException;
import me.dariansandru.io.InputDevice;
import me.dariansandru.io.OutputDevice;
import me.dariansandru.io.exception.InputException;
import me.dariansandru.round.ChessRound;
import me.dariansandru.ui.consoleUI.ChessConsoleUI;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static me.dariansandru.domain.chess.piece.PieceColour.WHITE;
import static me.dariansandru.domain.chess.piece.PieceColour.BLACK;

public class ChessTest {
    @Test
    public void testChess() throws ValidatorException, InputException {
        testRound();
        testController();
        testUI();
        testPiece();
    }

    @Test
    public void testRound() throws InputException, ValidatorException {
        System.out.println("Testing Chess Round:");
        Player whitePiecesPlayer = new Player();
        Player blackPiecesPlayer = new Player();
        ChessRound chessRound = new ChessRound(whitePiecesPlayer, blackPiecesPlayer);

        assert(chessRound.getWhitePiecesPlayer() == whitePiecesPlayer);
        assert(chessRound.getBlackPiecesPlayer() == blackPiecesPlayer);
        System.out.println("Test 1 passed! Players are set correctly.");

        chessRound.setPieceLocation();
        Piece whiteRook = new Rook(WHITE);
        Piece whitePawn = new Pawn(WHITE);
        Piece whiteKing = new King(WHITE);
        assert(Objects.equals(chessRound.getPieces()[0][0].getName(), whiteRook.getName()));
        assert(Objects.equals(chessRound.getPieces()[1][0].getName(), whitePawn.getName()));
        assert(Objects.equals(chessRound.getPieces()[0][4].getName(), whiteKing.getName()));
        System.out.println("Test 2 passed! White pieces are set correctly.");

        Piece blackQueen = new Queen(PieceColour.BLACK);
        Piece blackKnight = new Knight(PieceColour.BLACK);
        Piece blackKing = new King(PieceColour.BLACK);
        assert(Objects.equals(chessRound.getPieces()[7][3].getName(), blackQueen.getName()));
        assert(Objects.equals(chessRound.getPieces()[7][1].getName(), blackKnight.getName()));
        assert(Objects.equals(chessRound.getPieces()[7][4].getName(), blackKing.getName()));
        System.out.println("Test 3 passed! Black pieces are set correctly.");

        assert(chessRound.movePiece("d4", WHITE));
        assert(chessRound.movePiece("d5", PieceColour.BLACK));
        assert(!chessRound.movePiece("Nf4", PieceColour.BLACK));
        assert(!chessRound.movePiece("Qe8", WHITE));
        System.out.println("Test 4 passed! Pieces move correctly from starting positions.");

        chessRound.resetBoard();
        chessRound.movePiece("g3", WHITE);
        assert(chessRound.movePiece("Bg2", WHITE));
        chessRound.movePiece("d5", PieceColour.BLACK);
        assert(chessRound.movePiece("Qd6", PieceColour.BLACK));
        System.out.println("Test 5 passed! Pieces that can be obstructed move correctly.");

        chessRound.resetBoard();
        chessRound.movePiece("e4", WHITE);
        chessRound.movePiece("f5", PieceColour.BLACK);
        chessRound.movePiece("f5", WHITE);
        chessRound.movePiece("e6", PieceColour.BLACK);
        chessRound.movePiece("e6", WHITE);
        chessRound.movePiece("Qe7", PieceColour.BLACK);
        chessRound.movePiece("Be2", WHITE);
        chessRound.movePiece("Qe6", PieceColour.BLACK);
        assert(!chessRound.movePiece("Bd3", WHITE));
        System.out.println("Test 6 passed! Pieces cannot move if they are pinned to the king.");

        chessRound.resetBoard();
        chessRound.movePiece("f3", WHITE);
        chessRound.movePiece("e5", PieceColour.BLACK);
        chessRound.movePiece("g4", WHITE);
        chessRound.movePiece("Qh4", PieceColour.BLACK);
        assert(chessRound.isCheckmate());
        System.out.println("Test 7 passed! Checkmate is set correctly.");
        System.out.println();
    }

    @Test
    public void testController() throws InputException {
        System.out.println("Testing Chess Controller:");
        Player whitePiecesPlayer = new Player();
        Player blackPiecesPlayer = new Player();
        ChessController chessController = new ChessController(whitePiecesPlayer, blackPiecesPlayer);

        assert(chessController.getWhitePiecesPlayer() == whitePiecesPlayer);
        assert(chessController.getBlackPiecesPlayer() == blackPiecesPlayer);
        System.out.println("Test 1 passed! Players are set correctly.");

        chessController.play("d4");
        chessController.play("d5");
        chessController.play("Nf3");
        chessController.play("Nc6");
        assert(Objects.equals(chessController.getLastTurn(), "Nc6"));
        assert(chessController.getTurns().size() == 4);
        assert(Objects.equals(chessController.getTurns().get(0), "d4"));
        assert(Objects.equals(chessController.getTurns().get(1), "d5"));
        assert(Objects.equals(chessController.getTurns().get(2), "Nf3"));
        assert(Objects.equals(chessController.getTurns().get(3), "Nc6"));
        System.out.println("Test 2 passed! Turns are set and fetched correctly.");
        System.out.println();
    }

    @Test
    public void testUI() throws InputException {
        System.out.println("Testing Chess UI:");
        Player whitePiecesPlayer = new Player();
        Player blackPiecesPlayer = new Player();
        ChessController chessController = new ChessController(whitePiecesPlayer, blackPiecesPlayer);

        InputDevice inputDevice = new InputDevice();
        OutputDevice outputDevice = new OutputDevice();
        ChessConsoleUI chessConsoleUI = new ChessConsoleUI(inputDevice, outputDevice, chessController);
        assert(chessConsoleUI.getInputDevice() == inputDevice);
        assert(chessConsoleUI.getOutputDevice() == outputDevice);
        assert(chessConsoleUI.getChessController() == chessController);
        System.out.println("Test 1 passed! InputDevice, OutputDevice and Controller are set correctly.");
        System.out.println();
    }

    @Test
    public void testPiece() throws InputException, ValidatorException {
        System.out.println("Testing Chess Pieces:");
        Player whitePiecesPlayer = new Player();
        Player blackPiecesPlayer = new Player();
        ChessRound chessRound = new ChessRound(whitePiecesPlayer, blackPiecesPlayer);

        Piece whiteRook = new Rook(WHITE);
        Piece blackQueen = new Queen(BLACK);
        Piece whiteKnight = new Knight(WHITE);
        assert(Objects.equals(whiteRook.getName(), "Rook") && whiteRook.getColour() == WHITE);
        assert(Objects.equals(blackQueen.getName(), "Queen") && blackQueen.getColour() == BLACK);
        assert(Objects.equals(whiteKnight.getName(), "Knight") && whiteKnight.getColour() == WHITE);
        assert(whiteRook.getPoints() == 5 && blackQueen.getPoints() == 9 && whiteKnight.getPoints() == 3);
        System.out.println("Test 1 passed! Piece fields are set correctly.");

        Piece whitePawn = new Pawn(WHITE);
        Piece blackPawn = new Pawn(BLACK);
        assert(whitePawn.isLegalMove(chessRound, 1, 0, "a4"));
        assert(!blackPawn.isLegalMove(chessRound, 6, 3, "d7"));
        System.out.println("Test 2 passed! Pawns move correctly from starting positions.");

        chessRound.resetBoard();
        chessRound.movePiece("d4", WHITE);
        chessRound.movePiece("e5", BLACK);
        assert(whitePawn.isLegalMove(chessRound, 3, 3, "e5"));
        System.out.println("Test 3 passed! Pawns capture other pieces correctly.");

        chessRound.resetBoard();
        assert(whiteRook.isLegalMove(chessRound,0,0, "Ra3"));
        assert(!whiteRook.isLegalMove(chessRound,0,0, "Rb7"));
        assert(blackQueen.isLegalMove(chessRound,7,3, "Qf6"));
        assert(!blackQueen.isLegalMove(chessRound,7,3, "Qe6"));
        assert(whiteKnight.isLegalMove(chessRound,0,6, "Nf3"));
        System.out.println("Test 4 passed! Pieces move correctly (assuming no obstructions).");

        chessRound.resetBoard();
        chessRound.movePiece("d5", BLACK);
        assert(chessRound.movePiece("Qd6", BLACK));
        assert(!chessRound.movePiece("Bh3", WHITE));
        System.out.println("Test 5 passed! Pieces move correctly (with obstructions).");
    }
}
