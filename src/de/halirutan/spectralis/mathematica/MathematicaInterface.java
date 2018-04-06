package de.halirutan.spectralis.mathematica;

import java.io.File;

import com.wolfram.jlink.KernelLink;
import com.wolfram.jlink.StdLink;
import de.halirutan.spectralis.SpectralisException;
import de.halirutan.spectralis.filestructure.CircularThicknessGrid;
import de.halirutan.spectralis.filestructure.FileHeader;
import de.halirutan.spectralis.filestructure.Grid;
import de.halirutan.spectralis.filestructure.GridType;
import de.halirutan.spectralis.filestructure.HSFFile;

/**
 * Created by patrick on 20.02.18.
 * (c) Patrick Scheibe 2018
 */
@SuppressWarnings("unused")
public class MathematicaInterface {

    public static float getGrid(String fileName) {
        try {
            HSFFile hsfFile = new HSFFile(new File(fileName));
            Grid grid = hsfFile.getThicknessGrid(1);
            KernelLink link = StdLink.getLink();
            if ((grid != null) && (link != null)) {
                if (grid.getGridType() == GridType.CIRCULAR_ETDRS) {
                        FileHeader fileHeader = hsfFile.getInfo();
                        CircularThicknessGrid g = (CircularThicknessGrid) grid;
                        return "OD".equals(fileHeader.getScanPosition()) ? g.getMinCentralThickness() : -g.getMinCentralThickness();
                }
            }
        } catch (SpectralisException e) {
            return 0.0f;
        }
        return 1.0f;
    }

    public static void getMeta(String fileName) {

    }

}
