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

    private static final String nl = System.getProperty("line.separator");
    private static final StringBuilder builder = new StringBuilder(100);

    private static void format(String key, Object val) {
        builder.append(String.format("%-20s %s%n", key, val));
    }

    public static void main(String[] args) {

        File file = new File(fileName);
        if (file.exists() && file.canRead() && (HSFVersion.readVersion(file) != HSFVersion.INVALID)) {
            try {
                HSFFile hsfFile = new HSFFile(file);
                FileHeader info = hsfFile.getInfo();
                format("Filename:", file.getName());

                format(nl + "Scan information", nl);
                format("Size X:", info.getSizeX());
                format("Number of BScans:", info.getNumBScans());
                format("Size Z:", info.getSizeZ());
                format("Scale X:", info.getScaleX());
                format("Distance:", info.getDistance());
                format("Scale Z:", info.getScaleZ());

                format(nl + "SLO information", nl);
                format("Size X SLO:", info.getSizeXSlo());
                format("Size Y SLO:", info.getSizeYSlo());
                format("Scale X SLO:", info.getScaleXSlo());
                format("Scale Y SLO:", info.getScaleYSlo());
                format("Field Size SLO:", info.getFieldSizeSlo());

                format(nl + "Other information", nl);
                format("Scan Focus:", info.getScanFocus());
                format("Scan Position:", info.getScanPosition());
                format("Exam Time:", info.getExamTime());
                format("Scan Pattern:", info.getScanPattern());
                format("BScan Hdr Size:", info.getBScanHdrSize());
                format("ID:", info.getId());
                format("Reference ID:", info.getReferenceId());
                format("PID:", info.getPid());
                format("Patient ID:", info.getPatientId());
                format("Date of birth:", info.getDob());
                format("VID:", info.getVid());
                format("Visit ID:", info.getVisitId());
                format("Visit Date:", info.getVisitDate());
                format("Grid 1 Type:", info.getGridType1());
                format("Grid 1 Offset:", info.getGridOffset1());
                format("Grid 2 Type:", info.getGridType2());
                format("Grid 2 Offset:", info.getGridOffset2());
                format("Prog ID:", info.getProgId());
                System.out.println(builder);
            } catch (Exception e) {
                LOG.log(Level.WARNING, "Could not write file", e);
            }
        }
    }
}

