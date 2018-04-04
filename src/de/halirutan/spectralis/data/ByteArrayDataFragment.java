package de.halirutan.spectralis.data;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

/**
 * Created by patrick on 09.01.17.
 * (c) Patrick Scheibe 2017
 */
public class ByteArrayDataFragment extends DataFragment<Byte[]> {

    private final int size;

    public ByteArrayDataFragment(int arraySize) {
        size = arraySize;
    }

    public ByteArrayDataFragment() {
        this(1);
    }

    @Override
    public final Byte[] read(RandomAccessFile file) throws IOException {
        byte read[] = new byte[size];
        file.read(read, 0, size);
        Byte[] contents = new Byte[size];
        Arrays.setAll(contents, n -> read[n]);
        return contents;
    }
}
