/*
 * Copyright (c) 2018 Patrick Scheibe
 * Affiliation: Saxonian Incubator for Clinical Translation, University Leipzig, Germany
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
 *  Provides the main access point to a HSF file. All important details of the underlying file structure are exposed
 *  by this class.
 * (c) Patrick Scheibe 2017
 */
@SuppressWarnings("WeakerAccess")
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

    /**
     * General information of the scan.
     * @return Information about the OCT file
     */
    public final FileInfo getInfo() {
        return info;
    }

    /**
     * SLO stands for Scanning Laser Ophthalmoscopy and is an image that provides an overview of the retina. The
     * SLOImage is captured per default for each scan and the coordinates of all BScans are given with respect to the
     * origin of this image.
     * @return An image of the retina
     * @throws SpectralisException Error during reading
     */
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
     * @throws SpectralisException Error during reading
     */
    public final List<BScanInfo> getBScanInfo() throws SpectralisException {
        return getBScanInfo(0, info.getNumBScans());
    }

    /**
     * Provides information about a range of BScans.
     * @param start Start index of the BScan
     * @param count Number of BScanInfo that should be accessed
     * @return Information about each BScan in the specified range
     * @throws SpectralisException Error during reading
     */
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

    /**
     * Provides information about a particular BScan.
     * @param index Index of the BScan
     * @return Information about the BScan
     * @throws SpectralisException Error during reading
     */
    public final BScanInfo getBScanInfo(int index) throws SpectralisException {
        return getBScanInfo(index, 1).get(0);
    }

    /**
     * Get all BScans as list.
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
     * @param index The BScan number to read
     * @return BScan
     * @throws SpectralisException Error during reading
     */
    public final BScanData getBScanData(int index) throws SpectralisException {
        List<BScanData> bScans = getBScanData(index, 1);
        return bScans.get(0);
    }

    /**
     * Provides a range of BScans.
     * @param start Start index of the BScan
     * @param count Number of BScanInfo that should be accessed
     * @return BScans in the specified range
     * @throws SpectralisException Error during reading
     */
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

    /**
     * Provides available segmentation of retinal layers for all BScans. Note that it might happen that some layers
     * were not extracted by the OCT. This is indicated by all values being {@link HSFFile#INVALID_FLOAT_VALUE} in the
     * layer.
     * @return List of retinal layer segmentation
     * @throws SpectralisException Error during reading
     */
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
     * Reads the thickness-grid from an OCT file.
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


    /**
     * Closes the HSFFile. This should not be forgotten because once a HSFFile instance is created, its file-pointer
     * is not closed.
     */
    public final void close() {
        try {
            file.close();
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Error closing file");
        }
    }

    /**
     * Version of scan. Note that this version is not so much the version of the device it was scanned with. It is the
     * version of the software that was used to export the scan into the HSF format
     * @return Version number
     */
    public final HSFVersion getVersion() {
        return version;
    }
}
