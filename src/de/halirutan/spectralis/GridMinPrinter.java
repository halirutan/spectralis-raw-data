package de.halirutan.spectralis;

import java.io.File;

import de.halirutan.spectralis.data.CircularThicknessGrid;
import de.halirutan.spectralis.data.Grid;
import de.halirutan.spectralis.filestructure.HSFFile;

/**
 * Created by patrick on 14.02.18.
 * (c) Patrick Scheibe 2018
 */
public class GridMinPrinter {
    public static void main(String[] args) {
        if (args.length < 1) {
            return;
        }
        final String fileName = args[0];
        final File file = new File(fileName);
        final Grid grid = HSFFile.getThicknessGrid(file, 1);
        if (grid != null) {
            System.out.println(grid.getGridType().getDescription());
            if (grid instanceof CircularThicknessGrid) {
                CircularThicknessGrid g = (CircularThicknessGrid) grid;
                System.out.println("Center: " + g.getCentralThickness()*1000);
                System.out.println("Central Min: " + g.getMinCentralThickness()*1000);
                System.out.println("Central Max: " + g.getMaxCentralThickness()*1000);
            }
        }
    }
}
