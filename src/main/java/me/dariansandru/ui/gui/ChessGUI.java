package me.dariansandru.ui.gui;

import me.dariansandru.domain.chess.Piece;
import me.dariansandru.domain.chess.PieceColour;
import me.dariansandru.domain.chess.piece.Pawn;
import me.dariansandru.domain.validator.exception.ValidatorException;
import me.dariansandru.io.exception.InputException;
import me.dariansandru.round.ChessRound;
import me.dariansandru.utilities.ChessUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

import static me.dariansandru.utilities.ChessUtils.getColRow;

/**
 * Using this class allows the user to use a custom-made GUI for a Chess game.
 */
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

    /**
     * Fetches the image of a piece from the images directory to display on the board.
     * @param piece Piece to display.
     * @return Image of the piece with the correct colour.
     */
    private Image getImageFromPiece(Piece piece) {
        if (Objects.equals(piece.getName(), "None")) return null;
        PieceColour colour = piece.getColour();
        String imageName = "images/" + piece.getName() + colour.toString() + ".jpeg";
        ImageIcon icon = new ImageIcon(imageName);
        return icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
    }

    /**
     * Draws the board in the initial state. Should only be used once because
     * of the high cost of the operation.
     */
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

    /**
     * Updates the board to increase efficiency of displays by only updating
     * the moved piece.
     * @param move Move that was played.
     * @param colour Colour of the piece that was moved.
     * @throws InputException Thrown when the input validation fails.
     * @throws ValidatorException Thrown when the validator fails.
     */
    public void updateBoard(String move, PieceColour colour) throws InputException, ValidatorException {
        int destinationCol = getColRow(move).getValue1();
        int destinationRow = getColRow(move).getValue2();

        int startRow = chessRound.getStartLocation(move, colour).getValue1();
        int startCol = chessRound.getStartLocation(move, colour).getValue2();

        Piece pieceToMove = ChessUtils.getPiece(String.valueOf(move.charAt(0)), colour);
        if (Objects.equals(pieceToMove.getName(), "None")) pieceToMove = new Pawn(colour);

        int startSquareIndex = (7 - startRow) * 8 + startCol;
        int destinationSquareIndex = (7 - destinationRow) * 8 + destinationCol;

        JLabel startSquare = (JLabel) this.getComponent(startSquareIndex);
        startSquare.setIcon(null);

        JLabel destinationSquare = (JLabel) this.getComponent(destinationSquareIndex);
        Image scaledImage = getImageFromPiece(pieceToMove);
        if (scaledImage != null) {
            destinationSquare.setIcon(new ImageIcon(scaledImage));
        }

        this.revalidate();
        this.repaint();
    }

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
