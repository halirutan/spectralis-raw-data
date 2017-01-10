package de.halirutan.spectralis.filestructure;

import de.halirutan.spectralis.data.*;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by patrick on 09.01.17.
 * (c) Patrick Scheibe 2017
 */
public enum FileHeaderContent {
    Version(new StringDataFragment(12)),
    SizeX(new IntegerDataFragment()),
    NumBScans(new IntegerDataFragment()),
    SizeZ(new IntegerDataFragment()),
    ScaleX(new DoubleDataFragment()),
    Distance(new DoubleDataFragment()),
    ScaleZ(new DoubleDataFragment()),
    SizeXSlo(new IntegerDataFragment()),
    SizeYSlo(new IntegerDataFragment()),
    ScaleXSlo(new DoubleDataFragment()),
    ScaleYSlo(new DoubleDataFragment()),
    FieldSizeSlo(new IntegerDataFragment()),
    ScanFocus(new DoubleDataFragment()),
    ScanPosition(new StringDataFragment(4)),
    ExamTime(new ExamTimeDataFragment()),
    ScanPattern(new IntegerDataFragment()),
    BScanHdrSize(new IntegerDataFragment()),
    ID(new StringDataFragment(16)),
    ReferenceID(new StringDataFragment(16)),

    // Below here only from version 101
    PID(new IntegerDataFragment(), HSFVersion.HSF_OCT_101),
    PatientID(new StringDataFragment(21), HSFVersion.HSF_OCT_101),
    Padding(new ByteArrayDataFragment(3), HSFVersion.HSF_OCT_101),
    DOB(new DateDataFragment(), HSFVersion.HSF_OCT_101),
    VID(new IntegerDataFragment(), HSFVersion.HSF_OCT_101),
    VisitID(new StringDataFragment(24), HSFVersion.HSF_OCT_101),
    VisitDate(new DateDataFragment(), HSFVersion.HSF_OCT_101),

    // Below here only from version 102
    GridType(new IntegerDataFragment(), HSFVersion.HSF_OCT_102),
    GridOffset(new IntegerDataFragment(), HSFVersion.HSF_OCT_102),

    // Below here only from version 103
    GridType1(new IntegerDataFragment(),HSFVersion.HSF_OCT_103),
    GridOffset1(new IntegerDataFragment(),HSFVersion.HSF_OCT_103),
    ProgID(new StringDataFragment(34),HSFVersion.HSF_OCT_103);
//    Spare(new ByteArrayDataFragment(1790));

    final HSFVersion version;
    final DataFragment dataFragment;

    FileHeaderContent(DataFragment dataFragment) {
        this.version = HSFVersion.HSF_OCT_100;
        this.dataFragment = dataFragment;
    }

    FileHeaderContent(DataFragment dataFragment, HSFVersion inHSFVersion) {
        this.dataFragment = dataFragment;
        this.version = inHSFVersion;
    }

    DataFragment readData(RandomAccessFile f) throws IOException {
        dataFragment.read(f);
        return dataFragment;
    }

    public DataFragment getDataFragment() {
        return dataFragment;
    }
}
