package de.halirutan.spectralis.data;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;

/**
 * Created by patrick on 10.01.17.
 * (c) Patrick Scheibe 2017
 */
public class SLOImage  {

    private final byte[] myPixelBytes;
    private final int myWidth;
    private final int myHeight;

    public SLOImage(int width, int height, byte[] pixels) {
        myWidth = width;
        myHeight = height;
        myPixelBytes = pixels;
    }

    public byte[] getPixels() {
        return myPixelBytes;
    }

    public BufferedImage getImage() {
        BufferedImage img = new BufferedImage(myWidth, myHeight, BufferedImage.TYPE_BYTE_GRAY);
        img.setData(Raster.createRaster(img.getSampleModel(), new DataBufferByte(myPixelBytes, myPixelBytes.length), null));
        return img;
    }
}