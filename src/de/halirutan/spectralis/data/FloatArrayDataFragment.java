package de.halirutan.spectralis.data;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 * Created by patrick on 09.01.17.
 * (c) Patrick Scheibe 2017
 */
public class FloatArrayDataFragment extends DataFragment<Float[]> {

    private final int size;

    public FloatArrayDataFragment() {
        this(1);
    }

    public FloatArrayDataFragment(int length) {
        size = length;
    }

    @Override
    public final Float[] read(RandomAccessFile file) throws IOException {
        ByteBuffer buffer = readIntoBuffer(file, size * DataTypes.Float);
        Float[] contents = new Float[size];
        for (int i = 0; i < size; i++) {
            contents[i] = buffer.getFloat(i * DataTypes.Float);

        }
        return contents;
    }

}
