package de.halirutan.spectralis.data;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

import de.halirutan.spectralis.filestructure.DataTypes;

/**
 * Created by patrick on 09.01.17.
 * (c) Patrick Scheibe 2017
 */
public class IntegerArrayDataFragment extends DataFragment<Integer[]> {

    private final int size;

    public IntegerArrayDataFragment() {
        this(1);
    }

    public IntegerArrayDataFragment(int length) {
        size = length;
    }

    @Override
    public final Integer[] read(RandomAccessFile file) throws IOException {
        ByteBuffer buffer = readIntoBuffer(file, size * DataTypes.Integer);
        Integer[] contents = new Integer[size];
        for (int i = 0; i < size; i++) {
            contents[i] = buffer.getInt(i*DataTypes.Integer);
        }
        return contents;
    }

}
