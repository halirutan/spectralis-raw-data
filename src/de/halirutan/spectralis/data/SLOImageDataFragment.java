package de.halirutan.spectralis.data;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

import de.halirutan.spectralis.filestructure.SLOImage;

/**
 * Created by patrick on 10.01.17.
 * (c) Patrick Scheibe 2017
 */
public class SLOImageDataFragment extends DataFragment<SLOImage> {

    private final int myHeight;
    private final int myWidth;

    private SLOImageDataFragment() {
        this(0, 0);
    };

    public SLOImageDataFragment(int width, int height) {
        myWidth = width;
        myHeight = height;
    }

    @Override
    public final SLOImage read(RandomAccessFile file) throws IOException {
        ByteBuffer buffer = readIntoBuffer(file, myHeight*myWidth);
        return new SLOImage(myWidth, myHeight, buffer.array());
    }
}
