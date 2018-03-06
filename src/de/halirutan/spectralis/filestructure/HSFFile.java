package de.halirutan.spectralis.filestructure;

import de.halirutan.spectralis.SpectralisException;
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
import java.util.List;

/**
 * Created by patrick on 10.01.17.
 * (c) Patrick Scheibe 2017
 */
public class HSFFile {

    @SuppressWarnings("FieldCanBeLocal")
    private static final long SLO_FILE_OFFSET = 2048;

    private RandomAccessFile myFile;
    private FileHeader myFileHeader;
    private SLOImage mySLOImage;

    public HSFFile(final File file) throws SpectralisException {
        try {
            if (!FileHeader.isValidHSFFile(file)) {
                throw new IOException("Invalid Heyex file");
            }
            myFile = new RandomAccessFile(file, "r");
            myFileHeader = FileHeader.readHeader(myFile);
        } catch (IOException e) {
            throw new SpectralisException(e);
        }
    }

    public SLOImage getSLOImage() throws SpectralisException {
        if (mySLOImage != null) {
            return mySLOImage;
        }
        try {

            int sloWidth = getSloWidth();
            int sloHeight = getSloHeight();
            myFile.seek(SLO_FILE_OFFSET);
            SLOImageDataFragment sloFragment = new SLOImageDataFragment(sloWidth, sloHeight);
            mySLOImage = sloFragment.read(myFile);
        } catch (IOException e) {
            throw new SpectralisException(e);
        }
        return mySLOImage;
    }

    public Integer getSloHeight() {
        return myFileHeader.getIntegerValue(FileHeaderContent.SizeYSlo);
    }

    /**
     * Reads only the thickness-grid from an OCT file
     *
     * @param gridNumber Either 1 or 2 for the first or the second grid
     * @return The grid or null
     */
    public Grid getThicknessGrid(int gridNumber) throws SpectralisException {
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
                throw new SpectralisException("Invalid Grid number. Either 1 or 2 is allowed");
        }
        try {
            final GridType gridType = GridType.getGridType(myFileHeader.getIntegerValue(gridTypeID));
            final Integer offset = myFileHeader.getIntegerValue(gridOffset);
            if (gridType != GridType.NO_GRID) {
                myFile.seek(offset);
                final GridDataFragment gridDataFragment = new GridDataFragment(gridType);
                return gridDataFragment.read(myFile);
            }
        } catch (IOException e) {
            throw new SpectralisException(e);
        }
        return null;
    }

    /**
     * Get all BScans as list
     * @return List of BScans
     * @throws SpectralisException Error during reading
     */
    public List<BScan> getBScans() throws SpectralisException {
        return getBScans(0, getNumBScans());
    }

    /**
     * Get exactly one BScan
     * @param i The BScan number to read
     * @return BScan
     * @throws SpectralisException Error during reading
     */
    public BScan getBScan(int i) throws SpectralisException {
        final List<BScan> bScans = getBScans(i, 1);
        return bScans.get(0);
    }

    private List<BScan> getBScans(int start, int count) throws SpectralisException {
        try {
            int sloWidth = getSloWidth();
            int sloHeight = getSloHeight();
            final long offsetBScan = SLO_FILE_OFFSET + sloWidth * sloHeight;
            final Integer sizeX = getSizeX();
            final Integer sizeZ = getSizeZ();
            final Integer bScanHdrSize = getBScanHdrSize();
            final Integer numBScans = getNumBScans();
            if (start + count > numBScans) {
                throw new SpectralisException("Not enough available BScans in dataset");
            }

            final ArrayList<BScan> bScans = new ArrayList<>(count);

            for (int i = start; i < start+count; i++) {
                myFile.seek(offsetBScan + i * (bScanHdrSize + sizeX * sizeZ * DataTypes.Float));
                bScans.add(BScan.read(myFile, myFileHeader));
            }
            return bScans;
        } catch (IOException e) {
            throw new SpectralisException(e);
        }
    }


    public Integer getSizeX() {
        return DataFragment.getIntegerValue(myFileHeader.get(FileHeaderContent.SizeX));
    }

    public Integer getSizeZ() {
        return DataFragment.getIntegerValue(myFileHeader.get(FileHeaderContent.SizeZ));
    }

    public Integer getBScanHdrSize() {
        return DataFragment.getIntegerValue(myFileHeader.get(FileHeaderContent.BScanHdrSize));
    }

    public Integer getNumBScans() {
        return DataFragment.getIntegerValue(myFileHeader.get(FileHeaderContent.NumBScans));
    }

    public Integer getSloWidth() {
        return myFileHeader.getIntegerValue(FileHeaderContent.SizeXSlo);
    }

    public static boolean isValidHSFFile(File file) {
        return FileHeader.isValidHSFFile(file);
    }

    public FileHeader getFileHeader() {
        return myFileHeader;
    }

}
