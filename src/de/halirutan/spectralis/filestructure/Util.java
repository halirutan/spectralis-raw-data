package de.halirutan.spectralis.filestructure;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Util {

    private static final ByteOrder BYTE_ORDER = ByteOrder.LITTLE_ENDIAN;


    private Util() {}

    public static float readFloat(RandomAccessFile file) {
        file.read
    }

    public static double readDouble(RandomAccessFile file) {}
    public static int readInt(RandomAccessFile file) {}
    public static float readByte(RandomAccessFile file) {}
    public static float readbyteArray(RandomAccessFile file) {}



    /**
     * Reading bytes from a file into a {@link ByteBuffer}
     * @param file Opened and readable file
     * @param size Number of bytes to be read
     * @return The bytes read, wrapped into a {@link ByteBuffer}
     * @throws IOException if something went wrong during reading
     */
    static ByteBuffer readIntoBuffer(RandomAccessFile file, int size) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(size);
        buffer.order(BYTE_ORDER);
        file.read(buffer.array(), 0, size);
        return buffer;
    }

}
