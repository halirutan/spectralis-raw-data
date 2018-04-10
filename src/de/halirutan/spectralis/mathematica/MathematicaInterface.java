package de.halirutan.spectralis.mathematica;

import java.io.File;
import java.time.LocalDateTime;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.wolfram.jlink.Expr;
import com.wolfram.jlink.KernelLink;
import com.wolfram.jlink.MathLinkException;
import com.wolfram.jlink.StdLink;
import de.halirutan.spectralis.SpectralisException;
import de.halirutan.spectralis.UnsupportedVersionException;
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

    public static Expr getInfo(String fileName) throws SpectralisException {
        HSFFile hsfFile = new HSFFile(new File(fileName));
        FileHeader info = hsfFile.getInfo();
        Map<String, Expr> content = new LinkedHashMap<>();
        content.put("SizeX", new Expr(info.getSizeX()));
        content.put("NumBScans", new Expr(info.getNumBScans()));
        content.put("SizeZ", new Expr(info.getSizeZ()));
        content.put("ScaleX", new Expr(info.getScaleX()));
        content.put("Distance", new Expr(info.getDistance()));
        content.put("ScaleZ", new Expr(info.getScaleZ()));
        content.put("SizeXSlo", new Expr(info.getSizeXSlo()));
        content.put("SizeYSlo", new Expr(info.getSizeYSlo()));
        content.put("ScaleXSlo", new Expr(info.getScaleXSlo()));
        content.put("ScaleYSlo", new Expr(info.getScaleYSlo()));
        content.put("FieldSizeSlo", new Expr(info.getFieldSizeSlo()));
        content.put("ScanFocus", new Expr(info.getScanFocus()));
        content.put("ScanPosition", new Expr(info.getScanPosition()));
        content.put("ScanPattern", new Expr(info.getScanPattern()));
        content.put("Id", new Expr(info.getId()));
        content.put("ReferenceId", new Expr(info.getReferenceId()));

        try {
            content.put("Pid", new Expr(info.getPid()));
            content.put("PatientId", new Expr(info.getPatientId()));
            content.put("Vid", new Expr(info.getVid()));
            content.put("VisitId", new Expr(info.getVisitId()));
        } catch (UnsupportedVersionException ignored) {
            // We simply don't print unsupported info of newer versions
        }

        for (String key : content.keySet()) {
            
        }


    }

    private static Expr makeRule(String keys, Expr expr) {
        return new Expr(Expr.SYMBOL, "Rule"); //TODO: implement correctly

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
        Expr dateListArg = new Expr(new Expr(Expr.SYMBOL, "List"), argArray);
        return new Expr(new Expr(Expr.SYMBOL, "DateList"), new Expr[]{dateListArg});
    }

    public static Expr putRule(){
        return new Expr(Expr.SYMBOL, "True");
    }

    public static double getD(){
        return 1.0;
    }

}
