package de.halirutan.spectralis.examples;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.halirutan.spectralis.filestructure.BScanData;
import de.halirutan.spectralis.filestructure.BScanHeader;
import de.halirutan.spectralis.filestructure.FileHeader;
import de.halirutan.spectralis.filestructure.HSFFile;
import de.halirutan.spectralis.filestructure.HSFVersion;

/**
 * Does nothing more than accessing all data from a scan
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
        if (args.length == 0) {
            LOG.log(Level.WARNING,"No input file given");
            return;
        }
        File file = new File(args[0]);
        if (file.exists() && file.canRead() && (HSFVersion.readVersion(file) != HSFVersion.INVALID)) {
            try {
                HSFFile hsfFile = new HSFFile(file);
                FileHeader info = hsfFile.getInfo();
                List<BScanHeader> allBScanHeaders = hsfFile.getAllBScanHeaders();
                List<BScanData> allBScanData = hsfFile.getAllBScanData();
                format("File Name", file.getName());
                format("Number BScans headers", allBScanHeaders.size());
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

