package de.halirutan.spectralis.filestructure;

import java.io.DataInput;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Util {

    private static final String CHARSET = "US-ASCII";
    private static final ByteOrder BYTE_ORDER = ByteOrder.LITTLE_ENDIAN;
    private static final LocalDateTime START_TIME = LocalDateTime.of(1601, 1, 1, 0, 0);
    private static final long DATE_SCALE = 10000000L;
    private static final LocalDateTime ZERO_COM_TIME = LocalDateTime.of(1899, 12, 30, 0, 0);
    private static final BigDecimal MILLIS_PER_DAY = new BigDecimal(86400000);

    private Util() {
    }

    static String readString(RandomAccessFile file, int size) throws IOException {
        byte result[] = new byte[size];
        file.read(result, 0, size);
        return new String(result, Charset.forName(CHARSET));
    }

    static String readStringTrimmed(RandomAccessFile file, int size) throws IOException {
        return readString(file, size).trim();
    }

    static float[] readFloatArray(DataInput file, int size) throws IOException {
        float [] result = new float[size];
        for (int i = 0; i < size; i++) {
            result[i] = file.readFloat();
        }
        return result;
    }

    static double[] readDoubleArray(DataInput file, int size) throws IOException {
        double [] result = new double[size];
        for (int i = 0; i < size; i++) {
            result[i] = file.readDouble();
        }
        return result;
    }

    static int[] readIntArray(DataInput file, int size) throws IOException {
        int [] result = new int[size];
        for (int i = 0; i < size; i++) {
            result[i] = file.readInt();
        }
        return result;
    }

    static LocalDateTime readExamTime(RandomAccessFile file) throws IOException {
        ByteBuffer buffer = readIntoBuffer(file, DataTypes.ExamTime);
        long seconds = buffer.getLong() / DATE_SCALE;
        return START_TIME.plus(seconds, ChronoUnit.SECONDS);
    }

    static LocalDateTime readDate(RandomAccessFile file) throws IOException {
        ByteBuffer buffer = readIntoBuffer(file, DataTypes.Date);
        return toLocalDateTime(buffer.getDouble());
    }

    static Sector readSector(DataInput file) throws IOException {
        float thickness = file.readFloat();
        float volume = file.readFloat();
        return new Sector(thickness, volume);
    }

    /**
     * Reading bytes from a file into a {@link ByteBuffer}
     *
     * @param file Opened and readable file
     * @param size Number of bytes to be read
     * @return The bytes read, wrapped into a {@link ByteBuffer}
     * @throws IOException if something went wrong during reading
     */
    static ByteBuffer readIntoBuffer(RandomAccessFile file, int size) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(size);
        buffer.order(BYTE_ORDER);
        file.read(buffer.array(), 0, size);
        return buffer;
    }


    public static LocalDateTime toLocalDateTime(double d) {
        return toLocalDateTime(BigDecimal.valueOf(d));
    }

    private static LocalDateTime toLocalDateTime(BigDecimal comTime) {
        BigDecimal daysAfterZero = comTime.setScale(0, RoundingMode.DOWN);
        BigDecimal fraction = comTime.subtract(daysAfterZero).abs(); //fraction always represents the time of that day
        BigDecimal fractionMillisAfterZero = fraction.multiply(MILLIS_PER_DAY).setScale(0, RoundingMode.HALF_DOWN);

        return ZERO_COM_TIME.plusDays(daysAfterZero.intValue()).plus(fractionMillisAfterZero.longValue(), ChronoUnit.MILLIS);
    }

}
