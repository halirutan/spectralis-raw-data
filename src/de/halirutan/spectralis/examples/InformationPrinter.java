package de.halirutan.spectralis.examples;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.halirutan.spectralis.filestructure.FileHeader;
import de.halirutan.spectralis.filestructure.HSFFile;
import de.halirutan.spectralis.filestructure.HSFVersion;

/**
 * Created by patrick on 06.04.18.
 * (c) Patrick Scheibe 2018
 */
public class InformationPrinter {

    private static final Logger LOG = Logger.getLogger("#de.halirutan.spectralis.examples.InformationPrinter");
    private static String fileName = "/home/patrick/workspace/projects/spectralis-raw-data/test-data/valid.vol";

    public static void main(String[] args) {

        File file = new File(fileName);
        if (file.exists() && file.canRead() && (HSFVersion.readVersion(file) != HSFVersion.INVALID)) {
            try {
                HSFFile hsfFile = new HSFFile(file);
                FileHeader info = hsfFile.getInfo();
                System.out.println(info.getVersion());
                System.out.println(info.getSizeX());
                System.out.println(info.getNumBScans());
                System.out.println(info.getSizeZ());

            } catch (Exception e) {
                LOG.log(Level.WARNING, "Could not write file", e);
            }
        }
    }

}

