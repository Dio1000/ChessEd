package me.dariansandru.domain.chess.chessEngine;

import me.dariansandru.domain.chess.piece.PieceColour;

public class ChessEngineTree {
    private ChessEngineTreeNode root;
    private ChessEngine chessEngine;
    private PieceColour colour;

    public ChessEngineTree(ChessEngine chessEngine) {
        this.chessEngine = chessEngine;
    }

    public ChessEngine getChessEngine() {
        return chessEngine;
    }

    public void setChessEngine(ChessEngine chessEngine) {
        this.chessEngine = chessEngine;
    }

    public ChessEngineTreeNode getRoot() {
        return root;
    }

    public void setRoot(ChessEngineTreeNode root) {
        this.root = root;
    }

    public PieceColour getColour() {
        return colour;
    }

    public void setColour(PieceColour colour) {
        this.colour = colour;
    }
}
