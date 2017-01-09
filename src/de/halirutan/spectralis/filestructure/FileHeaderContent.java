package de.halirutan.spectralis.filestructure;

import de.halirutan.spectralis.data.*;

import java.io.File;
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
    ScanPosition(new ByteArrayDataFragment(4)),
    ExamTime(new IntegerArrayDataFragment(2)),
    ScanPattern(new IntegerDataFragment()),
    BScanHdrSize(new IntegerDataFragment()),
    ID(new ByteArrayDataFragment(16)),
    ReferenceID(new ByteArrayDataFragment(16)),
    PID(new IntegerDataFragment()),
    PatientID(new ByteArrayDataFragment(21)),
    Padding(new ByteArrayDataFragment(3)),
    DOB(new DateDataFragment()),
    VID(new IntegerDataFragment()),
    VisitID(new StringDataFragment(24)),
    VisitDate(new DateDataFragment()),
    GridType(new IntegerDataFragment()),
    GridOffset(new IntegerDataFragment()),
    GridType1(new IntegerDataFragment()),
    GridOffset1(new IntegerDataFragment()),
    ProgID(new ByteArrayDataFragment(34)),
    Spare(new ByteArrayDataFragment(1790));

    final Version version;
    final DataFragment dataFragment;

    FileHeaderContent(DataFragment dataFragment) {
        this.version = de.halirutan.spectralis.filestructure.Version.HSF_OCT_100;
        this.dataFragment = dataFragment;
    }

    FileHeaderContent(DataFragment dataFragment, Version inVersion) {
        this.dataFragment = dataFragment;
        this.version = inVersion;
    }

    void readData(RandomAccessFile f) throws IOException {
        dataFragment.read(f);
    }

    public DataFragment getDataFragment() {
        return dataFragment;
    }
}
