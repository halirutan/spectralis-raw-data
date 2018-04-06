package de.halirutan.spectralis.filestructure;

import java.io.DataInput;
import java.io.IOException;

/**
 * Created by patrick on 06.04.18.
 * (c) Patrick Scheibe 2018
 */
public class RetinalLayer {

    private final float[] layer;
    private final LayerNames name;
    private final int size;

    RetinalLayer(DataInput file, int layerOffset, int layerSize, LayerNames layerName) throws IOException {
        name = layerName;
        size = layerSize;
        layer = Util.readFloatArray(file, size);
    }

    public final float[] getLayer() {
        return layer;
    }

    public final LayerNames getName() {
        return name;
    }

    public final int getSize() {
        return size;
    }
}
