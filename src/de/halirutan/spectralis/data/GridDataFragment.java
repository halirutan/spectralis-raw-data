package de.halirutan.spectralis.data;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by patrick on 14.02.18.
 * (c) Patrick Scheibe 2018
 */
public class GridDataFragment extends DataFragment<Grid> {

    private GridType myType;

    public GridDataFragment(GridType type) {
        myType = type;
    }

    @Override
    public final Grid read(RandomAccessFile file) throws IOException {
        IntegerDataFragment intDF = new IntegerDataFragment();
        DoubleDataFragment doubleDF = new DoubleDataFragment();
        FloatDataFragment floatDF = new FloatDataFragment();
        SectorDataFragment sectorDF = new SectorDataFragment();

        switch (myType) {
            case CIRCULAR1:
            case CIRCULAR2:
            case CIRCULAR_ETDRS:
                CircularThicknessGrid circularGrid = new CircularThicknessGrid(myType);
                circularGrid.myTypeID = intDF.read(file);
                circularGrid.myDiameters = new double[3];
                for (int i = 0; i < 3; i++) {
                    circularGrid.myDiameters[i] = doubleDF.read(file);
                }
                circularGrid.myCenterPos = new double[2];
                circularGrid.myCenterPos[0] = doubleDF.read(file);
                circularGrid.myCenterPos[1] = doubleDF.read(file);
                circularGrid.myCentralThickness = floatDF.read(file);
                circularGrid.myMinCentralThickness = floatDF.read(file);
                circularGrid.myMaxCentralThickness = floatDF.read(file);
                circularGrid.myTotalVolume = floatDF.read(file);
                circularGrid.mySectors = new Sector[9];
                for(int s = 0; s < 9; s++) {
                    circularGrid.mySectors[s] = sectorDF.read(file);
                }
                return circularGrid;
            case RECTANGULAR_15:
            case RECTANGULAR_20:
            case RECTANGULAR_POLE:
                RectangularThicknessGrid rectangularGrid = new RectangularThicknessGrid(myType);
                rectangularGrid.myTypeID = intDF.read(file);
                rectangularGrid.myNumRow = intDF.read(file);
                rectangularGrid.myNumCol = intDF.read(file);
                rectangularGrid.myCellWidth = floatDF.read(file);
                rectangularGrid.myCellHeight = floatDF.read(file);
                rectangularGrid.myTilt = floatDF.read(file);
                rectangularGrid.myCenterPos = new double[2];
                rectangularGrid.myCenterPos[0] = doubleDF.read(file);
                rectangularGrid.myCenterPos[1] = doubleDF.read(file);
                int numCells = rectangularGrid.myNumRow*rectangularGrid.myNumCol;
                rectangularGrid.myCells = new Sector[numCells];
                for(int cell = 0; cell < numCells; cell++) {
                    rectangularGrid.myCells[cell] = sectorDF.read(file);
                }
                return rectangularGrid;
        }
        throw new IOException("Tried to read grid, although grid was not available. This should not happen.");
    }


}
