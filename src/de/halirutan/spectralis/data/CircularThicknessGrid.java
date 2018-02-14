package de.halirutan.spectralis.data;

/**
 * Provides access to the retinal thickness measurements inside one of the defined circular grids. The most popular one probably
 * being the one from the ETDRS which creates a nine-field circular grid
 * Created by patrick on 14.02.18.
 * (c) Patrick Scheibe 2018
 */
public class CircularThicknessGrid implements Grid{

    final private GridType myType;
    int myTypeID;
    double myDiameters[];
    double myCenterPos[];
    float myCentralThickness;
    float myMinCentralThickness;
    float myMaxCentralThickness;
    float myTotalVolume;
    Sector mySectors[];

    CircularThicknessGrid(GridType myType) {
        this.myType = myType;
        assert (myType == GridType.CIRCULAR1 || myType == GridType.CIRCULAR2 || myType == GridType.CIRCULAR_ETDRS);
    }

    @Override
    public GridType getGridType() {
        return myType;
    }

    public double[] getDiameters() {
        return myDiameters;
    }

    public double[] getCenterPos() {
        return myCenterPos;
    }

    public float getCentralThickness() {
        return myCentralThickness;
    }

    public float getMinCentralThickness() {
        return myMinCentralThickness;
    }

    public float getMaxCentralThickness() {
        return myMaxCentralThickness;
    }

    public float getTotalVolume() {
        return myTotalVolume;
    }

    public Sector[] getSectors() {
        return mySectors;
    }
}
