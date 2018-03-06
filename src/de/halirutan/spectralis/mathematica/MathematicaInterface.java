package de.halirutan.spectralis.mathematica;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.wolfram.jlink.KernelLink;
import com.wolfram.jlink.StdLink;
import de.halirutan.spectralis.data.CircularThicknessGrid;
import de.halirutan.spectralis.data.Grid;
import de.halirutan.spectralis.data.GridType;
import de.halirutan.spectralis.data.StringDataFragment;
import de.halirutan.spectralis.filestructure.FileHeader;
import de.halirutan.spectralis.filestructure.FileHeaderContent;
import de.halirutan.spectralis.filestructure.HSFFile;

/**
 * Created by patrick on 20.02.18.
 * (c) Patrick Scheibe 2018
 */
public class MathematicaInterface {

    public static float getGrid(final String fileName) {
        File f = new File(fileName);
        HSFFile file = new HSFFile(new File(fileName));
        final Grid grid = HSFFile.getThicknessGrid(f, 1);
        final KernelLink link = StdLink.getLink();
        if (grid != null && link != null) {
            if (grid.getGridType() == GridType.CIRCULAR_ETDRS) {
                try {
                    RandomAccessFile rf = new RandomAccessFile(f, "r");
                    final FileHeader fileHeader = FileHeader.readHeader(rf);
                    final StringDataFragment stringDataFragment = (StringDataFragment) fileHeader.get(FileHeaderContent.ScanPosition);
                    CircularThicknessGrid g = (CircularThicknessGrid) grid;
                    return stringDataFragment.getValue().equals("OD") ? g.getMinCentralThickness() : -g.getMinCentralThickness();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0f;
    }

    public static void getMeta(final String fileName) {

    }

}
