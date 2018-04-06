package de.halirutan.spectralis.filestructure;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;

/**
 * Created by patrick on 10.01.17.
 * (c) Patrick Scheibe 2017
 */
public class SLOImage  {

    private final byte[] pixelData;
    private final int myWidth;
    private final int myHeight;

    SLOImage(int width, int height, byte[] pixels) {
        myWidth = width;
        myHeight = height;
        pixelData = pixels;
    }

    public final byte[] getPixelData() {
        return pixelData;
    }

    public final BufferedImage getImage() {
        BufferedImage img = new BufferedImage(myWidth, myHeight, BufferedImage.TYPE_BYTE_GRAY);
        img.setData(Raster.createRaster(img.getSampleModel(), new DataBufferByte(pixelData, pixelData.length), null));
        return img;
    }
}
