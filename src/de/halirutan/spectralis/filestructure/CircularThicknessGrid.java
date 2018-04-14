/*
 * Copyright (c) 2018 Patrick Scheibe
 * Affiliation: Saxonian Incubator for Clinical Translation, University Leipzig, Germany
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.halirutan.spectralis.filestructure;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

import de.halirutan.spectralis.SpectralisException;

/**
 * Provides access to the retinal thickness measurements inside one of the defined circular grids. The most popular one probably
 * being the one from the ETDRS which creates a nine-field circular grid
 * Created by patrick on 14.02.18.
 * (c) Patrick Scheibe 2018
 */
@SuppressWarnings("MethodWithMultipleLoops")
public class CircularThicknessGrid implements Grid {

    private static final int GRID_BYTE_SIZE = 132;
    private static final int NUM_SECTORS = 9;
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
            ByteBuffer buffer = Util.readIntoBuffer(file, gridOffset, GRID_BYTE_SIZE);
            typeId = buffer.getInt();
            diameters = Util.getDoubleArray(buffer, 3);
            centerPos = Util.getDoubleArray(buffer, 2);
            centralThickness = buffer.getFloat();
            minCentralThickness = buffer.getFloat();
            maxCentralThickness = buffer.getFloat();
            totalVolume = buffer.getFloat();
            sectors = new Sector[NUM_SECTORS];
            for (int i = 0; i < NUM_SECTORS; i++) {
                sectors[i] = Util.readSector(buffer);
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
