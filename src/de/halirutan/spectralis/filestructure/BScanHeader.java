package de.halirutan.spectralis.filestructure;

import java.io.IOException;
import java.io.RandomAccessFile;

import de.halirutan.spectralis.SpectralisException;
import de.halirutan.spectralis.UnsupportedVersionException;

public class BScanHeader {
    private final RandomAccessFile file;
    private final int offset;
    private static final int VERSION_BYTE_SIZE = 12;
    private static final int NUM_TRANSFORM_ENTRIES = 6;
    private final HSFVersion version;

    private final int bScanHdrSize;
    private final double startX;
    private final double startY;
    private final double endX;
    private final double endY;
    private final int numSeg;
    private final int offsetSeg;
    private float quality;
    private int shift;
    private float[] transformation;
    private int[] bmoCoordLeft;
    private int[] bmoCoordRight;


    public BScanHeader(RandomAccessFile inputFile, int headerOffset) throws SpectralisException {
        file = inputFile;
        offset = headerOffset;
        try {
            file.seek(offset);
            String versionString = Util.readStringTrimmed(file, VERSION_BYTE_SIZE);
            version = HSFVersion.parseVersionString(versionString);
            if (version == HSFVersion.INVALID) {
                throw new SpectralisException("Cannot read BScan Header");
            }
            bScanHdrSize = file.readInt();
            startX = file.readDouble();
            startY = file.readDouble();
            endX = file.readDouble();
            endY = file.readDouble();
            numSeg = file.readInt();
            offsetSeg = file.readInt();

            if (version.isAtLeast(HSFVersion.HSF_OCT_101)) {
                quality = file.readFloat();
            }

            if (version.isAtLeast(HSFVersion.HSF_OCT_102)) {
                shift = file.readInt();
            }

            if (version.isAtLeast(HSFVersion.HSF_OCT_103)) {
                transformation = Util.readFloatArray(file, NUM_TRANSFORM_ENTRIES);
            }

            if (version.isAtLeast(HSFVersion.HSF_OCT_104)) {
                bmoCoordLeft = Util.readIntArray(file, 3);
                bmoCoordRight = Util.readIntArray(file, 3);
            }

        } catch (IOException e) {
            throw new SpectralisException(e);
        }
    }

    public final HSFVersion getVersion() {
        return version;
    }

    public final int getbScanHdrSize() {
        return bScanHdrSize;
    }

    public final double getStartX() {
        return startX;
    }

    public final double getStartY() {
        return startY;
    }

    public final double getEndX() {
        return endX;
    }

    public final double getEndY() {
        return endY;
    }

    public final int getNumSeg() {
        return numSeg;
    }

    public final int getOffsetSeg() {
        return offsetSeg;
    }

    public final float getQuality() throws UnsupportedVersionException {
        if (!version.isAtLeast(HSFVersion.HSF_OCT_101)) {
            throw new UnsupportedVersionException(HSFVersion.HSF_OCT_101);
        }
        return quality;
    }

    public final int getShift() throws UnsupportedVersionException {
        if (!version.isAtLeast(HSFVersion.HSF_OCT_102)) {
            throw new UnsupportedVersionException(HSFVersion.HSF_OCT_102);
        }
        return shift;
    }

    public final float[] getTransformation() throws UnsupportedVersionException {
        if (!version.isAtLeast(HSFVersion.HSF_OCT_103)) {
            throw new UnsupportedVersionException(HSFVersion.HSF_OCT_103);
        }
        return transformation;
    }

    public final int[] getBmoCoordLeft() throws UnsupportedVersionException {
        if (!version.isAtLeast(HSFVersion.HSF_OCT_104)) {
            throw new UnsupportedVersionException(HSFVersion.HSF_OCT_104);
        }
        return bmoCoordLeft;
    }

    public final int[] getBmoCoordRight() throws UnsupportedVersionException {
        if (!version.isAtLeast(HSFVersion.HSF_OCT_104)) {
            throw new UnsupportedVersionException(HSFVersion.HSF_OCT_104);
        }
        return bmoCoordRight;
    }


}
