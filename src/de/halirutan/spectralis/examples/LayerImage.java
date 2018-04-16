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

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import de.halirutan.spectralis.SpectralisException;
import de.halirutan.spectralis.filestructure.BScanData;
import de.halirutan.spectralis.filestructure.FileInfo;
import de.halirutan.spectralis.filestructure.HSFFile;
import de.halirutan.spectralis.filestructure.LayerSegmentation;
import de.halirutan.spectralis.filestructure.RetinalLayer;

/**
 * Simple example that shows how to extract retinal layers for a BScan. Note that whether or not these layers are
 * available depends on Heyex software version you are using and the quality of the scan.
 */
public class LayerImage {

    private static final Logger LOG = Logger.getLogger("#de.halirutan.spectralis.examples.LayerImage");

    /**
     * Colors were created from a Mathematica color scheme with
     *
     * toRGB[RGBColor[r_, g_, b_]] := Module[{rr, gg, bb},
     *   {rr, gg, bb} = Round[255*#] & /@ {r, g, b};
     *   BitShiftLeft[rr, 16] + BitShiftLeft[gg, 8] + bb
     *   ]
     *
     * toRGB /@ Table[ColorData[24, i], {i, 20}]
     */
    private static final int[] COLORS = {
            15433326, 16758840, 15916143, 11264239, 5351360, 2196641, 1529470,
            11828875, 8928334, 14988735, 15433326, 16758840, 15916143, 11264239,
            5351360, 2196641, 1529470, 11828875, 8928334, 14988735
    };

    public static void main(String[] args) {
        if (args.length != 2) {
            LOG.log(Level.WARNING, "Please specify input file and a BScan index");
            return;
        }
        File file = new File(args[0]);
        Integer index = Integer.valueOf(args[1]);

        if (!file.exists()) {
            LOG.log(Level.WARNING, "Input file does not exist");
        }

        try {
            HSFFile hsfFile = new HSFFile(file);
            FileInfo info = hsfFile.getInfo();
            if ((index < 0) || (index > (info.getNumBScans() - 1))) {
                LOG.log(Level.WARNING, "BScan index is out of bounds");
                return;
            }

            LayerSegmentation layerSegmentation = hsfFile.getLayerSegmentation(index);
            BScanData bScanData = hsfFile.getBScanData(index);
            BufferedImage image = bScanData.getImage();

            BufferedImage layerImage = drawLayers(image, layerSegmentation);
            JFrame frame = new JFrame("BScan Layers");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            JPanel p = new JPanel(){
                @Override
                public void paint(Graphics g) {
                    super.paint(g);
                    g.drawImage(layerImage, 0, 0, null);
                }
            };
            p.setSize(layerImage.getWidth(), layerImage.getHeight());
            frame.setSize(layerImage.getWidth(), layerImage.getHeight());
            frame.add(p);
            frame.setVisible(true);
            hsfFile.close();
        } catch (SpectralisException e) {
            LOG.log(Level.WARNING, "Error reading input file", e);
        }
    }

    private static BufferedImage drawLayers(BufferedImage image, LayerSegmentation layerSegmentation) {
        BufferedImage rgbImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        rgbImage.getGraphics().drawImage(image, 0, 0, null);
        int colorIndex = 0;
        int invalid = Float.floatToIntBits(HSFFile.INVALID_FLOAT_VALUE);
        for (RetinalLayer layer : layerSegmentation.getLayers().values()) {
            colorIndex = (colorIndex+1) % COLORS.length;
            float[] positions = layer.getLayer();
            for (int i = 0; i < positions.length; i++) {
                if (Float.floatToIntBits(positions[i]) != invalid) {
                    rgbImage.setRGB(i, (int) positions[i], COLORS[colorIndex]);
                }
            }
        }
        return rgbImage;
    }
}
