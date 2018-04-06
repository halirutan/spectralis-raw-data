package de.halirutan.spectralis.filestructure;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.EnumMap;
import java.util.Map;

import de.halirutan.spectralis.SpectralisException;

public class SegmentationLayers {

    private final Map<LayerNames, RetinalLayer> layers;

    SegmentationLayers(FileHeader fileHdr, BScanHeader info) throws SpectralisException {
        RandomAccessFile file = info.getFile();
        int offset = info.getOffset() + info.getOffsetSeg();
        int num = info.getNumSeg();
        int sizeX = fileHdr.getSizeX();
        layers = new EnumMap<>(LayerNames.class);
        LayerNames[] layerNames = LayerNames.values();
        for (int i = 0; i < num; i++) {
            RetinalLayer retinalLayer = null;
            try {
                retinalLayer = new RetinalLayer(file, offset + (i * sizeX), sizeX, layerNames[i]);
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
