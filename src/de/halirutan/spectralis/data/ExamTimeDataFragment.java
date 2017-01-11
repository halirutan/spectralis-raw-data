package de.halirutan.spectralis.data;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * This is annoying. For the ExamTime the HSF specification uses a different date representation which is an
 * unsigned 64-bit integer that represents the numer of 100-nanosecond units since the beginning of
 * January 1, 1601. WTF?
 * Created by patrick on 10.01.17.
 * (c) Patrick Scheibe 2017
 */
public class ExamTimeDataFragment extends DataFragment<LocalDateTime> {

    private static final LocalDateTime START_TIME = LocalDateTime.of(1601, 1, 1, 0, 0);

    public ExamTimeDataFragment() {
        super(1);
    }

    @Override
    public LocalDateTime read(RandomAccessFile file) throws IOException {
        final ByteBuffer b = readIntoBuffer(file, myCount *DataTypes.ExamTime);
        final long seconds = b.getLong()/10000000L;
        myValue = START_TIME.plus(seconds, ChronoUnit.SECONDS);
        return myValue;
    }
}
