package me.dariansandru.ui.guiController;

import me.dariansandru.controller.ChessController;
import me.dariansandru.domain.chess.PieceColour;
import me.dariansandru.domain.validator.exception.ValidatorException;
import me.dariansandru.io.exception.InputException;
import me.dariansandru.round.ChessRound;
import me.dariansandru.ui.consoleUI.ChessConsoleUI;
import me.dariansandru.ui.gui.ChessGUI;

import javax.swing.*;
import java.awt.*;

public class GUIController {
    private final ChessConsoleUI chessConsoleUI;
    private final ChessController chessController;
    private final ChessRound chessRound;
    private int turnCounter;

    public GUIController(ChessConsoleUI chessConsoleUI)
    {
        this.chessConsoleUI = chessConsoleUI;
        this.chessController = this.chessConsoleUI.getChessController();
        this.chessRound = chessController.getChessRound();
    }

    public void run() {
        JFrame frame = new JFrame("Chess GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);

        ChessGUI chessPanel = new ChessGUI(chessRound);

        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.LIGHT_GRAY);
        rightPanel.setLayout(new BorderLayout());

        JTextField moveInput = new JTextField();
        JButton submitButton = new JButton("Submit");
        JLabel instructionLabel = new JLabel("Insert move:");
        JLabel error = new JLabel();

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 1, 5, 5));
        inputPanel.add(instructionLabel);
        inputPanel.add(moveInput);
        inputPanel.add(submitButton);
        inputPanel.add(error);

        rightPanel.add(inputPanel, BorderLayout.NORTH);

        submitButton.addActionListener(e -> {
            try {
                String move = moveInput.getText();
                PieceColour colour = (turnCounter % 2 == 0) ? PieceColour.WHITE : PieceColour.BLACK;
                while (!chessRound.movePiece(move, colour)){
                    error.setText("Invalid move");

                    moveInput.setText("");
                    move = moveInput.getText();
                }

                chessPanel.drawBoard();
                turnCounter++;

                moveInput.setText("");
                error.setText("");
            } catch (ValidatorException | InputException ex) {
                throw new RuntimeException(ex);
            }
        });

        frame.setLayout(new BorderLayout());
        frame.add(chessPanel, BorderLayout.WEST);
        frame.add(rightPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }
}
