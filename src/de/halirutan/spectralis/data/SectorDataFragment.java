package de.halirutan.spectralis.data;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by patrick on 14.02.18.
 * (c) Patrick Scheibe 2018
 */
public class SectorDataFragment extends DataFragment<Sector> {

    @Override
    public final Sector read(RandomAccessFile file) throws IOException {
        Float thickness = (new FloatDataFragment()).read(file);
        Float volume = (new FloatDataFragment()).read(file);
        return new Sector(thickness, volume);
    }
}
