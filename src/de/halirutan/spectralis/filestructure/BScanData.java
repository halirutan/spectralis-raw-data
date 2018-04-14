package de.halirutan.spectralis.filestructure;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

import de.halirutan.spectralis.SpectralisException;

/**
 * The data of a BScan are the measured reflectivity values. One BScan consist of several AScans which are the reflectivity
 * in different depths of the retina at one specific location. A BScan is therefore a collection of adjacent AScan rows
 * which is usually presented as an image where the columns are the AScans.
 * (c) Patrick Scheibe 2018
 */
@SuppressWarnings("unused")
public class BScanData {

    private static final double GRAY_VALUE_BYTE = 255.0;
    private static final double GAMMA_CORRECTOR = 0.25;
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

    /**
     * Provides an image representation of the BScan. Note that the values are transformed to create better image brightness.
     * @return Image of this BScan
     */
    public final BufferedImage getImage() {
        byte[] pixelData = new byte[width*height];
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {

                double value = (GRAY_VALUE_BYTE * StrictMath.pow(contents[j][i], GAMMA_CORRECTOR));
                //noinspection NumericCastThatLosesPrecision
                pixelData[(j * width) + i] = (byte) value;
            }
        }
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        img.setData(Raster.createRaster(img.getSampleModel(), new DataBufferByte(pixelData, pixelData.length), null));
        return img;
    }

}
