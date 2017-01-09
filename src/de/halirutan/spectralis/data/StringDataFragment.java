package de.halirutan.spectralis.data;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by patrick on 09.01.17.
 * (c) Patrick Scheibe 2017
 */
public class StringDataFragment extends DataFragment<String> {

    public StringDataFragment(int myLength) {
        super(myLength);
    }

    @Override
    public String read(RandomAccessFile file) throws IOException {
        byte result[] = new byte[myLength];
        file.read(result, 0, myLength);
        myValue = new String(result).trim();
        return myValue;
    }
}
