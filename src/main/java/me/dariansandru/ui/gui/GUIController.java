package me.dariansandru.ui.gui;

import me.dariansandru.controller.ChessController;
import me.dariansandru.domain.chess.piece.PieceColour;
import me.dariansandru.domain.validator.exception.ValidatorException;
import me.dariansandru.io.exception.InputException;
import me.dariansandru.round.ChessRound;
import me.dariansandru.ui.consoleUI.ChessConsoleUI;

import javax.swing.*;
import java.awt.*;

/**
 * Using this object allows the user to control the GUI of a Chess game.
 */
public class GUIController {
    private final ChessRound chessRound;
    private int turnCounter;

    // Instance variables for components
    private ChessGUI chessPanel;
    private JTextField moveInput;
    private JLabel instructionLabel;
    private JLabel error;
    private JLabel advantageLabel;

    public GUIController(ChessConsoleUI chessConsoleUI) {
        ChessController chessController = chessConsoleUI.getChessController();
        this.chessRound = chessController.getChessRound();
    }

    /**
     * Runs the logic of a Chess game.
     */
    public void run() {
        JFrame frame = new JFrame("Chess GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);

        chessPanel = new ChessGUI(chessRound);
        moveInput = new JTextField();
        instructionLabel = new JLabel("White to move:");
        error = new JLabel();
        advantageLabel = new JLabel("Advantage: 0");

        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.LIGHT_GRAY);
        rightPanel.setLayout(new BorderLayout());

        JButton submitButton = new JButton("Submit");
        JButton resetButton = new JButton("Reset");

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(6, 1, 10, 10));
        inputPanel.add(instructionLabel);
        inputPanel.add(moveInput);
        inputPanel.add(submitButton);
        inputPanel.add(error);
        inputPanel.add(advantageLabel);
        inputPanel.add(resetButton);

        rightPanel.add(inputPanel, BorderLayout.NORTH);

        resetButton.addActionListener(e -> resetGame());

        submitButton.addActionListener(e -> {
            try {
                String move = moveInput.getText();
                PieceColour colour = (turnCounter % 2 == 0) ? PieceColour.WHITE : PieceColour.BLACK;

                if (!chessRound.checkMovePiece(move, colour)) {
                    error.setText("Invalid move. Please try again.");
                    return;
                }

                chessPanel.updateBoard(move, colour);
                chessRound.movePiece(move, colour);

                turnCounter++;
                moveInput.setText("");
                error.setText("");
                instructionLabel.setText((colour == PieceColour.WHITE) ? "Black to move:" : "White to move:");

                float advantage = chessRound.computeAdvantage();
                String advantageText = (advantage > 0) ? "White +" + advantage : (advantage < 0) ? "Black " + advantage : "0";
                advantageLabel.setText("Advantage: " + advantageText);

                if (chessRound.isCheckmate()) {
                    String winner = (turnCounter % 2 == 0) ? "Black" : "White";
                    JOptionPane.showMessageDialog(frame, winner + " won by checkmate!", "Game Over", JOptionPane.INFORMATION_MESSAGE);

                    resetGame();
                }

            } catch (ValidatorException | InputException ex) {
                error.setText("An error occurred: " + ex.getMessage());
            } catch (Exception ex) {
                error.setText("Unexpected error: " + ex.getMessage());
            }
        });

        frame.setLayout(new BorderLayout());
        frame.add(chessPanel, BorderLayout.WEST);
        frame.add(rightPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    /**
     * Resets the chess game to its initial state.
     */
    private void resetGame() {
        turnCounter = 0;
        try {
            chessRound.resetBoard();
        } catch (InputException ex) {
            throw new RuntimeException(ex);
        }
        chessPanel.drawBoard();
        moveInput.setText("");
        error.setText("");
        instructionLabel.setText("White to move:");
        advantageLabel.setText("Advantage: 0");
    }
}

