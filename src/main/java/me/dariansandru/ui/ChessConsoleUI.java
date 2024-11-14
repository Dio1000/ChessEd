package me.dariansandru.ui;

import me.dariansandru.controller.ChessController;
import me.dariansandru.domain.chess.PieceColour;
import me.dariansandru.domain.validator.exception.ValidatorException;
import me.dariansandru.io.InputDevice;
import me.dariansandru.io.OutputDevice;
import me.dariansandru.io.exception.InputException;
import me.dariansandru.io.exception.OutputException;
import me.dariansandru.utilities.ChessUtils;

import java.util.List;

public class ChessConsoleUI implements ConsoleUI{

    private final InputDevice inputDevice;
    private final OutputDevice outputDevice;
    private final ChessController chessController;

    public ChessConsoleUI(InputDevice inputDevice, OutputDevice outputDevice, ChessController chessController) {
        this.inputDevice = inputDevice;
        this.outputDevice = outputDevice;
        this.chessController = chessController;
    }

    @Override
    public OutputDevice getOutputDevice() {
        return outputDevice;
    }

    @Override
    public InputDevice getInputDevice() {
        return inputDevice;
    }

    @Override
    public void show() throws InputException, OutputException, ValidatorException {
        String writeFile = "files/chessCurrentGame.txt";
        if (inputDevice.isFileEmpty(writeFile)) {
            outputDevice.appendToFile(chessController.getWhitePiecesPlayer().getUsername(), writeFile);
            outputDevice.appendToFile(chessController.getBlackPiecesPlayer().getUsername(), writeFile);
        }
        else outputDevice.emptyFile(writeFile);

        int turn;
        do{
            turn = chessController.getTurnCount();

            if (turn % 2 == 0) {
                displayBoard();
                outputDevice.write("White to move: ");
            }
            else {
                displayRotatedBoard();
                outputDevice.write("Black to move: ");
            }

            String move = inputDevice.readString();
            String errorMaybe = chessController.play(move);
            if (errorMaybe != null) {
                outputDevice.writeLine(errorMaybe);
            }
            else{
                chessController.addTurn(move);
                outputDevice.appendToFile(chessController.getLastTurn(), writeFile);
            }
        }while(!chessController.isGameFinished());

        if (turn % 2 == 0){
            displayBoard();
            outputDevice.writeLine(chessController.getWhitePiecesPlayer().getUsername() + " won by checkmate!");
        }
        else {
            displayRotatedBoard();
            outputDevice.writeLine(chessController.getBlackPiecesPlayer().getUsername() + " won by checkmate!");
        }

        displayMoves();
        outputDevice.emptyFile(writeFile);
    }

    public void showResumedGame() throws InputException {
        String writeFile = "files/chessCurrentGame.txt";
        List<String> moveList = inputDevice.readFile(writeFile);

        for (String move : moveList){
            chessController.play(move);
            chessController.addTurn(move);
        }
    }

    public void displayMoves(){
        int move = 1;

        List<String> turns = chessController.getTurns();
        for (int index = 0; index < turns.size(); index+=2){
            if (index + 1 >= turns.size()) outputDevice.write(move + "." + turns.get(index) + "\n");
            else outputDevice.write(move + "." + turns.get(index) + " " + turns.get(index + 1) + "\n");
            move++;
        }
    }

    public void displayBoard(){
        outputDevice.writeLine(chessController.getBlackPiecesPlayer().getUsername() + " " +
                ChessUtils.getColourMaterialAdvantage(chessController.getChessRound(), PieceColour.BLACK));

        for (int row = 7 ; row >= 0 ; row--){
            for (int col = 0 ; col < 8 ; col++){
                outputDevice.write(chessController.getPiece(row, col).getDisplay() + " ");
            }
            outputDevice.write("\n");
        }

        outputDevice.writeLine(chessController.getWhitePiecesPlayer().getUsername() + " " +
                ChessUtils.getColourMaterialAdvantage(chessController.getChessRound(), PieceColour.WHITE));
    }

    public void displayRotatedBoard() {
        outputDevice.writeLine(chessController.getWhitePiecesPlayer().getUsername() + " " +
                ChessUtils.getColourMaterialAdvantage(chessController.getChessRound(), PieceColour.WHITE));

        for (int row = 7; row >= 0; row--) {
            for (int col = 0; col < 8; col++) {
                outputDevice.write(chessController.getPiece(7 - row, 7 - col).getDisplay() + " ");
            }
            outputDevice.write("\n");
        }

        outputDevice.writeLine(chessController.getBlackPiecesPlayer().getUsername() + " " +
                ChessUtils.getColourMaterialAdvantage(chessController.getChessRound(), PieceColour.BLACK));
    }
}
