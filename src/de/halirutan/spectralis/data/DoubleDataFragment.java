package de.halirutan.spectralis.data;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 * Created by patrick on 09.01.17.
 * (c) Patrick Scheibe 2017
 */
public class DoubleDataFragment extends DataFragment<Double> {
    public DoubleDataFragment() {
        super(1);
    }

    @Override
    public Double read(RandomAccessFile file) throws IOException {
        final ByteBuffer b = readIntoBuffer(file, myCount *DataTypes.Double);
        myValue = b.getDouble();
        return myValue;
    }
}
