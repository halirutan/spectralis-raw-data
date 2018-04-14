package de.halirutan.spectralis.filestructure;

/**
 * Provides retinal thickness within one cell of a thickness grid
 * @see CircularThicknessGrid
 * (c) Patrick Scheibe 2018
 */
public class Sector {
    private final float thickness;
    private final float volume;

    Sector(float thick, float vol) {
        thickness = thick;
        volume = vol;
    }

    public float getThickness() {
        return thickness;
    }

    public float getVolume() {
        return volume;
    }
}
