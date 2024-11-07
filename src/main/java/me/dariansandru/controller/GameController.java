package me.dariansandru.controller;

import me.dariansandru.io.exception.InputException;

public interface GameController {
    String play(String move);

    void reset() throws InputException;
}
