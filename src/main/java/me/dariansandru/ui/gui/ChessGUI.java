package me.dariansandru.ui.gui;

import me.dariansandru.domain.chess.Piece;
import me.dariansandru.domain.chess.PieceColour;
import me.dariansandru.domain.chess.piece.EmptyPiece;
import me.dariansandru.domain.validator.exception.ValidatorException;
import me.dariansandru.io.exception.InputException;
import me.dariansandru.round.ChessRound;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

import static me.dariansandru.utilities.ChessUtils.getColRow;
import static me.dariansandru.utilities.ChessUtils.getPiece;

public class ChessGUI extends JPanel {
    private Piece[][] pieces;
    private final ChessRound chessRound;

    public ChessGUI(ChessRound chessRound) {
        initComponents();
        this.chessRound = chessRound;
        this.pieces = chessRound.getPieces();
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

                if ((i + j) % 2 == 0) {
                    square.setBackground(Color.PINK);
                } else {
                    square.setBackground(Color.WHITE);
                }

                Image scaledImage = getImageFromPiece(pieces[i][j]);
                if (scaledImage != null) square.setIcon(new ImageIcon(scaledImage));

                this.add(square);
            }
        }

        this.revalidate();
        this.repaint();
    }

    // Not working properly yet.
    public void updateBoard(String move, PieceColour colour) throws InputException, ValidatorException {
        char pieceType = move.charAt(0);
        String pieceName = getPiece(String.valueOf(pieceType), colour).getName();

        int destinationCol = getColRow(move).getValue1();
        int destinationRow = getColRow(move).getValue2();

        Piece pieceToMove = null;
        int startRow = -1;
        int startCol = -1;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = pieces[i][j];
                if (chessRound.checkMovePiece(move, colour)) {
                    pieceToMove = piece;
                    startRow = i;
                    startCol = j;
                    break;
                }
            }
            if (pieceToMove != null) break;
        }
        chessRound.movePiece(move, colour);

        JLabel startSquare = (JLabel) this.getComponent(startRow * 8 + startCol);
        startSquare.setIcon(null);

        JLabel destinationSquare = (JLabel) this.getComponent(destinationRow * 8 + destinationCol);
        assert pieceToMove != null;

        Image scaledImage = getImageFromPiece(pieceToMove);
        if (scaledImage != null) {
            destinationSquare.setIcon(new ImageIcon(scaledImage));
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
