package de.halirutan.spectralis.filestructure;

import de.halirutan.spectralis.Util;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by patrick on 10.01.17.
 * (c) Patrick Scheibe 2017
 */
public class HSFFileTest {
    @Test
    public void read() throws Exception {
        final HSFFile hsfFile = HSFFile.read(Util.getVolFile("valid.vol"));

    }

}