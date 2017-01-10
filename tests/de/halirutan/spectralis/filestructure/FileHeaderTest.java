package de.halirutan.spectralis.filestructure;

import de.halirutan.spectralis.Util;
import org.junit.Test;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by patrick on 10.01.17.
 * (c) Patrick Scheibe 2017
 */
public class FileHeaderTest {
    private final URL validURL = ClassLoader.getSystemResource("valid.vol");

    @Test
    public void readHeader() throws Exception {
        final RandomAccessFile f = Util.getVolRandomAccessFile("valid.vol");
        final long position = 123;
        f.seek(position); // try to confuse the file reader
        FileHeader header = FileHeader.readHeader(f);
        assertEquals(0, (int) header.getIntegerValue(FileHeaderContent.GridType) );
        for (FileHeaderContent content : header.myInfo.keySet()) {
            System.out.println(content.name()+" = "+content.getDataFragment().getValue());
        }
    }

    @Test
    public void testReadHeader() throws Exception {


    }


    @Test
    public void readVersion() throws Exception {
        assertEquals(FileHeader.readVersion(new File(validURL.toURI())), HSFVersion.HSF_OCT_103);
    }


    @Test
    public void isValidHSFFile() throws Exception {
        File file = new File(validURL.toURI());
        assertTrue(FileHeader.isValidHSFFile(file));
    }

}