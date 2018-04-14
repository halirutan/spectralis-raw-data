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
 * Provides access to the retinal thickness measurements inside a rectangular region.
 * (c) Patrick Scheibe 2018
 */
public class RectangularThicknessGrid implements Grid {

    @SuppressWarnings("FieldCanBeLocal") private static final int INFO_BYTE_SIZE = 40;

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
            ByteBuffer buffer = Util.readIntoBuffer(file, gridOffset, INFO_BYTE_SIZE);
            typeID = buffer.getInt();
            numRow = buffer.getInt();
            numCol = buffer.getInt();
            cellWidth = buffer.getFloat();
            cellHeight = buffer.getFloat();
            tilt = buffer.getFloat();
            centerPos = Util.getDoubleArray(buffer, 2);
            int numCells = numCol * numRow;
            ByteBuffer dataBuffer = Util.readIntoBuffer(file, gridOffset + INFO_BYTE_SIZE, numCells * DataTypes.Float * 2);
            sectors = new Sector[numCells];
            for (int i = 0; i < numCells; i++) {
                sectors[i] = Util.readSector(dataBuffer);
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
