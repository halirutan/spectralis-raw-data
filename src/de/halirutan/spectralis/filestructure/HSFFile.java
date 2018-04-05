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
import de.halirutan.spectralis.data.SLOImageDataFragment;

/**
 * Created by patrick on 10.01.17.
 * (c) Patrick Scheibe 2017
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class HSFFile {

    @SuppressWarnings("FieldCanBeLocal")
    private static final int SLO_FILE_OFFSET = 2048;
    private static final Logger LOG = Logger.getLogger("#de.halirutan.spectralis.filestructure.HSFFile");
    private final RandomAccessFile file;
    private final FileHeader info;
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
            info = new FileHeader(file);
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
            file.seek(SLO_FILE_OFFSET);
            ByteBuffer buffer = Util.readIntoBuffer(file, sloWidth * sloHeight);
            sloImage = new SLOImage(sloWidth, sloHeight, buffer.array());
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Error reading the SLO image", e);
            throw new SpectralisException(e);
        }
        return sloImage;
    }

    /**
     * Reads only the thickness-grid from an OCT file
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
     * Get all BScans as list
     *
     * @return List of BScans
     * @throws SpectralisException Error during reading
     */
    public final List<BScan> getBScans() throws SpectralisException {
        return getBScans(0, info.getNumBScans());
    }

    /**
     * Get exactly one BScan
     *
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
            int sloWidth = info.getSizeXSlo();
            int sloHeight = info.getSizeYSlo();
            int sizeX = getSizeX();
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
                bScans.add(BScan.read(file, info));
            }
            return bScans;
        } catch (IOException e) {
            throw new SpectralisException(e);
        }
    }

    public final FileHeader getInfo() {
        return info;
    }

}
