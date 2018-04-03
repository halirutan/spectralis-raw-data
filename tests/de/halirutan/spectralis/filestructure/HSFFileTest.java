package de.halirutan.spectralis.filestructure;

import org.junit.Assert;
import org.junit.Test;

import static de.halirutan.spectralis.Util.VALID;
import static de.halirutan.spectralis.Util.getVolFile;

/**
 * Created by patrick on 10.01.17.
 * (c) Patrick Scheibe 2017
 */
public class HSFFileTest {
    @Test
    public final void read() throws Exception {
        HSFFile hsfFile = new HSFFile(getVolFile(VALID));
        Assert.assertNotNull("HSF File is null", hsfFile);
        Assert.assertNotNull("Header is null", hsfFile.getFileHeader());
    }


}
