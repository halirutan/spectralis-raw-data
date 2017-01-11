package de.halirutan.spectralis;

import de.halirutan.spectralis.data.DataFragment;
import de.halirutan.spectralis.filestructure.FileHeaderContent;
import org.junit.Test;

import java.io.File;

/**
 * Created by patrick on 10.01.17.
 * (c) Patrick Scheibe 2017
 */
public class HSFFileTest {
    @Test
    public void read() throws Exception {
        final HSFFile hsfFile = HSFFile.read(Util.getVolFile("valid.vol"));
        hsfFile.getFileName();

    }

    @Test
    public void testLargeFile() throws Exception {
        File f = new File("/home/patrick/Documents/trm/projects/2015/rauscher/data/testSubjects/B_1051_Heider_S_1078412.vol");
        long starTime = System.nanoTime();
        final HSFFile hsfFile = HSFFile.read(f);
        long endTime = System.nanoTime();
        System.out.println(DataFragment.getIntegerValue(hsfFile.getFileHeader().get(FileHeaderContent.NumBScans)));
        System.out.println("runtime in ms = " + (endTime - starTime) / 1000000);

    }

}