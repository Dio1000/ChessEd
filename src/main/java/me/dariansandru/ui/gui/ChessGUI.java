package me.dariansandru.ui.gui;

import me.dariansandru.domain.chess.Piece;
import me.dariansandru.domain.chess.PieceColour;
import me.dariansandru.round.ChessRound;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ChessGUI extends JPanel {
    private Piece[][] pieces;
    private final ChessRound chessRound;

    public ChessGUI(ChessRound chessRound) {
        initComponents();
        pieces = chessRound.getPieces();
        this.chessRound = chessRound;

        this.setLayout(new GridLayout(8, 8));
        drawBoard();
    }

    private Image getImageFromPiece(Piece piece) {
        if (Objects.equals(piece.getName(), "None")) return null;
        PieceColour colour = piece.getColour();

        String imageName = "images/" + piece.getName() + colour.toString() + ".jpeg";
        ImageIcon icon = new ImageIcon(imageName);

        return icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
    }

    public void drawBoard() {
        this.removeAll();
        pieces = chessRound.getPieces();

        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j < 8; j++) {
                JLabel square = new JLabel();
                square.setOpaque(true);

                if ((i + j) % 2 != 0) {
                    square.setBackground(Color.WHITE);
                } else {
                    square.setBackground(Color.PINK);
                }

                Image scaledImage = getImageFromPiece(pieces[i][j]);
                if (scaledImage != null) square.setIcon(new ImageIcon(scaledImage));

                this.add(square);
            }
        }
        this.revalidate();
        this.repaint();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 579, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 394, Short.MAX_VALUE)
        );
    }
}
