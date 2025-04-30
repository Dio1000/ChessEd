package me.dariansandru.ui.guiController.playerGUIController;

import me.dariansandru.controller.ChessController;
import me.dariansandru.domain.chess.chessEngine.ChessEngine;
import me.dariansandru.domain.chess.piece.PieceColour;
import me.dariansandru.domain.validator.exception.ValidatorException;
import me.dariansandru.io.exception.InputException;
import me.dariansandru.round.ChessRound;
import me.dariansandru.ui.consoleUI.ChessConsoleUI;
import me.dariansandru.ui.gui.playerGUI.ChessGUI;

import javax.swing.*;
import java.awt.*;

public class ChessGUIController {

    private final ChessRound chessRound;
    private final ChessEngine chessEngine;
    private final EvaluationBar evaluationBar;
    private final JLabel currentTurnLabel;

    public ChessRound getChessRound() {
        return this.chessRound;
    }

    public void updateEvaluation(int whiteScore) {
        evaluationBar.setWhitePercentage(whiteScore);
    }

    public void updateCurrentTurn(String turnText) {
        currentTurnLabel.setText("Current Turn: " + turnText);
    }

    private static class EvaluationBar extends JPanel {
        private int whitePercentage = 250;

        public void setWhitePercentage(int percentage) {
            this.whitePercentage = Math.max(0, Math.min(percentage, 500));
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int width = getWidth();
            int height = getHeight();

            int whiteHeight = (height * whitePercentage) / 500;
            int blackHeight = height - whiteHeight;

            g.setColor(Color.BLACK);
            g.fillRect(0, 0, width, blackHeight);

            g.setColor(Color.WHITE);
            g.fillRect(0, blackHeight, width, whiteHeight);
        }
    }

    public ChessGUIController(ChessConsoleUI chessConsoleUI) {
        ChessController chessController = chessConsoleUI.getChessController();
        this.chessRound = chessController.getChessRound();
        this.chessEngine = new ChessEngine();
        chessEngine.setChessRound(chessRound);
        this.evaluationBar = new EvaluationBar();
        this.currentTurnLabel = new JLabel("Current Turn: WHITE");
    }

    public void run() throws ValidatorException, InputException {
        JFrame frame = new JFrame("Chess");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLayout(new BorderLayout());

        ChessGUI chessPanel = new ChessGUI(this, frame);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(200, 700));
        rightPanel.setBackground(Color.LIGHT_GRAY);

        JButton resetButton = new JButton("Reset Game");
        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resetButton.addActionListener(e -> {
            try {
                chessRound.resetBoard();
                chessPanel.setCurrentTurn(PieceColour.WHITE);
                currentTurnLabel.setText("Current Turn: WHITE");
            } catch (InputException ex) {
                throw new RuntimeException(ex);
            }
            try {
                chessPanel.drawBoard();
            } catch (ValidatorException | InputException ex) {
                throw new RuntimeException(ex);
            }
        });

        JLabel title = new JLabel("Options");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        evaluationBar.setPreferredSize(new Dimension(40, 300));
        evaluationBar.setMaximumSize(new Dimension(60, 400));
        evaluationBar.setAlignmentX(Component.CENTER_ALIGNMENT);

        currentTurnLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        currentTurnLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        currentTurnLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        rightPanel.add(title);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(resetButton);
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(currentTurnLabel);
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(evaluationBar);

        frame.add(chessPanel, BorderLayout.CENTER);
        frame.add(rightPanel, BorderLayout.EAST);

        frame.setVisible(true);
    }


}
