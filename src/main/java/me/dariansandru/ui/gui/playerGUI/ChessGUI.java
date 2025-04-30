package me.dariansandru.ui.gui.playerGUI;

import me.dariansandru.domain.chess.piece.Piece;
import me.dariansandru.domain.chess.piece.PieceColour;

import me.dariansandru.ui.guiController.playerGUIController.ChessGUIController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class ChessGUI extends JPanel {
    private final ChessGUIController controller;
    private Piece[][] pieces;
    private PieceColour currentTurn = PieceColour.WHITE;
    private JPanel selectedSquarePanel = null;
    private JFrame parentFrame;
    private JPanel boardPanel;
    private JPanel[][] squarePanels = new JPanel[8][8];
    private static final Color LIGHT_SQUARE = new Color(240, 217, 181);
    private static final Color DARK_SQUARE = new Color(181, 136, 99);
    private static final int SQUARE_SIZE = 64;

    public ChessGUI(ChessGUIController controller, JFrame parentFrame) {
        this.controller = controller;
        this.parentFrame = parentFrame;
        this.pieces = controller.getChessRound().getPieces();
        this.setLayout(new BorderLayout());
        drawBoard();
    }

    private Image getImageFromPiece(Piece piece) {
        if (piece == null || Objects.equals(piece.getName(), "None"))
            return null;

        PieceColour colour = piece.getColour();
        String imageName = "images/" + piece.getName() + colour.toString() + ".jpeg";
        ImageIcon icon = new ImageIcon(imageName);
        return icon.getImage().getScaledInstance(SQUARE_SIZE - 10, SQUARE_SIZE - 10, Image.SCALE_SMOOTH);
    }

    public void drawBoard() {
        this.removeAll();
        this.pieces = controller.getChessRound().getPieces();

        JPanel topPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> handleBackButton());
        topPanel.add(backButton, BorderLayout.WEST);
        this.add(topPanel, BorderLayout.NORTH);

        boardPanel = new JPanel(new GridLayout(8, 8));
        MouseAdapter clickHandler = createMouseHandler();

        for (int row = 7; row >= 0; row--) {
            for (int col = 0; col < 8; col++) {
                JPanel square = new JPanel(new BorderLayout());
                square.setPreferredSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));
                square.setBackground((row + col) % 2 == 0 ? LIGHT_SQUARE : DARK_SQUARE);
                square.addMouseListener(clickHandler);
                squarePanels[row][col] = square;

                Image img = getImageFromPiece(pieces[row][col]);
                if (img != null) square.add(new JLabel(new ImageIcon(img)));

                boardPanel.add(square);
            }
        }

        this.add(boardPanel, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }

    private MouseAdapter createMouseHandler() {
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JPanel clickedSquare = (JPanel) e.getComponent();
                int squareIndex = getSquareIndex(clickedSquare);
                int toRow = 7 - (squareIndex / 8);
                int toCol = squareIndex % 8;

                if (selectedSquarePanel == null) {
                    if (pieces[toRow][toCol] != null && pieces[toRow][toCol].getColour() == currentTurn) {
                        selectedSquarePanel = clickedSquare;
                        selectedSquarePanel.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                    }
                }
                else {
                    int fromIndex = getSquareIndex(selectedSquarePanel);
                    int fromRow = 7 - (fromIndex / 8);
                    int fromCol = fromIndex % 8;

                    String moveNotation = createMoveNotation(fromRow, fromCol, toRow, toCol);
                    try {
                        if (controller.getChessRound().isMovePlayable(
                                fromRow, fromCol, toRow, toCol,
                                pieces[fromRow][fromCol].getRepresentation(),
                                currentTurn, moveNotation)) {

                            controller.getChessRound().movePiece(moveNotation, currentTurn);
                            currentTurn = currentTurn == PieceColour.WHITE ? PieceColour.BLACK : PieceColour.WHITE;
                            pieces = controller.getChessRound().getPieces();
                            redrawSquareUpdates(fromRow, fromCol, toRow, toCol);
                        }
                    } catch (Exception ex) {
                        selectedSquarePanel.setBorder(null);
                        selectedSquarePanel = null;
                    }
                }
            }
        };
    }

    private void updateSquare(int row, int col) {
        JPanel square = squarePanels[row][col];
        square.removeAll();
        Image img = getImageFromPiece(pieces[row][col]);
        if (img != null) {
            square.add(new JLabel(new ImageIcon(img)));
        }
        square.revalidate();
        square.repaint();
    }

    private void redrawSquareUpdates(int fromRow, int fromCol, int toRow, int toCol) {
        updateSquare(fromRow, fromCol);
        updateSquare(toRow, toCol);
        if (selectedSquarePanel != null) selectedSquarePanel.setBorder(null);
        selectedSquarePanel = null;
    }

    private String createMoveNotation(int fromRow, int fromCol, int toRow, int toCol) {
        String pieceNotation = pieces[fromRow][fromCol].getRepresentation();
        char file = (char) ('a' + toCol);
        int rank = toRow + 1;
        return pieceNotation.equals("P") ? "" + file + rank : pieceNotation + file + rank;
    }

    private int getSquareIndex(JPanel square) {
        Component[] squares = boardPanel.getComponents();
        for (int i = 0; i < squares.length; i++) {
            if (squares[i] == square) return i;
        }
        return -1;
    }

    private void handleBackButton() {
        if (parentFrame != null) parentFrame.dispose();
    }
}
