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