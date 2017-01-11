package de.halirutan.spectralis.data;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 * Created by patrick on 09.01.17.
 * (c) Patrick Scheibe 2017
 */
public class FloatArrayDataFragment extends DataFragment<Float[]> {

    public FloatArrayDataFragment() {
        super(1);
    }

    public FloatArrayDataFragment(int length) {
        super(length);
    }

    @Override
    public Float[] read(RandomAccessFile file) throws IOException {
        final ByteBuffer b = readIntoBuffer(file, myCount * DataTypes.Float);
        Float result[] = new Float[myCount];
        for (int i = 0; i < myCount; i++) {
            result[i] = b.getFloat(i * DataTypes.Float);

        }
        myValue = result;
        return myValue;
    }
}
