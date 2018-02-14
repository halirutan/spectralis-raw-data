package de.halirutan.spectralis.data;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by patrick on 14.02.18.
 * (c) Patrick Scheibe 2018
 */
public class SectorDataFragment extends DataFragment<Sector> {

    SectorDataFragment() {
        super(1);
    }

    @Override
    public Sector read(RandomAccessFile file) throws IOException {
        final Float thickness = (new FloatDataFragment()).read(file);
        final Float volume = (new FloatDataFragment()).read(file);
        return new Sector(thickness, volume);

    }
}
