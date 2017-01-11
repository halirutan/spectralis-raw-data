package de.halirutan.spectralis.filestructure;

import de.halirutan.spectralis.data.DataFragment;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by patrick on 11.01.17.
 * (c) Patrick Scheibe 2017
 */
public class HeaderContentBase implements HeaderContent {

    private final HSFVersion version;
    private final DataFragment dataFragment;

    HeaderContentBase(DataFragment dataFragment) {
        this.version = HSFVersion.HSF_OCT_100;
        this.dataFragment = dataFragment;
    }

    HeaderContentBase(DataFragment dataFragment, HSFVersion inHSFVersion) {
        this.dataFragment = dataFragment;
        this.version = inHSFVersion;
    }


    @Override
    public HSFVersion getVersion() {
        return version;
    }

    public DataFragment readData(RandomAccessFile f) throws IOException {
        dataFragment.read(f);
        return dataFragment;
    }

    public DataFragment getDataFragment() {
        return dataFragment;
    }
}
