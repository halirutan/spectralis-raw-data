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
import java.util.EnumMap;
import java.util.Map;

import de.halirutan.spectralis.SpectralisException;

/**
 * Provides access to the segmentation of different retinal layers that are accessible in each OCT BScan.
 * Use {@link HSFFile#getLayerSegmentation()} to access this information.
 */
public class LayerSegmentation {

    private final Map<LayerNames, RetinalLayer> layers;

    LayerSegmentation(FileInfo fileInfo, BScanInfo scanInfo) throws SpectralisException {
        RandomAccessFile file = scanInfo.getFile();
        int offset = scanInfo.getOffset() + scanInfo.getOffsetSeg();
        int num = scanInfo.getNumSeg();
        int sizeX = fileInfo.getSizeX();
        layers = new EnumMap<>(LayerNames.class);
        LayerNames[] layerNames = LayerNames.values();
        for (int i = 0; i < num; i++) {
            RetinalLayer retinalLayer;
            try {
                retinalLayer = new RetinalLayer(file, offset + (i * sizeX*DataTypes.Float), sizeX, layerNames[i]);
            } catch (IOException e) {
                throw new SpectralisException(e);
            }
            layers.put(layerNames[i], retinalLayer);
        }
    }

    public final Map<LayerNames, RetinalLayer> getLayers() {
        return layers;
    }
}
