package de.halirutan.spectralis.filestructure;

import de.halirutan.spectralis.data.SLOImage;
import de.halirutan.spectralis.data.SLOImageDataFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import static de.halirutan.spectralis.filestructure.FileHeader.isValidHSFFile;

/**
 * Created by patrick on 10.01.17.
 * (c) Patrick Scheibe 2017
 */
public class HSFFile {

    private static long SLO_FILE_OFFSET = 2048;

    private File myFile;
    private FileHeader myFileHeader = null;
    private SLOImage mySLOImage = null;

    private HSFFile(File myFile, FileHeader myFileHeader, SLOImage mySLOImage) {
        this.myFile = myFile;
        this.myFileHeader = myFileHeader;
        this.mySLOImage = mySLOImage;
    }

    public static HSFFile read(File file) {
        HSFFile inst = null;
        try {
            if (file.exists() && file.canRead() && isValidHSFFile(file)) {
                RandomAccessFile f = new RandomAccessFile(file, "r");
                FileHeader header = FileHeader.readHeader(f);

                int sloWidth = header.getIntegerValue(FileHeaderContent.SizeXSlo);
                int sloHeight = header.getIntegerValue(FileHeaderContent.SizeYSlo);
                f.seek(SLO_FILE_OFFSET);
                SLOImageDataFragment sloFragment = new SLOImageDataFragment(sloWidth, sloHeight);
                final SLOImage sloImage = sloFragment.read(f);

                inst = new HSFFile(file, header, sloImage);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inst;
    }

}
