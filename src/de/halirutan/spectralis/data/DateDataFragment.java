package de.halirutan.spectralis.data;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;

import de.halirutan.spectralis.filestructure.COMDate;
import de.halirutan.spectralis.filestructure.DataTypes;

/**
 * Created by patrick on 09.01.17.
 * (c) Patrick Scheibe 2017
 */
public class DateDataFragment extends DataFragment<LocalDateTime> {


    @Override
    public final LocalDateTime read(RandomAccessFile file) throws IOException {
        ByteBuffer buffer = readIntoBuffer(file, DataTypes.Date);
        return COMDate.toLocalDateTime(buffer.getDouble());
    }
}
