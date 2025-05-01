package me.dariansandru.ui.gui.playerGUI;

import me.dariansandru.domain.chess.chessEngine.EngineConnector;
import me.dariansandru.domain.chess.piece.Piece;
import me.dariansandru.domain.chess.piece.PieceColour;
import me.dariansandru.domain.validator.exception.ValidatorException;
import me.dariansandru.io.exception.InputException;
import me.dariansandru.utilities.FENEncoder;
import me.dariansandru.ui.guiController.playerGUIController.ChessGUIController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Objects;

public class ChessGUI extends JPanel {
    private final ChessGUIController controller;
    private EngineConnector engine;
    private Piece[][] pieces;
    private int selectedRow = -1;
    private int selectedCol = -1;

    private final boolean vsAI;
    private PieceColour currentTurn;
    private JPanel selectedSquarePanel = null;

    private final JFrame parentFrame;
    private JPanel boardPanel;
    private final JPanel[][] squarePanels = new JPanel[8][8];
    private static final Color LIGHT_SQUARE = new Color(240, 217, 181);
    private static final Color DARK_SQUARE = new Color(181, 136, 99);
    private static final int SQUARE_SIZE = 64;

    public ChessGUI(ChessGUIController controller, JFrame parentFrame) throws ValidatorException, InputException {
        this.controller = controller;
        this.vsAI = controller.isVsAI();
        this.parentFrame = parentFrame;
        this.pieces = controller.getChessRound().getPieces();
        this.currentTurn = PieceColour.WHITE;

        if (vsAI) {
            this.engine = new EngineConnector("localhost", 12345, 5000);
            try {
                this.engine.connect();
            } catch (IOException e) {
                System.err.println("Failed to connect to engine: " + e.getMessage());
            }
        }

        this.setLayout(new BorderLayout());
        drawBoard();
    }

    private Image getImageFromPiece(Piece piece) {
        if (piece == null || Objects.equals(piece.getName(), "None")) return null;
        PieceColour colour = piece.getColour();
        String imageName = "images/" + piece.getName() + colour.toString() + ".jpeg";
        ImageIcon icon = new ImageIcon(imageName);
        return icon.getImage().getScaledInstance(SQUARE_SIZE - 10, SQUARE_SIZE - 10, Image.SCALE_SMOOTH);
    }

    public void drawBoard() throws ValidatorException, InputException {
        this.removeAll();
        this.pieces = controller.getChessRound().getPieces();

        JPanel topPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> handleBackButton());
        topPanel.add(backButton, BorderLayout.WEST);
        this.add(topPanel, BorderLayout.NORTH);

        boardPanel = new JPanel(new GridLayout(8, 8));
        boardPanel.setPreferredSize(new Dimension(SQUARE_SIZE * 8, SQUARE_SIZE * 8));
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
                if (vsAI && currentTurn == PieceColour.BLACK) {
                    return;
                }

                JPanel clickedSquare = (JPanel) e.getComponent();
                int squareIndex = getSquareIndex(clickedSquare);
                int toRow = 7 - (squareIndex / 8);
                int toCol = squareIndex % 8;

                if (selectedSquarePanel == clickedSquare) {
                    selectedSquarePanel.setBorder(null);
                    selectedSquarePanel = null;
                    selectedRow = -1;
                    selectedCol = -1;
                    return;
                }

                if (selectedSquarePanel == null) {
                    if (pieces[toRow][toCol] != null && pieces[toRow][toCol].getColour() == currentTurn) {
                        selectedSquarePanel = clickedSquare;
                        selectedRow = toRow;
                        selectedCol = toCol;
                        selectedSquarePanel.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                    }
                } else {
                    Piece selectedPiece = pieces[selectedRow][selectedCol];
                    Piece clickedPiece = pieces[toRow][toCol];

                    if (selectedPiece.getRepresentation().equals("K") && clickedPiece != null &&
                            clickedPiece.getColour() == currentTurn && clickedPiece.getRepresentation().equals("R")) {
                        handleCastling(selectedRow, selectedCol, toCol);
                    } else {
                        String moveNotation = createMoveNotation(selectedRow, selectedCol, toRow, toCol);
                        tryMove(selectedRow, selectedCol, toRow, toCol, moveNotation, true);
                    }
                }
            }
        };
    }

    private void handleCastling(int row, int kingCol, int rookCol) {
        int targetKingCol = rookCol == 0 ? 2 : 6;
        int targetRookCol = rookCol == 0 ? 3 : 5;

        try {
            boolean canCastle = (rookCol == 0 && controller.getChessRound().canCastleQueenSide(currentTurn)) ||
                    (rookCol == 7 && controller.getChessRound().canCastleKingSide(currentTurn));

            if (canCastle) {
                Piece king = pieces[row][kingCol];
                Piece rook = pieces[row][rookCol];

                pieces[row][targetKingCol] = king;
                pieces[row][kingCol] = null;

                pieces[row][targetRookCol] = rook;
                pieces[row][rookCol] = null;

                updateSquare(row, kingCol);
                updateSquare(row, targetKingCol);
                updateSquare(row, rookCol);
                updateSquare(row, targetRookCol);

                switchTurn();
            }
        } catch (Exception ex) {
            clearSelection();
        }

        clearSelection();
    }

    private void tryMove(int fromRow, int fromCol, int toRow, int toCol, String moveNotation, boolean isPlayerMove) {
        try {
            if (controller.getChessRound().isMovePlayable(fromRow, fromCol, toRow, toCol,
                    pieces[fromRow][fromCol].getRepresentation(), currentTurn, moveNotation)) {

                controller.getChessRound().movePiece(moveNotation, currentTurn);
                pieces = controller.getChessRound().getPieces();
                redrawSquareUpdates(fromRow, fromCol, toRow, toCol);
                switchTurn();

                if (vsAI && currentTurn == PieceColour.BLACK && isPlayerMove) {
                    SwingUtilities.invokeLater(() -> {
                        try {
                            requestEngineMove();
                        } catch (ValidatorException | InputException e) {
                            e.printStackTrace();
                        }
                    });
                }

            } else {
                clearSelection();
            }
        } catch (Exception ex) {
            clearSelection();
        }
    }

    private void updateSquare(int row, int col) {
        JPanel square = squarePanels[row][col];
        square.removeAll();
        Image img = getImageFromPiece(pieces[row][col]);
        if (img != null) square.add(new JLabel(new ImageIcon(img)));

        square.revalidate();
        square.repaint();
    }

    private void redrawSquareUpdates(int fromRow, int fromCol, int toRow, int toCol) {
        updateSquare(fromRow, fromCol);
        updateSquare(toRow, toCol);
        clearSelection();
    }

    private void clearSelection() {
        if (selectedSquarePanel != null) selectedSquarePanel.setBorder(null);
        selectedSquarePanel = null;
        selectedRow = -1;
        selectedCol = -1;
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
        if (engine != null) {
            engine.disconnect();
        }
        if (parentFrame != null) parentFrame.dispose();
    }

    public void setCurrentTurn(PieceColour turn) {
        this.currentTurn = turn;
    }

    private void requestEngineMove() throws ValidatorException, InputException {
        if (!vsAI || currentTurn != PieceColour.BLACK) return;

        String fen = FENEncoder.encode(controller.getChessRound(), currentTurn);
        try {
            String move = engine.getBestMove(fen);
            if (move != null && (move.length() == 4 || move.length() == 5)) executeEngineMove(move);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Engine error: " + e.getMessage());
        }
    }

    private void executeEngineMove(String engineMove) throws ValidatorException, InputException {
        int fromCol = engineMove.charAt(0) - 'a';
        int fromRow = (Character.getNumericValue(engineMove.charAt(1)) - 1);

        int toCol = engineMove.charAt(2) - 'a';
        int toRow = (Character.getNumericValue(engineMove.charAt(3)) - 1);

        String promotion = engineMove.length() == 5 ? String.valueOf(engineMove.charAt(4)).toUpperCase() : null;

        String pieceSymbol = pieces[fromRow][fromCol].getRepresentation();
        String moveNotation;

        if (pieceSymbol.equals("P")) {
            char file = (char) ('a' + toCol);
            int rank = toRow + 1;
            moveNotation = promotion != null ? file + "" + rank + promotion : file + "" + rank;
        } else {
            char file = (char) ('a' + toCol);
            int rank = toRow + 1;
            moveNotation = pieceSymbol + file + rank;
        }

        controller.getChessRound().movePiece(moveNotation, currentTurn);
        pieces = controller.getChessRound().getPieces();

        updateSquare(fromRow, fromCol);
        updateSquare(toRow, toCol);

        currentTurn = PieceColour.WHITE;
        controller.updateCurrentTurn(currentTurn.name());
    }

    private void switchTurn() {
        currentTurn = currentTurn == PieceColour.WHITE ? PieceColour.BLACK : PieceColour.WHITE;
        controller.updateCurrentTurn(currentTurn.name());
    }
}
