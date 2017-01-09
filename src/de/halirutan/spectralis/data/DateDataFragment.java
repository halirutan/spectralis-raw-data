package de.halirutan.spectralis.data;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by patrick on 09.01.17.
 * (c) Patrick Scheibe 2017
 */
public class DateDataFragment extends DataFragment<Byte[]> {
    public DateDataFragment() {
        super(2);
    }

    @Override
    public Byte[] read(RandomAccessFile file) throws IOException {
        Byte[] result = new Byte[myLength];
        for (int i = 0; i < myLength; i++) {
            result[i] = file.readByte();
        }
        return result;
    }
}
