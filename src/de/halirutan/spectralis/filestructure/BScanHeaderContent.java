package de.halirutan.spectralis.filestructure;

import de.halirutan.spectralis.data.*;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by patrick on 11.01.17.
 * (c) Patrick Scheibe 2017
 */
public enum  BScanHeaderContent implements HeaderContent{
    Version(new StringDataFragment(12)),
    BScanHdrSize(new IntegerDataFragment()),
    StartX(new DoubleDataFragment()),
    StartY(new DoubleDataFragment()),
    EndX(new DoubleDataFragment()),
    EndY(new DoubleDataFragment()),
    NumSeg(new IntegerDataFragment()),
    OffSeg(new IntegerDataFragment()),

    // Version 101
    Quality(new FloatDataFragment(), HSFVersion.HSF_OCT_101),

    // Version 102
    Shift(new IntegerDataFragment(), HSFVersion.HSF_OCT_102),

    // Version 103
    IVTrafo(new FloatArrayDataFragment(6), HSFVersion.HSF_OCT_103);

    public static final String[] layerNames = {"ILM", "RPE", "NFL", "L4", "L5", "L6", "L7", "L8", "L9", "L10", "L11", "L12",
            "L13", "L14", "L15", "L16", "L17", "L18"};

    private final HSFVersion version;
    private final DataFragment dataFragment;

    BScanHeaderContent(DataFragment dataFragment) {
        this.version = HSFVersion.HSF_OCT_100;
        this.dataFragment = dataFragment;
    }

    BScanHeaderContent(DataFragment dataFragment, HSFVersion inHSFVersion) {
        this.dataFragment = dataFragment;
        this.version = inHSFVersion;
    }


    @Override
    public HSFVersion getVersion() {
        return version;
    }

    public DataFragment readData(RandomAccessFile file) throws IOException {
        dataFragment.read(file);
        return dataFragment;
    }

    public DataFragment getDataFragment() {
        return dataFragment;
    }
}
