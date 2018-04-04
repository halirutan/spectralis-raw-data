package de.halirutan.spectralis.data;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 * Created by patrick on 09.01.17.
 * (c) Patrick Scheibe 2017
 */
public class FloatDataFragment extends DataFragment<Float> {

    @Override
    public final Float read(RandomAccessFile file) throws IOException {
        ByteBuffer buffer = readIntoBuffer(file, DataTypes.Float);
        return buffer.getFloat();
    }

}
