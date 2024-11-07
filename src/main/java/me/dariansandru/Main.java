package me.dariansandru;

import me.dariansandru.controller.ChessController;
import me.dariansandru.io.InputDevice;
import me.dariansandru.io.OutputDevice;
import me.dariansandru.domain.*;
import me.dariansandru.domain.chess.Manual;
import me.dariansandru.io.exception.InputException;
import me.dariansandru.io.exception.OutputException;
import me.dariansandru.ui.ChessConsoleUI;

import java.util.Objects;

public class Main {

    public static void main(String[] args) throws InputException, OutputException {
        Player p1 = new Player();
        Player p2 = new Player();
        InputDevice inputDevice = new InputDevice();
        OutputDevice outputDevice = new OutputDevice();

        switch (args[0].toLowerCase()) {
            case "play" -> {
                p1.setUsername(args[1]);
                p2.setUsername(args[2]);

                ChessController chessController = new ChessController(p1, p2);
                ChessConsoleUI chessConsoleUI = new ChessConsoleUI(inputDevice, outputDevice, chessController);
                chessConsoleUI.show();
            }
            case "resume" -> {
                ChessController chessController = new ChessController(p1, p2);
                ChessConsoleUI chessConsoleUI = new ChessConsoleUI(inputDevice, outputDevice, chessController);

                chessConsoleUI.showResumedGame();
                chessConsoleUI.show();
            }
            case "rules" -> Manual.showRules(outputDevice);
            default -> throw new IllegalStateException("Could not find command: " + args[0]);
        }
    }
}

//TODO Fix ambiguous moves
//TODO Add en passant and castling
//TODO make validators separate classes
//TODO Create Virtualiser