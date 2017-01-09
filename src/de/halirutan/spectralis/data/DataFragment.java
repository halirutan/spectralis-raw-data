package de.halirutan.spectralis.data;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by patrick on 09.01.17.
 * (c) Patrick Scheibe 2017
 */
public abstract class DataFragment<T> {

    protected final int myLength;
    protected T myValue = null;


    public DataFragment(int length) {
        this.myLength = length;
    }

    public abstract T read(RandomAccessFile file) throws IOException;

    public T getValue() {
        return myValue;
    }

    protected ByteBuffer readIntoBuffer(RandomAccessFile file, int size) throws IOException {
        ByteBuffer b = ByteBuffer.allocate(size);
        b.order(ByteOrder.LITTLE_ENDIAN);
        file.read(b.array(), 0, size);
        return b;
    }

}
