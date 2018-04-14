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

import de.halirutan.spectralis.filestructure.HSFVersion;
import de.halirutan.spectralis.filestructure.SLOImage;
import de.halirutan.spectralis.filestructure.HSFFile;

import javax.imageio.ImageIO;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by patrick on 11.01.17.
 * (c) Patrick Scheibe 2017
 */
public class ExportSLO {

    static Logger LOG = Logger.getLogger("#de.halirutan.spectralis.examples.ExportSLO");

    public static void main(String[] args) {
        if (args.length != 1) {
            LOG.log(Level.WARNING, "Please specify the path to the vol file");
            return;
        }

        File file = new File(args[0]);
        if (file.exists() && file.canRead() && (HSFVersion.readVersion(file) != HSFVersion.INVALID)) {
            try {
                HSFFile hsfFile = new HSFFile(file);
                SLOImage slo = hsfFile.getSLOImage();
                File output = new File("/tmp/out.png");
                ImageIO.write(slo.getImage(), "png", output);
                LOG.log(Level.INFO, "File saved");
            } catch (Exception e) {
                LOG.log(Level.WARNING, "Could not write file", e);
            }
        }
    }
}
