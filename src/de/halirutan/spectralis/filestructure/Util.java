package de.halirutan.spectralis.filestructure;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Util {

    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private static final ByteOrder BYTE_ORDER = ByteOrder.LITTLE_ENDIAN;
    private static final LocalDateTime START_TIME = LocalDateTime.of(1601, 1, 1, 0, 0);
    private static final long DATE_SCALE = 10000000L;
    private static final LocalDateTime ZERO_COM_TIME = LocalDateTime.of(1899, 12, 30, 0, 0);
    private static final BigDecimal MILLIS_PER_DAY = new BigDecimal(86400000);

    private Util() {
    }

    static String readString(RandomAccessFile file, int size) throws IOException {
        byte[] result = new byte[size];
        file.read(result);
        return new String(result);
    }

    static String readStringTrimmed(RandomAccessFile file, int size) throws IOException {
        return readString(file, size).trim();
    }

    static String getString(ByteBuffer buffer, int size) {
        byte[] result = new byte[size];
        buffer.get(result);
        return new String(result, CHARSET);
    }

    static String getStringTrimmed(ByteBuffer buffer, int numChars) {
        return getString(buffer, numChars).trim();
    }

    static float[] getFloatArray(ByteBuffer buffer, int numFloats) {
        float[] result = new float[numFloats];
        for (int i = 0; i < numFloats; i++) {
            result[i] = buffer.getFloat();
        }
        return result;
    }

    static double[] getDoubleArray(ByteBuffer buffer, int numDoubles) {
        double[] result = new double[numDoubles];
        for (int i = 0; i < numDoubles; i++) {
            result[i] = buffer.getDouble();
        }
        return result;
    }

    static int[] getIntArray(ByteBuffer buffer, int size) {
        int[] result = new int[size];
        for (int i = 0; i < size; i++) {
            result[i] = buffer.getInt();
        }
        return result;
    }


    static LocalDateTime readExamTime(ByteBuffer buffer) {
        long seconds = buffer.getLong() / DATE_SCALE;
        return START_TIME.plus(seconds, ChronoUnit.SECONDS);
    }

    static LocalDateTime readDate(ByteBuffer buffer) {
        return toLocalDateTime(buffer.getDouble());
    }

    static Sector readSector(ByteBuffer buffer) {
        float thickness = buffer.getFloat();
        float volume = buffer.getFloat();
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
    static ByteBuffer readIntoBuffer(RandomAccessFile file, int offset, int size) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(size);
        buffer.order(BYTE_ORDER);
        file.seek(offset);
        file.read(buffer.array());
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
