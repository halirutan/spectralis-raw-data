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

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

import de.halirutan.spectralis.SpectralisException;
import de.halirutan.spectralis.UnsupportedVersionException;

/**
 * Provides information about a single BScan that contains its position, scan quality, etc.
 */
@SuppressWarnings("unused")
public class BScanInfo {
    private final RandomAccessFile file;
    private final int offset;
    private final int size;
    private static final int VERSION_BYTE_SIZE = 12;
    private static final int NUM_TRANSFORM_ENTRIES = 6;
    private final BScanVersion version;

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


    BScanInfo(RandomAccessFile inputFile, int headerOffset, int headerSize) throws SpectralisException {
        file = inputFile;
        size = headerSize;
        offset = headerOffset;

        try {
            ByteBuffer buffer = Util.readIntoBuffer(file, offset, size);

            String versionString = Util.getStringTrimmed(buffer, VERSION_BYTE_SIZE);
            version = BScanVersion.parseVersionString(versionString);
            if (version == BScanVersion.INVALID) {
                throw new SpectralisException("Cannot read BScan Header");
            }
            bScanHdrSize = buffer.getInt();
            startX = buffer.getDouble();
            startY = buffer.getDouble();
            endX = buffer.getDouble();
            endY = buffer.getDouble();
            numSeg = buffer.getInt();
            offsetSeg = buffer.getInt();

            if (version.isAtLeast(BScanVersion.HSF_BS_101)) {
                quality = buffer.getFloat();
            }

            if (version.isAtLeast(BScanVersion.HSF_BS_102)) {
                shift = buffer.getInt();
            }

            if (version.isAtLeast(BScanVersion.HSF_BS_103)) {
                transformation = Util.getFloatArray(buffer, NUM_TRANSFORM_ENTRIES);
            }

            if (version.isAtLeast(BScanVersion.HSF_BS_104)) {
                bmoCoordLeft = Util.getIntArray(buffer, 3);
                bmoCoordRight = Util.getIntArray(buffer, 3);
            }
        } catch (IOException e) {
            throw new SpectralisException(e);
        }
    }

    public final RandomAccessFile getFile() {
        return file;
    }

    public final BScanVersion getVersion() {
        return version;
    }

    final int getOffset() {
        return offset;
    }

    public final int getBScanHdrSize() {
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

    final int getOffsetSeg() {
        return offsetSeg;
    }

    public int getSize() {
        return size;
    }

    public final float getQuality() throws UnsupportedVersionException {
        if (!version.isAtLeast(BScanVersion.HSF_BS_101)) {
            throw new UnsupportedVersionException(BScanVersion.HSF_BS_101);
        }
        return quality;
    }

    public final int getShift() throws UnsupportedVersionException {
        if (!version.isAtLeast(BScanVersion.HSF_BS_102)) {
            throw new UnsupportedVersionException(BScanVersion.HSF_BS_102);
        }
        return shift;
    }

    public final float[] getTransformation() throws UnsupportedVersionException {
        if (!version.isAtLeast(BScanVersion.HSF_BS_103)) {
            throw new UnsupportedVersionException(BScanVersion.HSF_BS_103);
        }
        return transformation;
    }

    public final int[] getBmoCoordLeft() throws UnsupportedVersionException {
        if (!version.isAtLeast(BScanVersion.HSF_BS_104)) {
            throw new UnsupportedVersionException(BScanVersion.HSF_BS_104);
        }
        return bmoCoordLeft;
    }

    public final int[] getBmoCoordRight() throws UnsupportedVersionException {
        if (!version.isAtLeast(BScanVersion.HSF_BS_104)) {
            throw new UnsupportedVersionException(BScanVersion.HSF_BS_104);
        }
        return bmoCoordRight;
    }
}
