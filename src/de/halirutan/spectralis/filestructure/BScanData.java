package de.halirutan.spectralis.filestructure;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

import de.halirutan.spectralis.SpectralisException;

/**
 * Created by patrick on 06.04.18.
 * (c) Patrick Scheibe 2018
 */
public class BScanData {

    private final int offset;
    private final int width;
    private final int height;
    private final float[][] contents;

    BScanData(RandomAccessFile file, int dataOffset, int sizeX, int sizeZ) throws SpectralisException {
        offset = dataOffset;
        width = sizeX;
        height = sizeZ;
        try {
            ByteBuffer buffer = Util.readIntoBuffer(file, offset, width * height * DataTypes.Float);
            contents = new float[height][width];
            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    contents[j][i] = buffer.getFloat();
                }
            }
        } catch (IOException e) {
            throw new SpectralisException(e);
        }
    }

    public final int getOffset() {
        return offset;
    }

    public final int getWidth() {
        return width;
    }

    public final int getHeight() {
        return height;
    }

    public final float[][] getContents() {
        return contents;
    }

}
