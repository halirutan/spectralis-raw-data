package de.halirutan.spectralis.data;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by patrick on 09.01.17.
 * (c) Patrick Scheibe 2017
 */
public class StringDataFragment extends DataFragment<String> {

    public StringDataFragment(int count) {
        super(count);
    }

    @Override
    public String read(RandomAccessFile file) throws IOException {
        byte result[] = new byte[myCount];
        file.read(result, 0, myCount);
        myValue = new String(result).trim();
        return myValue;
    }
}
