package me.dariansandru.ui.guiController.playerGUIController;

import me.dariansandru.controller.ChessController;
import me.dariansandru.domain.chess.chessEngine.ChessEngine;
import me.dariansandru.io.exception.InputException;
import me.dariansandru.round.ChessRound;
import me.dariansandru.ui.consoleUI.ChessConsoleUI;
import me.dariansandru.ui.gui.playerGUI.ChessGUI;

import javax.swing.*;
import java.awt.*;

public class ChessGUIController {

    private final ChessRound chessRound;
    private final ChessEngine chessEngine;

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

            g.setColor(Color.WHITE);
            g.fillRect(0, 0, (width * whitePercentage) / 500, height);

            g.setColor(Color.BLACK);
            g.fillRect((width * whitePercentage) / 500, 0, width - (width * whitePercentage) / 500, height);
        }
    }

    public ChessGUIController(ChessConsoleUI chessConsoleUI) {
        ChessController chessController = chessConsoleUI.getChessController();
        this.chessRound = chessController.getChessRound();
        this.chessEngine = new ChessEngine();
        chessEngine.setChessRound(chessRound);
    }

    public void run() {
        JFrame frame = new JFrame("Chess");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 700);
        frame.setLayout(new BorderLayout());

        ChessGUI chessPanel = new ChessGUI(chessRound, frame);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(200, 700));
        rightPanel.setBackground(Color.LIGHT_GRAY);

        JButton resetButton = new JButton("Reset Game");
        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resetButton.addActionListener(e -> {
            try {
                chessRound.resetBoard();
            } catch (InputException ex) {
                throw new RuntimeException(ex);
            }
            chessPanel.drawBoard();
        });

        JLabel title = new JLabel("Options");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        EvaluationBar evaluationBar = new EvaluationBar();
        evaluationBar.setPreferredSize(new Dimension(180, 30));
        evaluationBar.setMaximumSize(new Dimension(180, 30));
        evaluationBar.setAlignmentX(Component.CENTER_ALIGNMENT);

        rightPanel.add(title);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(resetButton);
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(evaluationBar);

        frame.add(chessPanel, BorderLayout.CENTER);
        frame.add(rightPanel, BorderLayout.EAST);

        frame.setVisible(true);
    }
}
