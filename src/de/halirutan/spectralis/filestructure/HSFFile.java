package de.halirutan.spectralis.filestructure;

import de.halirutan.spectralis.data.DataFragment;
import de.halirutan.spectralis.data.DataTypes;
import de.halirutan.spectralis.data.SLOImage;
import de.halirutan.spectralis.data.SLOImageDataFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 * Created by patrick on 10.01.17.
 * (c) Patrick Scheibe 2017
 */
public class HSFFile {

    @SuppressWarnings("FieldCanBeLocal")
    private static final long SLO_FILE_OFFSET = 2048;

    private final File myFile;
    private FileHeader myFileHeader = null;
    private SLOImage mySLOImage = null;
    private ArrayList<BScan> myBScans = null;


    private HSFFile(File myFile, FileHeader myFileHeader, SLOImage mySLOImage, ArrayList<BScan> bscans) {
        this.myFile = myFile;
        this.myFileHeader = myFileHeader;
        this.mySLOImage = mySLOImage;
        this.myBScans = bscans;
    }

    public static SLOImage readSLOImage(File file) {
        SLOImage sloImage = null;
        try {
            if (file.exists() && file.canRead() && isValidHSFFile(file)) {
                RandomAccessFile f = new RandomAccessFile(file, "r");
                FileHeader header = FileHeader.readHeader(f);

                int sloWidth = header.getIntegerValue(FileHeaderContent.SizeXSlo);
                int sloHeight = header.getIntegerValue(FileHeaderContent.SizeYSlo);
                f.seek(SLO_FILE_OFFSET);
                SLOImageDataFragment sloFragment = new SLOImageDataFragment(sloWidth, sloHeight);
                sloImage = sloFragment.read(f);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return sloImage;
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

                final long offsetBScan = SLO_FILE_OFFSET + sloWidth * sloHeight;
                final Integer sizeX = DataFragment.getIntegerValue(header.get(FileHeaderContent.SizeX));
                final Integer sizeZ = DataFragment.getIntegerValue(header.get(FileHeaderContent.SizeZ));
                final Integer bscanHdrSize = DataFragment.getIntegerValue(header.get(FileHeaderContent.BScanHdrSize));
                final Integer numBScans = DataFragment.getIntegerValue(header.get(FileHeaderContent.NumBScans));

                ArrayList<BScan> bscans = new ArrayList<>(numBScans);

                for(int i = 0; i< numBScans; i++) {
                    f.seek(offsetBScan + i*(bscanHdrSize+sizeX*sizeZ* DataTypes.Float));
                    bscans.add(BScan.read(f, header));
                }

                inst = new HSFFile(file, header, sloImage, bscans);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inst;
    }

    public static boolean isValidHSFFile(String fileName) {
        return isValidHSFFile(new File(fileName));
    }

    public static boolean isValidHSFFile(File file) {
        return FileHeader.isValidHSFFile(file);
    }

    public FileHeader getFileHeader() {
        return myFileHeader;
    }

    public SLOImage getSLOImage() {
        return mySLOImage;
    }

    public ArrayList<BScan> getBScans() {
        return myBScans;
    }

    public BScan getBScan(int i) {
        if (i < myBScans.size()) {
            return myBScans.get(i);
        }
        return null;
    }

    public String getFileName() {
        return myFile.getName();
    }
}
