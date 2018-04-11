package de.halirutan.spectralis.filestructure;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.halirutan.spectralis.SpectralisException;

/**
 *  Provides the main access point to a HSF file.
 * (c) Patrick Scheibe 2017
 */
public class HSFFile {

    // Special value that is used to mark a value as invalid. This happens when e.g. the segmentation of a layer was
    // not possible
    public static final float INVALID_FLOAT_VALUE = Float.intBitsToFloat(0x7F7FFFFF);

    private static final int SLO_FILE_OFFSET = 2048;
    private static final Logger LOG = Logger.getLogger("#de.halirutan.spectralis.filestructure.HSFFile");
    private final RandomAccessFile file;
    private final FileInfo info;
    private final HSFVersion version;
    private final int bScanOffset;
    private SLOImage sloImage;

    public HSFFile(File inFile) throws SpectralisException {
        version = HSFVersion.readVersion(inFile);
        if (version == HSFVersion.INVALID) {
            throw new SpectralisException("Not an Spectralis vol file.");
        }
        try {
            file = new RandomAccessFile(inFile, "r");
            info = new FileInfo(file);
            bScanOffset = SLO_FILE_OFFSET + (info.getSizeXSlo() * info.getSizeYSlo());
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Error opening file", e);
            throw new SpectralisException(e);
        }
    }

    public final SLOImage getSLOImage() throws SpectralisException {
        if (sloImage != null) {
            return sloImage;
        }
        try {
            int sloWidth = info.getSizeXSlo();
            int sloHeight = info.getSizeYSlo();
            ByteBuffer buffer = Util.readIntoBuffer(file, SLO_FILE_OFFSET, sloWidth * sloHeight);
            sloImage = new SLOImage(sloWidth, sloHeight, buffer.array());
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Error reading the SLO image", e);
            throw new SpectralisException(e);
        }
        return sloImage;
    }

    /**
     * Provides a list of all available BScan headers which contain information about the position, segmentation, etc.
     * @return List of all available BScanHeaders
     * @throws SpectralisException
     */
    public final List<BScanInfo> getBScanInfo() throws SpectralisException {
        return getBScanInfo(0, info.getNumBScans());
    }

    public final List<BScanInfo> getBScanInfo(int start, int count) throws SpectralisException {
        if ((start < 0) || (count < 1) || ((start + count) > info.getNumBScans())) {
            LOG.log(Level.WARNING, "Cannot access " + count + " BScans starting from " + start);
            throw new SpectralisException("BScan index out of bounds.");
        }
        int bsBlkSize = info.getBScanHdrSize() + (info.getSizeX() * info.getSizeZ() * DataTypes.Float);
        List<BScanInfo> result = new ArrayList<>(count);
        for (int i = start; i < (start + count); i++) {
            result.add(
                    new BScanInfo(file, bScanOffset + (i * bsBlkSize), info.getBScanHdrSize())
            );
        }
        return result;
    }

    public final BScanInfo getBScanInfo(int index) throws SpectralisException {
        return getBScanInfo(index, 1).get(0);
    }

    /**
     * Get all BScans as list
     *
     * @return List of BScans
     * @throws SpectralisException Error during reading
     */
    public final List<BScanData> getBScanData() throws SpectralisException {
        return getBScanData(0, info.getNumBScans());
    }

    /**
     * Get exactly one BScan
     *
     * @param i The BScan number to read
     * @return BScan
     * @throws SpectralisException Error during reading
     */
    public final BScanData getBScanData(int i) throws SpectralisException {
        List<BScanData> bScans = getBScanData(i, 1);
        return bScans.get(0);
    }

    private List<BScanData> getBScanData(int start, int count) throws SpectralisException {
        int bScanHdrSize = info.getBScanHdrSize();
        int sizeX = info.getSizeX();
        int sizeZ = info.getSizeZ();
        int numBScans = info.getNumBScans();
        if ((start < 0) || ((start + count) > numBScans)) {
            throw new SpectralisException("Not enough available BScans in dataset");
        }

        List<BScanData> bScans = new ArrayList<>(count);
        int bsBlkSize = bScanHdrSize + (sizeX * sizeZ * DataTypes.Float);
        for (int i = start; i < (start + count); i++) {
            // Refer to the manual if the offset calculation is too cryptic.
            // First BScan in the file is after the SLO image -> bscanOffset
            // bsBlkSize is the size of a BScan block consisting of header and data
            // The data follows after the header, so we need to add the BScanInfo size to the offset
            int offset = bScanOffset + (i * bsBlkSize) + bScanHdrSize;
            BScanData bs = new BScanData(file, offset, sizeX, sizeZ);
            bScans.add(bs);
        }
        return bScans;
    }

    public final List<LayerSegmentation> getLayerSegmentation() throws SpectralisException {
        List<BScanInfo> bScanInfo = getBScanInfo();
        List<LayerSegmentation> layers = new ArrayList<>(bScanInfo.size());
        for (BScanInfo scan : bScanInfo) {
            layers.add(new LayerSegmentation(info, scan));
        }
        return layers;
    }

    public final LayerSegmentation getLayerSegmentation(int index) throws SpectralisException {
        BScanInfo bScanInfo = getBScanInfo(index);
        return new LayerSegmentation(info, bScanInfo);
    }

    /**
     * Reads the thickness-grid from an OCT file
     *
     * @param gridNumber Either 1 or 2 for the first or the second grid
     * @return The grid or null
     */
    public final Grid getThicknessGrid(int gridNumber) throws SpectralisException {
        int gridTypeID;
        int gridOffset;
        switch (gridNumber) {
            case 1:
                gridTypeID = info.getGridType1();
                gridOffset = info.getGridOffset1();
                break;
            case 2:
                gridTypeID = info.getGridType2();
                gridOffset = info.getGridOffset2();
                break;
            default:
                throw new SpectralisException("Invalid Grid number. Either 1 or 2 is allowed");
        }
        Grid result;
        GridType gridType = GridType.getGridType(gridTypeID);
        switch (gridType) {
            case CIRCULAR_ETDRS:
            case CIRCULAR1:
            case CIRCULAR2:
                result = new CircularThicknessGrid(file, gridType, gridOffset);
                break;
            case RECTANGULAR_15:
            case RECTANGULAR_20:
            case RECTANGULAR_POLE:
                result = new RectangularThicknessGrid(file, gridType, gridOffset);
                break;
            case NO_GRID:
            default:
                throw new SpectralisException("No grid available");
        }
        return result;
    }


    public final FileInfo getInfo() {
        return info;
    }

    public final HSFVersion getVersion() {
        return version;
    }

    public final void close() {
        try {
            file.close();
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Error closing file");
        }
    }
}
