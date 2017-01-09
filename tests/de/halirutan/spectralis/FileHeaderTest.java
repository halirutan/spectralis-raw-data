package de.halirutan.spectralis;

import de.halirutan.spectralis.filestructure.FileHeader;
import de.halirutan.spectralis.filestructure.FileHeaderContent;
import de.halirutan.spectralis.filestructure.Version;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.Assert.*;

/**
 * Created by patrick on 09.01.17.
 * (c) Patrick Scheibe 2017
 */
public class FileHeaderTest {
    final URL validURL = ClassLoader.getSystemResource("valid.vol");

    @Test
    public void testHeader() throws IOException, URISyntaxException {
        final File file = new File(validURL.toURI());
        FileHeader header = new FileHeader(file);
        for (FileHeaderContent content : header.myInfo) {
            System.out.println(content.name() + " " + content.getDataFragment().getValue());
        }
    }

    @Test
    public void readVersion() throws Exception {
        assertEquals(FileHeader.readVersion(new File(validURL.toURI())), Version.HSF_OCT_103);
    }


    @Test
    public void isValidHSFFile() throws Exception {
        File file = new File(validURL.toURI());
        assertTrue(FileHeader.isValidHSFFile(file));
    }

}