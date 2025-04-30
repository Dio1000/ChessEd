package me.dariansandru.domain.chess.chessEngine;

import me.dariansandru.domain.chess.piece.PieceColour;
import me.dariansandru.domain.validator.exception.ValidatorException;
import me.dariansandru.io.exception.InputException;
import me.dariansandru.round.ChessRound;

public class ChessEngineTreeNode {
    private ChessEngineTreeNode parent;
    private ChessEngineTreeNode[] children;
    private ChessRound key;
    private ChessEngine chessEngine;
    private PieceColour pieceColour;
    private boolean blocked = false;

    public ChessEngineTreeNode() {
        this.parent = null;
        this.children = new ChessEngineTreeNode[0];
        this.key = null;
    }

    public ChessEngineTreeNode(ChessRound key) {
        this.parent = null;
        this.children = new ChessEngineTreeNode[0];
        this.key = key;
    }

    public ChessEngineTreeNode getParent() {
        return parent;
    }

    public void setParent(ChessEngineTreeNode parent) {
        this.parent = parent;
    }

    public ChessEngineTreeNode[] getChildren() {
        return children;
    }

    public void setChildren(ChessEngineTreeNode[] children) {
        this.children = children;
    }

    public ChessRound getKey() {
        return key;
    }

    public void setKey(ChessRound key) {
        this.key = key;
    }

    public ChessEngine getChessEngine() {
        return chessEngine;
    }

    public void setChessEngine(ChessEngine chessEngine) {
        this.chessEngine = chessEngine;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public void setChessRound(ChessRound chessRound) {
        this.chessEngine.setChessRound(chessRound);
    }

    public int evaluatePosition() throws ValidatorException, InputException {
        return this.chessEngine.evaluatePosition(pieceColour);
    }

    public PieceColour getPieceColour() {
        return pieceColour;
    }

    public void setPieceColour(PieceColour pieceColour) {
        this.pieceColour = pieceColour;
    }
}
