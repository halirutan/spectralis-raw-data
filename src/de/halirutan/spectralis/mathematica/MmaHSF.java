/*
 * Copyright (c) 2018 Patrick Scheibe
 * Affiliation: Saxonian Incubator for Clinical Translation, University Leipzig, Germany
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.halirutan.spectralis.mathematica;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.wolfram.jlink.Expr;
import de.halirutan.spectralis.SpectralisException;
import de.halirutan.spectralis.UnsupportedVersionException;
import de.halirutan.spectralis.filestructure.BScanData;
import de.halirutan.spectralis.filestructure.BScanInfo;
import de.halirutan.spectralis.filestructure.CircularThicknessGrid;
import de.halirutan.spectralis.filestructure.FileInfo;
import de.halirutan.spectralis.filestructure.Grid;
import de.halirutan.spectralis.filestructure.HSFFile;
import de.halirutan.spectralis.filestructure.LayerNames;
import de.halirutan.spectralis.filestructure.RectangularThicknessGrid;
import de.halirutan.spectralis.filestructure.RetinalLayer;
import de.halirutan.spectralis.filestructure.SLOImage;
import de.halirutan.spectralis.filestructure.Sector;
import de.halirutan.spectralis.filestructure.LayerSegmentation;

/**
 * Simple wrapper functions for the Spectralis API that allows to call it from within Mathematica.
 * The general rule is that each function opens a file, extracts information, and closes the file. This is opposed to
 * opening and loading the complete information within a file.
 * (c) Patrick Scheibe 2018
 */
@SuppressWarnings("unused")
public class MmaHSF {

    private static final Expr[] EMPTY_EXPR_ARRAY = new Expr[0];
    private static final Expr RULE = new Expr(Expr.SYMBOL, "Rule");
    private static final Expr ASSOCIATION = new Expr(Expr.SYMBOL, "Association");
    private static final Expr PARTITION = new Expr(Expr.SYMBOL, "Partition");
    private static final Expr DATE_OBJECT = new Expr(Expr.SYMBOL, "DateObject");
    private static final Expr IMAGE = new Expr(Expr.SYMBOL, "Image");
    private static final Expr FAILED = new Expr(Expr.SYMBOL, "$FAILED");

    /**
     * Provides the general information about a scan. This is basically most of the information that can be found in the
     * file header.
     * @param fileName Input file
     * @return An association with information about the scan
     * @throws SpectralisException When opening or reading the file failed
     */
    public static Expr getInfo(String fileName) throws SpectralisException {
        HSFFile hsfFile = new HSFFile(new File(fileName));
        FileInfo info = hsfFile.getInfo();
        Collection<Expr> content = new ArrayList<>();
        content.add(makeRule("SizeX", new Expr(info.getSizeX())));
        content.add(makeRule("NumBScans", new Expr(info.getNumBScans())));
        content.add(makeRule("SizeZ", new Expr(info.getSizeZ())));
        content.add(makeRule("ScaleX", new Expr(info.getScaleX())));
        content.add(makeRule("Distance", new Expr(info.getDistance())));
        content.add(makeRule("ScaleZ", new Expr(info.getScaleZ())));
        content.add(makeRule("SizeXSlo", new Expr(info.getSizeXSlo())));
        content.add(makeRule("SizeYSlo", new Expr(info.getSizeYSlo())));
        content.add(makeRule("ScaleXSlo", new Expr(info.getScaleXSlo())));
        content.add(makeRule("ScaleYSlo", new Expr(info.getScaleYSlo())));
        content.add(makeRule("FieldSizeSlo", new Expr(info.getFieldSizeSlo())));
        content.add(makeRule("ScanFocus", new Expr(info.getScanFocus())));
        content.add(makeRule("ScanPosition", new Expr(info.getScanPosition())));
        content.add(makeRule("ScanPattern", new Expr(info.getScanPattern())));
        content.add(makeRule("Id", new Expr(info.getId())));
        content.add(makeRule("ExamTime", dateToExpression(info.getExamTime())));
        content.add(makeRule("DOB", dateToExpression(info.getDob())));
        content.add(makeRule("ReferenceId", new Expr(info.getReferenceId())));

        try {
            content.add(makeRule("Pid", new Expr(info.getPid())));
            content.add(makeRule("PatientId", new Expr(info.getPatientId())));
            content.add(makeRule("Vid", new Expr(info.getVid())));
            content.add(makeRule("VisitId", new Expr(info.getVisitId())));
            content.add(makeRule("VisitDate", dateToExpression(info.getVisitDate())));
            content.add(makeRule("GridType1", new Expr(info.getGridType1())));
            content.add(makeRule("GridType2", new Expr(info.getGridType2())));
        } catch (UnsupportedVersionException ignored) {
            // We simply don't print unsupported info of newer versions
        }
        hsfFile.close();
        return new Expr(ASSOCIATION, content.toArray(EMPTY_EXPR_ARRAY));
    }

    /**
     * Provides the SLO image as Mathematica Image
     * @param fileName Input file
     * @return SLO image
     * @throws SpectralisException When opening or reading the file failed
     */
    public static Expr getSLOImage(String fileName) throws SpectralisException {
        HSFFile hsfFile = new HSFFile(new File(fileName));
        SLOImage sloImage = hsfFile.getSLOImage();
        hsfFile.close();
        return sloImageToExpr(sloImage);
    }

    /**
     * Provides a list with information about each BScan. This is the information that can be found in the header of each
     * BScan.
     * @param fileName Input file
     * @return A List with information about each BScan
     * @throws SpectralisException When opening or reading the file failed
     */
    public static Expr getBScanInfo(String fileName) throws SpectralisException {
        HSFFile hsfFile = new HSFFile(new File(fileName));
        List<BScanInfo> bScanInfo = hsfFile.getBScanInfo();
        int size = bScanInfo.size();
        Expr[] infoList = new Expr[size];
        for (int i = 0; i < size; i++) {
            infoList[i] = bScanInfoToExpr(bScanInfo.get(i));
        }
        hsfFile.close();
        return new Expr(Expr.SYM_LIST, infoList);
    }

    /**
     * Provides information about one specific BScan. This is the information that can be found in the header of each
     * BScan.
     * @param fileName Input file
     * @param index Index of the BScan. Mathematica indexing is used and the possible range is from 1 to number of BScans
     * @return Information about the BScan at index
     * @throws SpectralisException When opening or reading the file failed
     */
    public static Expr getBScanInfo(String fileName, int index) throws SpectralisException {
        HSFFile hsfFile = new HSFFile(new File(fileName));
        BScanInfo bScanInfo = hsfFile.getBScanInfo(index - 1);
        hsfFile.close();
        return bScanInfoToExpr(bScanInfo);
    }

    /**
     * Provides the raw scan values of one BScan.
     * @param fileName Input file
     * @return BScan data. Note that for an image representation you should use {@code Image[data^0.25]}.
     * @throws SpectralisException When opening or reading the file failed
     */
    public static float[][][] getBScanData(String fileName) throws SpectralisException {
        HSFFile hsfFile = new HSFFile(new File(fileName));
        List<BScanData> bScanData = hsfFile.getBScanData();
        int size = bScanData.size();
        float[][][] result = new float[size][][];
        for (int i = 0; i < size; i++) {
            result[i] = bScanData.get(i).getContents();
        }
        hsfFile.close();
        return result;
    }

    /**
     * Provides the raw scan values of one BScan.
     * @param fileName Input file
     * @param index Index of the BScan. Mathematica indexing is used and the possible range is from 1 to number of BScans
     * @return BScan data. Note that for an image representation you should use {@code Image[data^0.25]}
     * @throws SpectralisException When opening or reading the file failed
     */
    public static float[][] getBScanData(String fileName, int index) throws SpectralisException {
        HSFFile hsfFile = new HSFFile(new File(fileName));
        BScanData bScan = hsfFile.getBScanData(index - 1);
        hsfFile.close();
        return bScan.getContents();
    }

    /**
     * Provides a list of all retinal layers that are segmented by the Spectralis OCT
     * @param fileName Input file
     * @return List of Associations which pixel values for each retinal layer
     * @throws SpectralisException When opening or reading the file failed
     */
    public static Expr getLayerSegmentation(String fileName) throws SpectralisException {
        HSFFile hsfFile = new HSFFile(new File(fileName));
        List<LayerSegmentation> layerSegmentationLayers = hsfFile.getLayerSegmentation();
        List<Expr> result = layerSegmentationLayers.stream().map(MmaHSF::segmentationLayerToExpr).collect(Collectors.toList());
        hsfFile.close();
        return new Expr(Expr.SYM_LIST, result.toArray(EMPTY_EXPR_ARRAY));
    }

    /**
     * Provides a list of all retinal layers that are segmented by the Spectralis OCT
     * @param fileName Input file
     * @param index Index of the BScan. Mathematica indexing is used and the possible range is from 1 to number of BScans
     * @return List of Associations which pixel values for each retinal layer
     * @throws SpectralisException When opening or reading the file failed
     */
    public static Expr getLayerSegmentation(String fileName, int index) throws SpectralisException {
        HSFFile hsfFile = new HSFFile(new File(fileName));
        LayerSegmentation segmentation = hsfFile.getLayerSegmentation(index - 1);
        hsfFile.close();
        return segmentationLayerToExpr(segmentation);
    }

    /**
     * Provides a list of all retinal layers that are segmented by the Spectralis OCT
     * @param fileName Input file
     * @param gridNumber Either 1 or 2 for the first and second grid respectively
     * @return Associations which the grid information
     * @throws SpectralisException When opening or reading the file failed
     */
    public static Expr getGrid(String fileName, int gridNumber) throws SpectralisException {
        HSFFile hsfFile = new HSFFile(new File(fileName));
        Grid thicknessGrid = hsfFile.getThicknessGrid(gridNumber);
        Expr result = FAILED;
        //noinspection ChainOfInstanceofChecks
        if (thicknessGrid instanceof CircularThicknessGrid) {
            result = circularGridToExpr((CircularThicknessGrid) thicknessGrid);
        } else if (thicknessGrid instanceof RectangularThicknessGrid) {
            result = rectangularGridToExpr((RectangularThicknessGrid) thicknessGrid);
        }
        hsfFile.close();
        return result;
    }

    /**
     * Provides the {@code INVALID} value that is used in e.g. the retinal layers to indicate that a value could not be obtained.
     * With this, it is easy to replace invalid values with more sensible ones for specific situations.
     * @return {@code INVALID} value as specified in the Spectralis manual
     */
    public static Expr getInvalidFloatValue() {
        return new Expr(HSFFile.INVALID_FLOAT_VALUE);
    }

    private static Expr makeRule(String keys, Expr expr) {
        return new Expr(RULE, new Expr[]{new Expr(keys), expr});
    }

    private static Expr sectorToArray(Sector sector) {
        Expr thickness = makeRule("Thickness", new Expr(sector.getThickness()));
        Expr volume = makeRule("Volume", new Expr(sector.getVolume()));
        return new Expr(Expr.SYM_LIST, new Expr[]{
                thickness,
                volume
        });
    }

    private static Expr segmentationLayerToExpr(LayerSegmentation layers) {
        Map<LayerNames, RetinalLayer> retinalLayers = layers.getLayers();
        Collection<Expr> layerExprs = new ArrayList<>(retinalLayers.size());
        for (RetinalLayer l : retinalLayers.values()) {
            layerExprs.add(makeRule(
                    l.getName().name(),
                    floatArrayToExpr(l.getLayer())
                    )
            );
        }
        return new Expr(ASSOCIATION, layerExprs.toArray(EMPTY_EXPR_ARRAY));
    }

    private static Expr sloImageToExpr(SLOImage img) {
        int nx = img.getWidth();
        byte[] pixelData = img.getPixelData();
        int[] data = new int[pixelData.length];
        for (int i = 0; i < data.length; i++) {
            data[i] = pixelData[i] & 0xFF;
        }

        Expr[] dataExpr = new Expr[2];
        dataExpr[0] = new Expr(data);
        dataExpr[1] = new Expr(nx);
        Expr partition = new Expr(PARTITION, dataExpr);
        return new Expr(IMAGE, new Expr[]{partition, new Expr("Byte")});
    }

    private static Expr dateToExpression(LocalDateTime dateTime) {
        Collection<Expr> dateArgs = new ArrayList<>(5);
        dateArgs.add(new Expr(dateTime.getYear()));
        dateArgs.add(new Expr(dateTime.getMonthValue()));
        dateArgs.add(new Expr(dateTime.getDayOfMonth()));
        dateArgs.add(new Expr(dateTime.getHour()));
        dateArgs.add(new Expr(dateTime.getMinute()));
        Expr[] argArray = new Expr[dateArgs.size()];
        dateArgs.toArray(argArray);
        Expr dateListArg = new Expr(Expr.SYM_LIST, argArray);
        return new Expr(DATE_OBJECT, new Expr[]{dateListArg});
    }

    private static Expr bScanInfoToExpr(BScanInfo bScanInfo) {
        Collection<Expr> content = new ArrayList<>();
        content.add(makeRule("Version", new Expr(bScanInfo.getVersion().getVersionString())));
        content.add(makeRule("StartX", new Expr(bScanInfo.getStartX())));
        content.add(makeRule("StartY", new Expr(bScanInfo.getStartY())));
        content.add(makeRule("EndX", new Expr(bScanInfo.getEndX())));
        content.add(makeRule("EndY", new Expr(bScanInfo.getEndY())));
        content.add(makeRule("NumSeg", new Expr(bScanInfo.getNumSeg())));
        // info that might not be available in all versions
        try {
            content.add(makeRule("Quality", new Expr(bScanInfo.getQuality())));
            content.add(makeRule("Shift", new Expr(bScanInfo.getShift())));
            content.add(makeRule("IVTrafo", floatArrayToExpr(bScanInfo.getTransformation())));
            content.add(makeRule("BmoCoordLeft", new Expr(bScanInfo.getBmoCoordLeft())));
            content.add(makeRule("BmoCoordRight", new Expr(bScanInfo.getBmoCoordRight())));
        } catch (UnsupportedVersionException ignored) {
            // Do nothing. Just don't include info that is not yet available
        }
        return new Expr(ASSOCIATION, content.toArray(EMPTY_EXPR_ARRAY));
    }

    private static Expr floatArrayToExpr(float[] arr) {
        double[] result = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[i];
        }
        return new Expr(result);
    }

    private static Expr circularGridToExpr(CircularThicknessGrid grid) {
        Collection<Expr> content = new ArrayList<>();
        content.add(makeRule("Type", new Expr(grid.getTypeId())));
        content.add(makeRule("Description", new Expr(grid.getGridType().getDescription())));
        content.add(makeRule("Diameters", new Expr(grid.getDiameters())));
        content.add(makeRule("CenterPos", new Expr(grid.getCenterPos())));
        content.add(makeRule("CentralThickness", new Expr(grid.getCentralThickness())));
        content.add(makeRule("MinCentralThickness", new Expr(grid.getMinCentralThickness())));
        content.add(makeRule("MaxCentralThickness", new Expr(grid.getMaxCentralThickness())));
        content.add(makeRule("TotalVolume", new Expr(grid.getTotalVolume())));
        int numSectors = grid.getSectors().length;
        Expr[] sectorList = new Expr[numSectors];
        for (int i = 0; i < numSectors; i++) {
            sectorList[i] = sectorToArray(grid.getSectors()[i]);
        }
        content.add(makeRule("Sectors", new Expr(Expr.SYM_LIST, sectorList)));
        return new Expr(ASSOCIATION, content.toArray(EMPTY_EXPR_ARRAY));
    }

    private static Expr rectangularGridToExpr(RectangularThicknessGrid grid) {
        Collection<Expr> content = new ArrayList<>();
        content.add(makeRule("Type", new Expr(grid.getTypeID())));
        content.add(makeRule("Description", new Expr(grid.getGridType().getDescription())));
        content.add(makeRule("NumRow", new Expr(grid.getNumRow())));
        content.add(makeRule("NumCol", new Expr(grid.getNumCol())));
        content.add(makeRule("CellWidth", new Expr(grid.getCellWidth())));
        content.add(makeRule("CellHeight", new Expr(grid.getCellHeight())));
        content.add(makeRule("Tilt", new Expr(grid.getTilt())));
        content.add(makeRule("CenterPos", new Expr(grid.getCenterPos())));
        int numSectors = grid.getSectors().length;
        Expr[] sectorList = new Expr[numSectors];
        for (int i = 0; i < numSectors; i++) {
            sectorList[i] = sectorToArray(grid.getSectors()[i]);
        }
        content.add(makeRule("Sectors", new Expr(Expr.SYM_LIST, sectorList)));
        return new Expr(ASSOCIATION, content.toArray(EMPTY_EXPR_ARRAY));
    }
}
