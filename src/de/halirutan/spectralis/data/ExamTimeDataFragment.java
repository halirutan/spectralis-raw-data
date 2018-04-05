package de.halirutan.spectralis.data;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import de.halirutan.spectralis.filestructure.DataTypes;

/**
 * This is annoying. For the ExamTime the HSF specification uses a different date representation which is an
 * unsigned 64-bit integer that represents the numer of 100-nanosecond units since the beginning of
 * January 1, 1601. WTF?
 * Created by patrick on 10.01.17.
 * (c) Patrick Scheibe 2017
 */
public class ExamTimeDataFragment extends DataFragment<LocalDateTime> {

    private static final LocalDateTime START_TIME = LocalDateTime.of(1601, 1, 1, 0, 0);
    private static final long SCALE = 10000000L;

    @Override
    public final LocalDateTime read(RandomAccessFile file) throws IOException {
        ByteBuffer buffer = readIntoBuffer(file, DataTypes.ExamTime);
        long seconds = buffer.getLong()/ SCALE;
        return START_TIME.plus(seconds, ChronoUnit.SECONDS);
    }

}
