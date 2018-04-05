package de.halirutan.spectralis.data;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

import de.halirutan.spectralis.filestructure.DataTypes;

/**
 * Created by patrick on 09.01.17.
 * (c) Patrick Scheibe 2017
 */
public class DoubleDataFragment extends DataFragment<Double> {


    @Override
    public final Double read(RandomAccessFile file) throws IOException {
        ByteBuffer buffer = readIntoBuffer(file, DataTypes.Double);
        return buffer.getDouble();
    }
}
