package de.halirutan.spectralis.filestructure;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 * Created by patrick on 06.04.18.
 * (c) Patrick Scheibe 2018
 */
public class RetinalLayer {

    private final float[] layer;
    private final LayerNames name;
    private final int size;

    RetinalLayer(RandomAccessFile file, int layerOffset, int layerSize, LayerNames layerName) throws IOException {
        name = layerName;
        size = layerSize;
        ByteBuffer buffer = Util.readIntoBuffer(file, layerOffset, layerSize * DataTypes.Float);
        layer = Util.getFloatArray(buffer, size);
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
