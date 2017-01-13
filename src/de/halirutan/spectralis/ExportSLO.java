package de.halirutan.spectralis;

import de.halirutan.spectralis.data.SLOImage;
import de.halirutan.spectralis.filestructure.HSFFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * Created by patrick on 11.01.17.
 * (c) Patrick Scheibe 2017
 */
public class ExportSLO {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.printf("Please specify the path to the vol file");
            return;
        }

        File file = new File(args[0]);
        if (file.exists() && file.canRead() && HSFFile.isValidHSFFile(file)) {
            HSFFile hsfFile = HSFFile.read(file);
            SLOImage slo = hsfFile.getSLOImage();
            File output = new File("/tmp/out.png");
            try {
                ImageIO.write(slo.getImage(), "png", output);
                System.out.println("File saved");
            } catch (IOException e) {
                System.out.println("Could not write file");
            }
        }

    }

}
