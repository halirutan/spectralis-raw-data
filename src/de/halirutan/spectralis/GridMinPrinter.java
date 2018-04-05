package de.halirutan.spectralis;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.halirutan.spectralis.filestructure.CircularThicknessGrid;
import de.halirutan.spectralis.filestructure.Grid;
import de.halirutan.spectralis.filestructure.HSFFile;

/**
 * Created by patrick on 14.02.18.
 * (c) Patrick Scheibe 2018
 */
public class GridMinPrinter {

    private static Logger LOG = Logger.getLogger("#de.halirutan.spectralis.GridMinPrinter");

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
