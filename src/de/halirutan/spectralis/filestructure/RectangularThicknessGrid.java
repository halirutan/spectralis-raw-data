package de.halirutan.spectralis.filestructure;

import java.io.IOException;
import java.io.RandomAccessFile;

import de.halirutan.spectralis.SpectralisException;

/**
 * Provides access to the retinal thickness measurements inside one of the defined circular grids. The most popular one probably
 * being the one from the ETDRS which creates a nine-field circular grid
 * Created by patrick on 14.02.18.
 * (c) Patrick Scheibe 2018
 */
public class RectangularThicknessGrid implements Grid {

    private final GridType type;
    private final int typeID;
    private final int numRow;
    private final int numCol;
    private final float cellWidth;
    private final float cellHeight;
    private final float tilt;
    private final double[] centerPos;
    private final Sector[] sectors;


    RectangularThicknessGrid(RandomAccessFile file, GridType gridType, int gridOffset) throws SpectralisException {
        type = gridType;
        if ((gridType != GridType.RECTANGULAR_15) && (gridType != GridType.RECTANGULAR_20) && (gridType != GridType.RECTANGULAR_POLE)) {
            throw new SpectralisException("Wrong grid type for rectangular grids");
        }

        try {
            file.seek(gridOffset);
            typeID = file.readInt();
            numRow = file.readInt();
            numCol = file.readInt();
            cellWidth = file.readFloat();
            cellHeight = file.readFloat();
            tilt = file.readFloat();
            centerPos = Util.readDoubleArray(file, 2);
            int numCells = numCol * numRow;
            sectors = new Sector[numCells];
            for (int i = 0; i < numCells; i++) {
                sectors[i] = Util.readSector(file);
            }
        } catch (IOException e) {
            throw new SpectralisException(e);
        }
    }


    @Override
    public final GridType getGridType() {
        return type;
    }

    public final int getTypeID() {
        return typeID;
    }

    public final int getNumRow() {
        return numRow;
    }

    public final int getNumCol() {
        return numCol;
    }

    public final float getCellWidth() {
        return cellWidth;
    }

    public final float getCellHeight() {
        return cellHeight;
    }

    public final float getTilt() {
        return tilt;
    }

    public final double[] getCenterPos() {
        return centerPos;
    }

    public final Sector[] getSectors() {
        return sectors;
    }
}
