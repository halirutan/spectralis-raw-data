package de.halirutan.spectralis;

/**
 * Created by patrick on 05.03.18.
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
