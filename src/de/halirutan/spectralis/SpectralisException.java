package de.halirutan.spectralis;

/**
 * General exception that is thrown when accessing the file went wrong
 * (c) Patrick Scheibe 2018
 */
public class SpectralisException extends Exception {
    public SpectralisException(Exception e) {
        super(e);
    }

    public SpectralisException(String message) {
        super(message);
    }
}
