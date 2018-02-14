package de.halirutan.spectralis.filestructure;

import de.halirutan.spectralis.data.DataFragment;
import de.halirutan.spectralis.data.DataTypes;
import de.halirutan.spectralis.data.Grid;
import de.halirutan.spectralis.data.GridDataFragment;
import de.halirutan.spectralis.data.GridType;
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
    private FileHeader myFileHeader;
    private SLOImage mySLOImage;
    private ArrayList<BScan> myBScans;


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

    /**
     * Reads only the thickness-grid from an OCT file
     *
     * @param file       HSF file
     * @param gridNumber Either 1 or 2 for the first or the second grid
     * @return The grid or null
     */
    public static Grid readThicknessGrid(File file, int gridNumber) {
        FileHeaderContent gridTypeID;
        FileHeaderContent gridOffset;
        switch (gridNumber) {
            case 1:
                gridTypeID = FileHeaderContent.GridType;
                gridOffset = FileHeaderContent.GridOffset;
                break;
            case 2:
                gridTypeID = FileHeaderContent.GridType1;
                gridOffset = FileHeaderContent.GridOffset1;
                break;
            default:
                return null;
        }
        try {
            if (file.exists() && file.canRead() && isValidHSFFile(file)) {
                RandomAccessFile f = new RandomAccessFile(file, "r");
                FileHeader header = FileHeader.readHeader(f);
                final GridType gridType = GridType.getGridType(header.getIntegerValue(gridTypeID));
                final Integer offset = header.getIntegerValue(gridOffset);
                if (gridType != GridType.NO_GRID) {
                    f.seek(offset);
                    final GridDataFragment gridDataFragment = new GridDataFragment(gridType);
                    return gridDataFragment.read(f);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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

                for (int i = 0; i < numBScans; i++) {
                    f.seek(offsetBScan + i * (bscanHdrSize + sizeX * sizeZ * DataTypes.Float));
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
