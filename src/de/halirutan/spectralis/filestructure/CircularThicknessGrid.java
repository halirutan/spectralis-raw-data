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
@SuppressWarnings("MethodWithMultipleLoops")
public class CircularThicknessGrid implements Grid {

    private final GridType type;
    private final int typeId;
    private final double diameters[];
    private final double centerPos[];
    private final float centralThickness;
    private final float minCentralThickness;
    private final float maxCentralThickness;
    private final float totalVolume;
    private final Sector sectors[];


    CircularThicknessGrid(RandomAccessFile file, GridType gridType, int gridOffset) throws SpectralisException {
        type = gridType;
        if ((type != GridType.CIRCULAR1) && (type != GridType.CIRCULAR2) && (type != GridType.CIRCULAR_ETDRS)) {
            throw new SpectralisException("Wrong grid type for circular grids");
        }

        try {
            file.seek(gridOffset);
            typeId = file.readInt();
            diameters = Util.readDoubleArray(file, 3);
            centerPos = Util.readDoubleArray(file, 2);
            centralThickness = file.readFloat();
            minCentralThickness = file.readFloat();
            maxCentralThickness = file.readFloat();
            totalVolume = file.readFloat();
            sectors = new Sector[9];
            for (int i = 0; i < 9; i++) {
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

    public final double[] getDiameters() {
        return diameters;
    }

    public final double[] getCenterPos() {
        return centerPos;
    }

    public final float getCentralThickness() {
        return centralThickness;
    }

    public final float getMinCentralThickness() {
        return minCentralThickness;
    }

    public final float getMaxCentralThickness() {
        return maxCentralThickness;
    }

    public final float getTotalVolume() {
        return totalVolume;
    }

    public final Sector[] getSectors() {
        return sectors;
    }

    public int getTypeId() {
        return typeId;
    }
}
