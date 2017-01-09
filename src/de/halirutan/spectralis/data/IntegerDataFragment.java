package de.halirutan.spectralis.data;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 * Created by patrick on 09.01.17.
 * (c) Patrick Scheibe 2017
 */
public class IntegerDataFragment extends DataFragment<Integer> {

    public IntegerDataFragment() {
        super(1);
    }

    @Override
    public Integer read(RandomAccessFile file) throws IOException {
        ByteBuffer b = readIntoBuffer(file, 4);
        myValue = b.getInt();
        return myValue;
    }
}
