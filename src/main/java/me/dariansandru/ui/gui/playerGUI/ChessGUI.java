package me.dariansandru.ui.gui.playerGUI;

import me.dariansandru.domain.chess.piece.Piece;
import me.dariansandru.domain.chess.piece.PieceColour;
import me.dariansandru.domain.chess.piece.Pawn;
import me.dariansandru.round.ChessRound;
import me.dariansandru.utilities.ChessUtils;
import me.dariansandru.utilities.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

import static me.dariansandru.utilities.ChessUtils.getColRow;

public class ChessGUI extends JPanel {
    private Piece[][] pieces;
    private final ChessRound chessRound;

    private JFrame parentFrame;
    private JPanel boardPanel;
    private static final Color LIGHT_SQUARE = new Color(240, 217, 181);
    private static final Color DARK_SQUARE = new Color(181, 136, 99);

    private static final int SQUARE_SIZE = 64;
    private PieceColour currentTurn = PieceColour.WHITE;
    private JPanel selectedSquarePanel = null;

    public ChessGUI(ChessRound chessRound, JFrame parentFrame) {
        this.chessRound = chessRound;
        this.parentFrame = parentFrame;
        this.pieces = chessRound.getPieces();
        this.setLayout(new BorderLayout());
        drawBoard();
    }

    private Image getImageFromPiece(Piece piece) {
        if (Objects.equals(piece.getName(), "None"))
            return null;
        PieceColour colour = piece.getColour();
        String imageName = "images/" + piece.getName() + colour.toString() + ".jpeg";
        ImageIcon icon = new ImageIcon(imageName);
        return icon.getImage().getScaledInstance(SQUARE_SIZE - 10, SQUARE_SIZE - 10, Image.SCALE_SMOOTH);
    }

    public void drawBoard() {
        this.removeAll();

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> handleBackButton());
        topPanel.add(backButton, BorderLayout.WEST);
        this.add(topPanel, BorderLayout.NORTH);

        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(8, 8));
        pieces = chessRound.getPieces();

        MouseAdapter clickHandler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JPanel clickedSquare = (JPanel) e.getComponent();
                int squareIndex = getSquareIndex(clickedSquare);
                int toRow = 7 - (squareIndex / 8);
                int toCol = squareIndex % 8;
                Piece clickedPiece = pieces[toRow][toCol];

                if (selectedSquarePanel == null) {
                    if (clickedPiece != null && clickedPiece.getColour() == currentTurn) {
                        selectedSquarePanel = clickedSquare;
                        selectedSquarePanel.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                    }
                }
                else {
                    int fromIndex = getSquareIndex(selectedSquarePanel);
                    int fromRow = 7 - (fromIndex / 8);
                    int fromCol = fromIndex % 8;
                    Piece movingPiece = pieces[fromRow][fromCol];

                    if (movingPiece == null) {
                        selectedSquarePanel.setBorder(null);
                        selectedSquarePanel = null;
                        return;
                    }

                    String pieceNotation = movingPiece.getRepresentation();
                    String moveNotation;

                    char file = (char)('a' + toCol);
                    int rank = toRow + 1;

                    if (pieceNotation.equals("P")) moveNotation = "" + file + rank;
                    else moveNotation = pieceNotation + file + rank;


                    try {
                        if (chessRound.isMovePlayable(
                                fromRow, fromCol,
                                toRow, toCol,
                                pieceNotation,
                                currentTurn,
                                moveNotation
                        )) {
                            pieces[toRow][toCol] = movingPiece;
                            pieces[fromRow][fromCol] = null;

                            selectedSquarePanel.removeAll();
                            selectedSquarePanel.setBorder(null);

                            clickedSquare.removeAll();
                            Image scaledImage = getImageFromPiece(movingPiece);
                            if (scaledImage != null) {
                                clickedSquare.add(new JLabel(new ImageIcon(scaledImage)));
                            }

                            currentTurn = (currentTurn == PieceColour.WHITE) ? PieceColour.BLACK : PieceColour.WHITE;
                        }
                        else {
                            selectedSquarePanel.setBorder(null);
                            JOptionPane.showMessageDialog(ChessGUI.this,
                                    "Illegal move: " + moveNotation,
                                    "Move Not Allowed",
                                    JOptionPane.WARNING_MESSAGE);
                        }
                    } catch (Exception ex) {
                        selectedSquarePanel.setBorder(null);
                        JOptionPane.showMessageDialog(ChessGUI.this,
                                "Error validating move: " + ex.getMessage(),
                                "Move Error",
                                JOptionPane.ERROR_MESSAGE);
                    }

                    selectedSquarePanel = null;
                }
            }
        };

        for (int row = 7; row >= 0; row--) {
            for (int col = 0; col < 8; col++) {
                JPanel square = new JPanel(new BorderLayout());
                square.setPreferredSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));
                square.setOpaque(true);
                square.setBackground((row + col) % 2 == 0 ? LIGHT_SQUARE : DARK_SQUARE);

                Image scaledImage = getImageFromPiece(pieces[row][col]);
                if (scaledImage != null) {
                    square.add(new JLabel(new ImageIcon(scaledImage)));
                }

                square.addMouseListener(clickHandler);
                boardPanel.add(square);
            }
        }

        this.add(boardPanel, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }

    private JPanel getSquareAtLocation(Point location) {
        for (Component comp : boardPanel.getComponents()) {
            if (comp instanceof JPanel && comp.getLocation().equals(location)) {
                return (JPanel) comp;
            }
        }
        return null;
    }

    public void updateBoard(String move, PieceColour colour, Pair<Integer, Integer> startLocation) {
        try {
            int destinationCol = getColRow(move).getValue1();
            int destinationRow = getColRow(move).getValue2();
            int startRow = startLocation.getValue1();
            int startCol = startLocation.getValue2();

            Piece pieceToMove = ChessUtils.getPiece(String.valueOf(move.charAt(0)), colour);
            if (Objects.equals(pieceToMove.getName(), "None"))
                pieceToMove = new Pawn(colour);

            int startSquareIndex = (7 - startRow) * 8 + startCol;
            int destinationSquareIndex = (7 - destinationRow) * 8 + destinationCol;

            JPanel startSquare = (JPanel) boardPanel.getComponent(startSquareIndex);
            startSquare.removeAll();
            startSquare.revalidate();
            startSquare.repaint();

            JPanel destinationSquare = (JPanel) boardPanel.getComponent(destinationSquareIndex);
            destinationSquare.removeAll();
            Image scaledImage = getImageFromPiece(pieceToMove);
            if (scaledImage != null) {
                destinationSquare.add(new JLabel(new ImageIcon(scaledImage)));
            }
            destinationSquare.revalidate();
            destinationSquare.repaint();

        } catch (Exception ex) {
            System.out.println("Error updating board: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void handleBackButton() {
        if (parentFrame != null) {
            parentFrame.dispose();
        }
    }

    private int getSquareIndex(JPanel square) {
        Component[] squares = boardPanel.getComponents();
        for (int i = 0; i < squares.length; i++) {
            if (squares[i] == square) {
                return i;
            }
        }
        return -1;
    }

}
