package de.halirutan.spectralis.filestructure;

import de.halirutan.spectralis.filestructure.CircularThicknessGrid;

/**
 * Provides retinal thickness within one cell of a thickness grid
 * @see CircularThicknessGrid
 * Created by patrick on 14.02.18.
 * (c) Patrick Scheibe 2018
 */
public class Sector {
    private float myThickness;
    private float myVolume;

    Sector(float myThickness, float myVolume) {
        this.myThickness = myThickness;
        this.myVolume = myVolume;
    }

    public float getThickness() {
        return myThickness;
    }

    public float getVolume() {
        return myVolume;
    }
}
