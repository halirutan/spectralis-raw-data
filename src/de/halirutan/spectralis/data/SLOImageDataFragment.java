package de.halirutan.spectralis.data;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 * Created by patrick on 10.01.17.
 * (c) Patrick Scheibe 2017
 */
public class SLOImageDataFragment extends DataFragment<SLOImage> {

    private final int myHeight;
    private final int myWidth;

    public SLOImageDataFragment(int width, int height) {
        super(width*height);
        myWidth = width;
        myHeight = height;
    }

    @Override
    public SLOImage read(RandomAccessFile file) throws IOException {
        final ByteBuffer b = readIntoBuffer(file, myLength);
        return new SLOImage(myWidth, myHeight, b.array());
    }
}
