package de.halirutan.spectralis;

import de.halirutan.spectralis.data.SLOImage;
import de.halirutan.spectralis.filestructure.HSFFile;

import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by patrick on 11.01.17.
 * (c) Patrick Scheibe 2017
 */
public class ExportSLO {

    static Logger LOG = Logger.getLogger("#de.halirutan.spectralis.ExportSLO");

    public static void main(String[] args) {
        if (args.length != 1) {
            LOG.log(Level.WARNING, "Please specify the path to the vol file");
            return;
        }

        File file = new File(args[0]);
        if (file.exists() && file.canRead() && HSFFile.isValidHSFFile(file)) {
            try {
                HSFFile hsfFile = new HSFFile(file);
                SLOImage slo = hsfFile.getSLOImage();
                File output = new File("/tmp/out.png");
                ImageIO.write(slo.getImage(), "png", output);
                LOG.log(Level.INFO, "File saved");
            } catch (Exception e) {
                LOG.log(Level.WARNING, "Could not write file");
            }
        }
    }
}
