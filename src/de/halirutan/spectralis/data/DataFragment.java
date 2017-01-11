package de.halirutan.spectralis.data;

import com.sun.istack.internal.Nullable;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Base class for all DataFragment implementations. A "data fragment" in this context is a binary value that is read
 * from the file. For instance the x-size of B-scan is an integer, therefore,
 * {@link de.halirutan.spectralis.filestructure.FileHeaderContent#SizeX} is initialized with an
 * {@link IntegerDataFragment}.
 *
 * @param <T> the generic type of the {@link DataFragment} used by implementing classes
 *
 * Created by patrick on 09.01.17.
 * (c) Patrick Scheibe 2017
 */
public abstract class DataFragment<T> {

    private static final ByteOrder BYTE_ORDER = ByteOrder.LITTLE_ENDIAN;

    final int myCount;
    T myValue = null;


    DataFragment(int count) {
        this.myCount = count;
    }

    /**
     * Reads the specific type from a binary file at the current file position
     * @param file Opened and readable binary file
     * @return value that is read from the file
     * @throws IOException if something went wrong during reading
     */
    public abstract T read(RandomAccessFile file) throws IOException;

    @Nullable
    public T getValue() {
        return myValue;
    }

    /**
     * Reading bytes from a file into a {@link ByteBuffer}
     * @param file Opened and readable file
     * @param size Number of bytes to be read
     * @return The bytes read, wrapped into a {@link ByteBuffer}
     * @throws IOException if something went wrong during reading
     */
    ByteBuffer readIntoBuffer(RandomAccessFile file, int size) throws IOException {
        ByteBuffer b = ByteBuffer.allocate(size);
        b.order(BYTE_ORDER);
        file.read(b.array(), 0, size);
        return b;
    }

    public static Integer getIntegerValue(DataFragment fragment) {
        if (fragment instanceof IntegerDataFragment) {
            return (Integer) fragment.getValue();
        }
        return null;
    }

    public static Float getFloatValue(DataFragment fragment) {
        if (fragment instanceof FloatDataFragment) {
            return (Float) fragment.getValue();
        }
        return null;
    }

}
