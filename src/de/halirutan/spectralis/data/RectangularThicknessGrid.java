package de.halirutan.spectralis.data;

/**
 * Provides access to the retinal thickness measurements inside one of the defined circular grids. The most popular one probably
 * being the one from the ETDRS which creates a nine-field circular grid
 * Created by patrick on 14.02.18.
 * (c) Patrick Scheibe 2018
 */
public class RectangularThicknessGrid implements Grid{

    final private GridType myType;
    int myTypeID;
    int myNumRow;
    int myNumCol;
    float myCellWidth;
    float myCellHeight;
    float myTilt;
    double myCenterPos[];
    Sector myCells[];

    RectangularThicknessGrid(GridType myType) {
        this.myType = myType;
        assert (myType == GridType.RECTANGULAR_15 || myType == GridType.RECTANGULAR_20 || myType == GridType.RECTANGULAR_POLE);
    }

    @Override
    public GridType getGridType() {
        return myType;
    }

    public int getTypeID() {
        return myTypeID;
    }

    public int getNumRow() {
        return myNumRow;
    }

    public int getNumCol() {
        return myNumCol;
    }

    public float getCellWidth() {
        return myCellWidth;
    }

    public float getCellHeight() {
        return myCellHeight;
    }

    public float getTilt() {
        return myTilt;
    }

    public double[] getCenterPos() {
        return myCenterPos;
    }

    public Sector[] getCells() {
        return myCells;
    }
}
