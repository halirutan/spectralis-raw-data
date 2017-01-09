package de.halirutan.spectralis.data;

import java.io.IOException;
import java.io.RandomAccessFile;

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
        Integer result[] = new Integer[myLength];
        for (int i = 0; i < myLength; i++) {
            result[i] = file.readInt();
        }
        myValue = result;
        return myValue;
    }
}
