package de.halirutan.spectralis.data;

import de.halirutan.spectralis.Util;
import de.halirutan.spectralis.filestructure.FileHeader;
import org.junit.Test;

import java.io.RandomAccessFile;

/**
 * Created by patrick on 10.01.17.
 * (c) Patrick Scheibe 2017
 */
public class SLOImageDataFragmentTest {
    @Test
    public void read() throws Exception {
        final RandomAccessFile volFile = Util.getVolRandomAccessFile("valid.vol");
        FileHeader.readHeader(volFile);
    }

}