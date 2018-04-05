package de.halirutan.spectralis.data;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;

import de.halirutan.spectralis.filestructure.Util;

/**
 * Created by patrick on 09.01.17.
 * (c) Patrick Scheibe 2017
 */
public class StringDataFragment extends DataFragment<String> {

    private final int size;

    public StringDataFragment() {
        size = 1;
    }

    public StringDataFragment(int count) {
        size = count;
    }

    @Override
    public final String read(RandomAccessFile file) throws IOException {
        byte result[] = new byte[size];
        file.read(result, 0, size);
        return new String(result, Charset.forName(Util.CHARSET)).trim();
    }
}
