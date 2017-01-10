package de.halirutan.spectralis.data;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;

/**
 * Created by patrick on 09.01.17.
 * (c) Patrick Scheibe 2017
 */
public class DateDataFragment extends DataFragment<LocalDateTime> {
    public DateDataFragment() {
        super(8);
    }

    @Override
    public LocalDateTime read(RandomAccessFile file) throws IOException {
        final ByteBuffer b = readIntoBuffer(file, myLength);
        myValue = COMDate.toLocalDateTime(b.getDouble());
        return myValue;
    }
}
