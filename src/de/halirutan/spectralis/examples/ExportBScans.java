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

import javax.imageio.ImageIO;

import de.halirutan.spectralis.filestructure.BScanData;
import de.halirutan.spectralis.filestructure.HSFFile;
import de.halirutan.spectralis.filestructure.HSFVersion;

/**
 * Simple example that shows how to convert all BScans of a file into png images
 * (c) Patrick Scheibe 2017
 */
public class ExportBScans {

    private static final Logger LOG = Logger.getLogger("#de.halirutan.spectralis.examples.ExportBScans");
    private static final String SEPARATOR = File.separator;


    public static void main(String[] args) {
        if (args.length != 1) {
            LOG.log(Level.WARNING, "Please specify the path to the vol file");
            return;
        }

        File file = new File(args[0]);
        if (file.exists() && file.canRead() && (HSFVersion.readVersion(file) != HSFVersion.INVALID)) {
            try {
                String outputPath = file.getParent();
                HSFFile hsfFile = new HSFFile(file);
                List<BScanData> bScanData = hsfFile.getBScanData();
                int i = 0;
                for (BScanData scan: bScanData) {
                    String name = outputPath + String.format("%s%05d.png", SEPARATOR, i);
                    i++;
                    ImageIO.write(scan.getImage(), "png", new File(name));
                }
                LOG.log(Level.INFO, "File saved");
                hsfFile.close();
            } catch (Exception e) {
                LOG.log(Level.WARNING, "Could not write file", e);
            }
        }
    }
}
