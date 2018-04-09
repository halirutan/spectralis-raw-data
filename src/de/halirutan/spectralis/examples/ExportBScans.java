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
 * Created by patrick on 11.01.17.
 * (c) Patrick Scheibe 2017
 */
public class ExportBScans {

    private static Logger LOG = Logger.getLogger("#de.halirutan.spectralis.examples.ExportBScans");
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
                List<BScanData> bScanData = hsfFile.getAllBScanData();
                int i = 0;
                for (BScanData scan: bScanData) {
                    String name = outputPath + String.format("%s%05d.png", SEPARATOR, i);
                    i++;
                    ImageIO.write(scan.getImage(), "png", new File(name));
                }
                LOG.log(Level.INFO, "File saved");
            } catch (Exception e) {
                LOG.log(Level.WARNING, "Could not write file", e);
            }
        }
    }
}
