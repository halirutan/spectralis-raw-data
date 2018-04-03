package de.halirutan.spectralis.data;

import java.io.RandomAccessFile;

import de.halirutan.spectralis.filestructure.FileHeader;
import org.junit.Test;

import static de.halirutan.spectralis.Util.VALID;
import static de.halirutan.spectralis.Util.getVolRandomAccessFile;

/**
 * Created by patrick on 10.01.17.
 * (c) Patrick Scheibe 2017
 */
public class SLOImageDataFragmentTest {
    @Test
    public final void read() throws Exception {
        RandomAccessFile volFile = getVolRandomAccessFile(VALID);
        FileHeader.readHeader(volFile);
    }

}
