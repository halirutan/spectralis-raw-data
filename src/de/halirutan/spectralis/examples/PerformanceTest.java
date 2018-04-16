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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.halirutan.spectralis.filestructure.BScanData;
import de.halirutan.spectralis.filestructure.BScanInfo;
import de.halirutan.spectralis.filestructure.FileInfo;
import de.halirutan.spectralis.filestructure.HSFFile;
import de.halirutan.spectralis.filestructure.HSFVersion;

/**
 * Simple example that does nothing more than accessing all data from a scan
 * Created by patrick on 06.04.18.
 * (c) Patrick Scheibe 2018
 */
public class PerformanceTest {

    private static final Logger LOG = Logger.getLogger("#de.halirutan.spectralis.examples.InformationPrinter");

    private static final StringBuilder builder = new StringBuilder(100);

    private static void format(String key, Object val) {
        builder.append(String.format("%-20s %s%n", key, val));
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            LOG.log(Level.WARNING,"No input file given");
            return;
        }
        File file = new File(args[0]);
        if (file.exists() && file.canRead() && (HSFVersion.readVersion(file) != HSFVersion.INVALID)) {
            try {
                HSFFile hsfFile = new HSFFile(file);
                List<BScanInfo> allBScanInfos = hsfFile.getBScanInfo();
                List<BScanData> allBScanData = hsfFile.getBScanData();
                format("File Name", file.getName());
                format("Number BScans headers", allBScanInfos.size());
                format("Number BScans", allBScanData.size());
                System.out.println(builder);
                hsfFile.close();
            } catch (Exception e) {
                LOG.log(Level.WARNING, "Error reading file", e);
            }
        } else {
            LOG.log(Level.WARNING, "Cannot open file or no valid vol file");
        }
    }
}

