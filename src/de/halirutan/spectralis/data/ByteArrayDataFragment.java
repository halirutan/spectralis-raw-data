package de.halirutan.spectralis.data;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by patrick on 09.01.17.
 * (c) Patrick Scheibe 2017
 */
public class ByteArrayDataFragment extends DataFragment<Byte[]> {

    public ByteArrayDataFragment(int length) {
        super(length);
    }

    public ByteArrayDataFragment() {
        super(1);
    }

    @Override
    public Byte[] read(RandomAccessFile file) throws IOException {
        byte read[] = new byte[myLength];
        Byte result[] = new Byte[myLength];
        file.read(read, 0, myLength);

        int i=0;
        for (byte b : read) {
            result[i++] = b;
        }
        myValue = result;
        return result;
    }
}
