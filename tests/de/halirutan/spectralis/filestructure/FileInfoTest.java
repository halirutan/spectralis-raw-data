package de.halirutan.spectralis.filestructure;

import org.junit.Test;

import static de.halirutan.spectralis.Util.INVALID;
import static de.halirutan.spectralis.Util.VALID;
import static de.halirutan.spectralis.Util.WILLIS_712;
import static de.halirutan.spectralis.Util.WILLIS_713;
import static de.halirutan.spectralis.Util.WILLIS_714;
import static de.halirutan.spectralis.Util.WILLIS_715;
import static de.halirutan.spectralis.Util.WILLIS_716_0;
import static de.halirutan.spectralis.Util.WILLIS_716_2;
import static de.halirutan.spectralis.Util.WILLIS_716_4;
import static de.halirutan.spectralis.Util.getVolFile;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by patrick on 10.01.17.
 * (c) Patrick Scheibe 2017
 */
public class FileInfoTest {

    @Test
    public final void readHeader() throws Exception {
        HSFFile hsfFile = new HSFFile(getVolFile(VALID));
        FileInfo info = hsfFile.getInfo();
        assertEquals(0, info.getGridType1());
    }

    @Test
    public void testReadHeader() throws Exception {

    }


    @Test
    public final void readInvalid() throws Exception {
         assertEquals(HSFVersion.INVALID, HSFVersion.readVersion(getVolFile(INVALID)));
    }


    @Test
    public final void readVersion() throws Exception {
        assertEquals(HSFVersion.HSF_OCT_103, HSFVersion.readVersion(getVolFile(VALID)));
        HSFVersion v712 = HSFVersion.readVersion(getVolFile(WILLIS_712));
        HSFVersion v713 = HSFVersion.readVersion(getVolFile(WILLIS_713));
        HSFVersion v714 = HSFVersion.readVersion(getVolFile(WILLIS_714));
        HSFVersion v715 = HSFVersion.readVersion(getVolFile(WILLIS_715));
        HSFVersion v716_0 = HSFVersion.readVersion(getVolFile(WILLIS_716_0));
        HSFVersion v716_2 = HSFVersion.readVersion(getVolFile(WILLIS_716_2));
        HSFVersion v716_4 = HSFVersion.readVersion(getVolFile(WILLIS_716_4));
        System.out.println("v712 = " + v712);
        System.out.println("v713 = " + v713);
        System.out.println("v714 = " + v714);
        System.out.println("v715 = " + v715);
        System.out.println("v716_0 = " + v716_0);
        System.out.println("v716_2 = " + v716_2);
        System.out.println("v716_4 = " + v716_4);
    }


    @Test
    public final void validHSFFile() throws Exception {
        assertNotEquals(HSFVersion.INVALID, HSFVersion.readVersion(getVolFile(VALID)));
    }

}
