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

import de.halirutan.spectralis.SpectralisException;
import de.halirutan.spectralis.filestructure.CircularThicknessGrid;
import de.halirutan.spectralis.filestructure.Grid;
import de.halirutan.spectralis.filestructure.HSFFile;

/**
 * Created by patrick on 14.02.18.
 * (c) Patrick Scheibe 2018
 */
public class GridMinPrinter {

    private static Logger LOG = Logger.getLogger("#de.halirutan.spectralis.examples.GridMinPrinter");

    public static void main(String[] args) {
        if (args.length < 1) {
            return;
        }
        try {
            String fileName = args[0];
            File file = new File(fileName);
            HSFFile hsfFile = new HSFFile(file);
            Grid grid = hsfFile.getThicknessGrid( 1);
            if (grid != null) {
                System.out.println(grid.getGridType().getDescription());
                if (grid instanceof CircularThicknessGrid) {
                    CircularThicknessGrid g = (CircularThicknessGrid) grid;
                    System.out.println("Center: " + g.getCentralThickness() * 1000);
                    System.out.println("Central Min: " + g.getMinCentralThickness() * 1000);
                    System.out.println("Central Max: " + g.getMaxCentralThickness() * 1000);
                }
            }
        } catch (SpectralisException e) {
            LOG.log(Level.WARNING, "Error reading Grid", e);
        }
    }
}
