package de.halirutan.spectralis.filestructure;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import de.halirutan.spectralis.SpectralisException;
import de.halirutan.spectralis.data.DataFragment;
import de.halirutan.spectralis.data.DataTypes;
import de.halirutan.spectralis.data.Grid;
import de.halirutan.spectralis.data.GridDataFragment;
import de.halirutan.spectralis.data.GridType;
import de.halirutan.spectralis.data.SLOImage;
import de.halirutan.spectralis.data.SLOImageDataFragment;

/**
 * Created by patrick on 10.01.17.
 * (c) Patrick Scheibe 2017
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class HSFFile {

    @SuppressWarnings("FieldCanBeLocal")
    private static final long SLO_FILE_OFFSET = 2048;

    private final RandomAccessFile file;
    private final FileHeader fileHeader;
    private SLOImage sloImage;

    public HSFFile(File inFile) throws SpectralisException {
        try {
            if (!FileHeader.isValidHSFFile(inFile)) {
                throw new SpectralisException("Invalid Heyex file");
            }
            file = new RandomAccessFile(inFile, "r");
            fileHeader = FileHeader.readHeader(file);
        } catch (IOException e) {
            throw new SpectralisException(e);
        }
    }

    public final SLOImage getSLOImage() throws SpectralisException {
        if (sloImage != null) {
            return sloImage;
        }
        try {
            int sloWidth = getSloWidth();
            int sloHeight = getSloHeight();
            file.seek(SLO_FILE_OFFSET);
            SLOImageDataFragment sloFragment = new SLOImageDataFragment(sloWidth, sloHeight);
            sloImage = sloFragment.read(file);
        } catch (IOException e) {
            throw new SpectralisException(e);
        }
        return sloImage;
    }

    public final Integer getSloHeight() {
        return fileHeader.getIntegerValue(FileHeaderContent.SizeYSlo);
    }

    /**
     * Reads only the thickness-grid from an OCT file
     *
     * @param gridNumber Either 1 or 2 for the first or the second grid
     * @return The grid or null
     */
    public final Grid getThicknessGrid(int gridNumber) throws SpectralisException {
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
        Grid result = null;
        try {
            GridType gridType = GridType.getGridType(fileHeader.getIntegerValue(gridTypeID));
            Integer offset = fileHeader.getIntegerValue(gridOffset);
            if (gridType != GridType.NO_GRID) {
                file.seek(offset);
                GridDataFragment gridDataFragment = new GridDataFragment(gridType);
                result =  gridDataFragment.read(file);
            }
        } catch (IOException e) {
            throw new SpectralisException(e);
        }
        return result;
    }

    /**
     * Get all BScans as list
     * @return List of BScans
     * @throws SpectralisException Error during reading
     */
    public final List<BScan> getBScans() throws SpectralisException {
        return getBScans(0, getNumBScans());
    }

    /**
     * Get exactly one BScan
     * @param i The BScan number to read
     * @return BScan
     * @throws SpectralisException Error during reading
     */
    public final BScan getBScan(int i) throws SpectralisException {
        List<BScan> bScans = getBScans(i, 1);
        return bScans.get(0);
    }

    private List<BScan> getBScans(int start, int count) throws SpectralisException {
        try {
            int sloWidth = getSloWidth();
            int sloHeight = getSloHeight();
            Integer sizeX = getSizeX();
            Integer sizeZ = getSizeZ();
            Integer bScanHdrSize = getBScanHdrSize();
            Integer numBScans = getNumBScans();
            if ((start + count) > numBScans) {
                throw new SpectralisException("Not enough available BScans in dataset");
            }

            List<BScan> bScans = new ArrayList<>(count);

            long offsetBScan = SLO_FILE_OFFSET + (sloWidth * sloHeight);
            for (int i = start; i < (start + count); i++) {
                file.seek(offsetBScan + (i * (bScanHdrSize + (sizeX * sizeZ * DataTypes.Float))));
                bScans.add(BScan.read(file, fileHeader));
            }
            return bScans;
        } catch (IOException e) {
            throw new SpectralisException(e);
        }
    }


    public final Integer getSizeX() {
        return DataFragment.getIntegerValue(fileHeader.get(FileHeaderContent.SizeX));
    }

    public final Integer getSizeZ() {
        return DataFragment.getIntegerValue(fileHeader.get(FileHeaderContent.SizeZ));
    }

    private Integer getBScanHdrSize() {
        return DataFragment.getIntegerValue(fileHeader.get(FileHeaderContent.BScanHdrSize));
    }

    public final Integer getNumBScans() {
        return DataFragment.getIntegerValue(fileHeader.get(FileHeaderContent.NumBScans));
    }

    public final Integer getSloWidth() {
        return fileHeader.getIntegerValue(FileHeaderContent.SizeXSlo);
    }

    public static boolean isValidHSFFile(File file) {
        return FileHeader.isValidHSFFile(file);
    }

    public final FileHeader getFileHeader() {
        return fileHeader;
    }

    @Override
    public final String toString() {
        return "HSFFile{" + file + '}';
    }
}
