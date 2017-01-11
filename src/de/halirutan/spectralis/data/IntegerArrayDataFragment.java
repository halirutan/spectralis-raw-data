package de.halirutan.spectralis.data;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 * Created by patrick on 09.01.17.
 * (c) Patrick Scheibe 2017
 */
public class IntegerArrayDataFragment extends DataFragment<Integer[]> {

    public IntegerArrayDataFragment() {
        super(1);
    }

    public IntegerArrayDataFragment(int length) {
        super(length);
    }

    @Override
    public Integer[] read(RandomAccessFile file) throws IOException {
        final ByteBuffer b = readIntoBuffer(file, myCount * DataTypes.Integer);
        Integer result[] = new Integer[myCount];
        for (int i = 0; i < myCount; i++) {
            result[i] = b.getInt(i*DataTypes.Integer);
        }
        myValue = result;
        return myValue;
    }
}
