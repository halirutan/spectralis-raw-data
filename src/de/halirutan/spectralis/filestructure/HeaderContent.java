package de.halirutan.spectralis.filestructure;

import de.halirutan.spectralis.data.DataFragment;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by patrick on 11.01.17.
 * (c) Patrick Scheibe 2017
 */
interface HeaderContent {

    HSFVersion getVersion();

    DataFragment readData(RandomAccessFile file) throws IOException;

    DataFragment getDataFragment();

}
