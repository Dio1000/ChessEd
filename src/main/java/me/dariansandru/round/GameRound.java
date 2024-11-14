package me.dariansandru.round;

import me.dariansandru.io.exception.InputException;

public interface GameRound {
    public void resetBoard() throws InputException;

    public void setPieceLocation() throws InputException;
}
