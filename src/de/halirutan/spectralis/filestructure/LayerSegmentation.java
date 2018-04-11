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
