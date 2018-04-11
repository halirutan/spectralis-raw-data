package de.halirutan.spectralis.mathematica;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import com.wolfram.jlink.Expr;
import com.wolfram.jlink.KernelLink;
import com.wolfram.jlink.StdLink;
import de.halirutan.spectralis.SpectralisException;
import de.halirutan.spectralis.UnsupportedVersionException;
import de.halirutan.spectralis.filestructure.CircularThicknessGrid;
import de.halirutan.spectralis.filestructure.FileHeader;
import de.halirutan.spectralis.filestructure.Grid;
import de.halirutan.spectralis.filestructure.GridType;
import de.halirutan.spectralis.filestructure.HSFFile;
import de.halirutan.spectralis.filestructure.RectangularThicknessGrid;
import de.halirutan.spectralis.filestructure.Sector;

/**
 * Created by patrick on 20.02.18.
 * (c) Patrick Scheibe 2018
 */
@SuppressWarnings("unused")
public class MathematicaInterface {

    private static final Expr[] EMPTY_EXPR_ARRAY = new Expr[0];
    private static final Expr RULE = new Expr(Expr.SYMBOL, "Rule");
    private static final Expr ASSOCIATION = new Expr(Expr.SYMBOL, "Association");
    private static final Expr DATE_OBJECT = new Expr(Expr.SYMBOL, "DateObject");
    private static final Expr FAILED = new Expr(Expr.SYMBOL, "$FAILED");

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

    public static Expr getInfo(String fileName) throws SpectralisException {
        HSFFile hsfFile = new HSFFile(new File(fileName));
        FileHeader info = hsfFile.getInfo();
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
}
