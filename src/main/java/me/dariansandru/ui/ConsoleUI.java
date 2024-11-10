package me.dariansandru.ui;

import me.dariansandru.domain.validator.exception.ValidatorException;
import me.dariansandru.io.InputDevice;
import me.dariansandru.io.OutputDevice;
import me.dariansandru.io.exception.InputException;
import me.dariansandru.io.exception.OutputException;

public interface ConsoleUI {
    OutputDevice getOutputDevice();

    InputDevice getInputDevice();

    void show() throws InputException, OutputException, ValidatorException;
}
