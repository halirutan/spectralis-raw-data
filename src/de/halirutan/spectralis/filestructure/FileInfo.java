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
import java.time.LocalDateTime;

import de.halirutan.spectralis.UnsupportedVersionException;

/**
 * Provides information about a file which is stored in the file header. This contains global settings for the scan like
 * resolution, scaling, and so on.
 * (c) Patrick Scheibe 2017
 */
@SuppressWarnings({"unused", "OverlyLongMethod", "ClassWithTooManyMethods", "ClassWithoutNoArgConstructor"})
public class FileInfo {

    private static final int HEADER_OFFSET = 0;
    private static final int HEADER_SIZE = 2048;

    private static final int SCAN_POSITION_BYTE_COUNT = 4;
    private static final int ID_BYTE_COUNT = 16;
    private static final int REFERENCE_ID_BYTE_COUNT = 16;
    private static final int PATIENT_ID_BYTE_COUNT = 21;
    private static final int PADDING_ID_BYTE_COUNT = 3;
    private static final int VISIT_ID_BYTE_COUNT = 24;
    private static final int PROG_ID_BYTE_COUNT = 24;


    private final HSFVersion version;
    private final int sizeX;
    private final int numBScans;
    private final int sizeZ;
    private final double scaleX;
    private final double distance;
    private final double scaleZ;
    private final int sizeXSlo;
    private final int sizeYSlo;
    private final double scaleXSlo;
    private final double scaleYSlo;
    private final int fieldSizeSlo;
    private final double scanFocus;
    private final String scanPosition;
    private final LocalDateTime examTime;
    private final int scanPattern;
    private final int bScanHdrSize;
    private final String id;
    private final String referenceId;
    private int pid;
    private String patientId;
    private LocalDateTime dob;
    private int vid;
    private String visitId;
    private LocalDateTime visitDate;
    private int gridType1;
    private int gridOffset1;
    private int gridType2;
    private int gridOffset2;
    private String progId;

    FileInfo(RandomAccessFile file) throws IOException {
        ByteBuffer buffer = Util.readIntoBuffer(file, HEADER_OFFSET, HEADER_SIZE);

        version = HSFVersion.parseVersionString(Util.getStringTrimmed(buffer, 12));
        if (version == HSFVersion.INVALID) {
            throw new IOException("Invalid vol file. Cannot parse version.");
        }

        sizeX = buffer.getInt();
        numBScans = buffer.getInt();
        sizeZ = buffer.getInt();
        scaleX = buffer.getDouble();
        distance = buffer.getDouble();
        scaleZ = buffer.getDouble();
        sizeXSlo = buffer.getInt();
        sizeYSlo = buffer.getInt();
        scaleXSlo = buffer.getDouble();
        scaleYSlo = buffer.getDouble();
        fieldSizeSlo = buffer.getInt();
        scanFocus = buffer.getDouble();
        scanPosition = Util.getStringTrimmed(buffer, SCAN_POSITION_BYTE_COUNT);
        examTime = Util.readExamTime(buffer);
        scanPattern = buffer.getInt();
        bScanHdrSize = buffer.getInt();
        id = Util.getStringTrimmed(buffer, ID_BYTE_COUNT);
        referenceId = Util.getStringTrimmed(buffer, REFERENCE_ID_BYTE_COUNT);

        if (version.isAtLeast(HSFVersion.HSF_OCT_101)) {
            pid = buffer.getInt();
            patientId = Util.getStringTrimmed(buffer, PATIENT_ID_BYTE_COUNT);
            int skip = buffer.position() + PADDING_ID_BYTE_COUNT;
            buffer.position(skip);
            dob = Util.readDate(buffer);
            vid = buffer.getInt();
            visitId = Util.getStringTrimmed(buffer, VISIT_ID_BYTE_COUNT);
            visitDate = Util.readDate(buffer);
        }

        if (version.isAtLeast(HSFVersion.HSF_OCT_102)) {
            gridType1 = buffer.getInt();
            gridOffset1 = buffer.getInt();
        }

        if (version.isAtLeast(HSFVersion.HSF_OCT_103)) {
            gridType2 = buffer.getInt();
            gridOffset2 = buffer.getInt();
            progId = Util.getStringTrimmed(buffer, PROG_ID_BYTE_COUNT);
        }
    }

    public final HSFVersion getVersion() {
        return version;
    }

    public final int getSizeX() {
        return sizeX;
    }

    public final int getNumBScans() {
        return numBScans;
    }

    public final int getSizeZ() {
        return sizeZ;
    }

    public final double getScaleX() {
        return scaleX;
    }

    public final double getDistance() {
        return distance;
    }

    public final double getScaleZ() {
        return scaleZ;
    }

    public final int getSizeXSlo() {
        return sizeXSlo;
    }

    public final int getSizeYSlo() {
        return sizeYSlo;
    }

    public final double getScaleXSlo() {
        return scaleXSlo;
    }

    public final double getScaleYSlo() {
        return scaleYSlo;
    }

    public final int getFieldSizeSlo() {
        return fieldSizeSlo;
    }

    public final double getScanFocus() {
        return scanFocus;
    }

    public final String getScanPosition() {
        return scanPosition;
    }

    public final LocalDateTime getExamTime() {
        return examTime;
    }

    public final int getScanPattern() {
        return scanPattern;
    }

    public final int getBScanHdrSize() {
        return bScanHdrSize;
    }

    public final String getId() {
        return id;
    }

    public final String getReferenceId() {
        return referenceId;
    }

    public final int getPid() throws UnsupportedVersionException {
        if (!version.isAtLeast(HSFVersion.HSF_OCT_101)) {
            throw new UnsupportedVersionException(HSFVersion.HSF_OCT_101);
        }
        return pid;
    }

    public final String getPatientId() throws UnsupportedVersionException {
        if (!version.isAtLeast(HSFVersion.HSF_OCT_101)) {
            throw new UnsupportedVersionException(HSFVersion.HSF_OCT_101);
        }
        return patientId;
    }

    public final LocalDateTime getDob() throws UnsupportedVersionException {
        if (!version.isAtLeast(HSFVersion.HSF_OCT_101)) {
            throw new UnsupportedVersionException(HSFVersion.HSF_OCT_101);
        }
        return dob;
    }

    public final int getVid() throws UnsupportedVersionException {
        if (!version.isAtLeast(HSFVersion.HSF_OCT_101)) {
            throw new UnsupportedVersionException(HSFVersion.HSF_OCT_101);
        }
        return vid;
    }

    public final String getVisitId() throws UnsupportedVersionException {
        if (!version.isAtLeast(HSFVersion.HSF_OCT_101)) {
            throw new UnsupportedVersionException(HSFVersion.HSF_OCT_101);
        }
        return visitId;
    }

    public final LocalDateTime getVisitDate() throws UnsupportedVersionException {
        if (!version.isAtLeast(HSFVersion.HSF_OCT_101)) {
            throw new UnsupportedVersionException(HSFVersion.HSF_OCT_101);
        }
        return visitDate;
    }

    public final int getGridType1() throws UnsupportedVersionException {
        if (!version.isAtLeast(HSFVersion.HSF_OCT_102)) {
            throw new UnsupportedVersionException(HSFVersion.HSF_OCT_102);
        }
        return gridType1;
    }

    public final int getGridOffset1() throws UnsupportedVersionException {
        if (!version.isAtLeast(HSFVersion.HSF_OCT_102)) {
            throw new UnsupportedVersionException(HSFVersion.HSF_OCT_102);
        }
        return gridOffset1;
    }

    public final int getGridType2() throws UnsupportedVersionException {
        if (!version.isAtLeast(HSFVersion.HSF_OCT_103)) {
            throw new UnsupportedVersionException(HSFVersion.HSF_OCT_103);
        }
        return gridType2;
    }

    public final int getGridOffset2() throws UnsupportedVersionException {
        if (!version.isAtLeast(HSFVersion.HSF_OCT_103)) {
            throw new UnsupportedVersionException(HSFVersion.HSF_OCT_103);
        }
        return gridOffset2;
    }

    public final String getProgId() throws UnsupportedVersionException {
        if (!version.isAtLeast(HSFVersion.HSF_OCT_103)) {
            throw new UnsupportedVersionException(HSFVersion.HSF_OCT_103);
        }
        return progId;
    }
}
