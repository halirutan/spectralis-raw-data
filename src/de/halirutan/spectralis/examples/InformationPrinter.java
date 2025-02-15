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

package de.halirutan.spectralis.examples;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.halirutan.spectralis.filestructure.BScanInfo;
import de.halirutan.spectralis.filestructure.FileInfo;
import de.halirutan.spectralis.filestructure.HSFFile;
import de.halirutan.spectralis.filestructure.HSFVersion;

/**
 * Simple example that displays information about an OCT scan
 * (c) Patrick Scheibe 2018
 */
public class InformationPrinter {

    private static final Logger LOG = Logger.getLogger("#de.halirutan.spectralis.examples.InformationPrinter");

    @SuppressWarnings("AccessOfSystemProperties") private static final String nl = System.getProperty("line.separator");
    @SuppressWarnings("StringBufferField") private static final StringBuilder builder = new StringBuilder(100);

    private static void format(String key, Object val) {
        builder.append(String.format("%-20s %s%n", key, val));
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("No file given");
            return;
        }
        File file = new File(args[0]);
        if (file.exists() && file.canRead() && (HSFVersion.readVersion(file) != HSFVersion.INVALID)) {
            try {
                HSFFile hsfFile = new HSFFile(file);
                FileInfo info = hsfFile.getInfo();
                format("Filename:", file.getName());

                format(nl + "Scan information", nl);
                format("Size X:", info.getSizeX());
                format("Number of BScans:", info.getNumBScans());
                format("Size Z:", info.getSizeZ());
                format("Scale X:", info.getScaleX());
                format("Distance:", info.getDistance());
                format("Scale Z:", info.getScaleZ());

                format(nl + "SLO information", nl);
                format("Size X SLO:", info.getSizeXSlo());
                format("Size Y SLO:", info.getSizeYSlo());
                format("Scale X SLO:", info.getScaleXSlo());
                format("Scale Y SLO:", info.getScaleYSlo());
                format("Field Size SLO:", info.getFieldSizeSlo());

                format(nl + "Other information", nl);
                format("Scan Focus:", info.getScanFocus());
                format("Scan Position:", info.getScanPosition());
                format("Exam Time:", info.getExamTime());
                format("Scan Pattern:", info.getScanPattern());
                format("BScan Hdr Size:", info.getBScanHdrSize());
                format("ID:", info.getId());
                format("Reference ID:", info.getReferenceId());
                format("PID:", info.getPid());
                format("Patient ID:", info.getPatientId());
                format("Date of birth:", info.getDob());
                format("VID:", info.getVid());
                format("Visit ID:", info.getVisitId());
                format("Visit Date:", info.getVisitDate());
                format("Grid 1 Type:", info.getGridType1());
                format("Grid 1 Offset:", info.getGridOffset1());
                format("Grid 2 Type:", info.getGridType2());
                format("Grid 2 Offset:", info.getGridOffset2());
                format("Prog ID:", info.getProgId());

                if (info.getNumBScans() > 0) {
                    BScanInfo bScanInfo = hsfFile.getBScanInfo(0);
                    format(nl + "Information of 1st BScan", nl);
                    format("Size", bScanInfo.getBScanHdrSize());
                    format("Start X", bScanInfo.getStartX());
                    format("Start Y", bScanInfo.getStartY());
                    format("End X", bScanInfo.getEndX());
                    format("End Y", bScanInfo.getEndY());
                    format("Number Segmentations", bScanInfo.getNumSeg());
                    format("Quality", bScanInfo.getQuality());
                    format("Shift", bScanInfo.getShift());
                }

                System.out.println(builder);
                hsfFile.close();
            } catch (Exception e) {
                LOG.log(Level.WARNING, "Could not open file", e);
            }
        }
    }
}

