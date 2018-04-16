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

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;

/**
 * Simple class to provide an 8bit gray image
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

    public int getWidth() {
        return myWidth;
    }

    public int getHeight() {
        return myHeight;
    }
}
