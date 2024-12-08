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

    public GUIController(ChessConsoleUI chessConsoleUI)
    {
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

        ChessGUI chessPanel = new ChessGUI(chessRound);

        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.LIGHT_GRAY);
        rightPanel.setLayout(new BorderLayout());

        JTextField moveInput = new JTextField();
        JButton submitButton = new JButton("Submit");
        JLabel instructionLabel = new JLabel("White to move:");
        JLabel error = new JLabel();
        JLabel advantageLabel = new JLabel("Advantage: 0");

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 1, 5, 5));
        inputPanel.add(instructionLabel);
        inputPanel.add(moveInput);
        inputPanel.add(submitButton);
        inputPanel.add(error);
        inputPanel.add(advantageLabel);

        rightPanel.add(inputPanel, BorderLayout.NORTH);

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

                int advantage = chessRound.computeAdvantage();
                String advantageText = (advantage > 0) ? "White +" + advantage : (advantage < 0) ? "Black " + advantage : "0";
                advantageLabel.setText("Advantage: " + advantageText);
                
                if (chessRound.isCheckmate()) {
                    String winner = (turnCounter % 2 == 0) ? "Black" : "White";

                    JOptionPane.showMessageDialog(frame, winner + " won by checkmate!", "Game Over", JOptionPane.INFORMATION_MESSAGE);

                    turnCounter = 0;
                    chessRound.resetBoard();
                    chessPanel.drawBoard();
                    moveInput.setText("");
                    error.setText("");
                    instructionLabel.setText("White to move:");
                    advantageLabel.setText("Advantage: 0");
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

}
