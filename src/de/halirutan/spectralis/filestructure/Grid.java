package de.halirutan.spectralis.filestructure;

/**
 * Interface for the two different types of Grid that exist.
 * (c) Patrick Scheibe 2018
 */
@FunctionalInterface
public interface Grid {
    GridType getGridType();
}
