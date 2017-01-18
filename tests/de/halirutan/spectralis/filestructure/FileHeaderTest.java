package de.halirutan.spectralis.filestructure;

import de.halirutan.spectralis.Util;
import org.junit.Test;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by patrick on 10.01.17.
 * (c) Patrick Scheibe 2017
 */
public class FileHeaderTest {
    private final File valid = Util.getVolFile("valid.vol");
    private final File invalid = Util.getVolFile("invalid.vol");

    private final File willis_712 = Util.getVolFile("Willis_B_712.vol");
    private final File willis_713 = Util.getVolFile("Willis_B_713.vol");
    private final File willis_714 = Util.getVolFile("Willis_B_714.vol");
    private final File willis_715 = Util.getVolFile("Willis_B_715.vol");
    private final File willis_716_0 = Util.getVolFile("Willis_B_716_0.vol");
    private final File willis_716_2 = Util.getVolFile("Willis_B_716_2.vol");
    private final File willis_716_4 = Util.getVolFile("Willis_B_716_4.vol");


    @Test
    public void readHeader() throws Exception {
        final RandomAccessFile f = Util.getVolRandomAccessFile("valid.vol");
        final long position = 123;
        f.seek(position); // try to confuse the file reader
        FileHeader header = FileHeader.readHeader(f);
        assertEquals(0, (int) header.getIntegerValue(FileHeaderContent.GridType));
        for (FileHeaderContent content : header.myInfo.keySet()) {
            System.out.println(content.name() + " = " + content.getDataFragment().getValue());
        }
    }

    @Test
    public void testReadHeader() throws Exception {

    }

    @Test
    public void readInvalid() throws Exception {

         FileHeader.readVersion(invalid);

    }


    @Test
    public void readVersion() throws Exception {
        assertEquals(FileHeader.readVersion(valid), HSFVersion.HSF_OCT_103);
        final HSFVersion v712 = FileHeader.readVersion(willis_712);
        final HSFVersion v713 = FileHeader.readVersion(willis_713);
        final HSFVersion v714 = FileHeader.readVersion(willis_714);
        final HSFVersion v715 = FileHeader.readVersion(willis_715);
        final HSFVersion v716_0 = FileHeader.readVersion(willis_716_0);
        final HSFVersion v716_2 = FileHeader.readVersion(willis_716_2);
        final HSFVersion v716_4 = FileHeader.readVersion(willis_716_4);
        System.out.println("v712 = " + v712);
        System.out.println("v713 = " + v713);
        System.out.println("v714 = " + v714);
        System.out.println("v715 = " + v715);
        System.out.println("v716_0 = " + v716_0);
        System.out.println("v716_2 = " + v716_2);
        System.out.println("v716_4 = " + v716_4);
    }


    @Test
    public void isValidHSFFile() throws Exception {
        assertTrue(FileHeader.isValidHSFFile(valid));
    }

}