package de.halirutan.spectralis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by patrick on 10.01.17.
 * (c) Patrick Scheibe 2017
 */
public class Util {

    public static RandomAccessFile getVolRandomAccessFile(String fileName) throws URISyntaxException, FileNotFoundException {
        final File f = getVolFile(fileName);
        return new RandomAccessFile(f, "r");
    }

    public static File getVolFile(String fileName) throws URISyntaxException {
        final URL validURL = ClassLoader.getSystemResource(fileName);
        return new File(validURL.toURI());
    }

}
