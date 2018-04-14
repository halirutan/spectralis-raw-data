/*
 * Copyright (c) 2018 Patrick Scheibe
 * Affiliation: Saxonian Incubator for Clinical Translation, University Leipzig, Germany
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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

/**
 * Utility class that is heavily used for reading data from the file and from buffers. Note that the Spectralis OCT format
 * uses little endian byte order. Since most systems use big endian, we cannot directly read e.g. floats or doubles from the
 * file. The general approach is to read the complete chunk, e.g. the complete file header, into a {@link ByteBuffer} and
 * then read containing values as specified in the Spectralis manual.
 */
public class Util {

    private static final Charset CHARSET = StandardCharsets.US_ASCII;
    private static final ByteOrder BYTE_ORDER = ByteOrder.LITTLE_ENDIAN;
    private static final LocalDateTime START_TIME = LocalDateTime.of(1601, 1, 1, 0, 0);
    private static final long DATE_SCALE = 10000000L;
    private static final LocalDateTime ZERO_COM_TIME = LocalDateTime.of(1899, 12, 30, 0, 0);
    private static final BigDecimal MILLIS_PER_DAY = new BigDecimal(86400000);

    private Util() {
    }

    /**
     * Strings in the file are zero-terminated. Therefore, all provided strings like Id or PatientId are trimmed by default.
     * @param file Input file
     * @param size Number of bytes to be read
     * @return Trimmed string
     * @throws IOException If reading failed
     */
    @SuppressWarnings("SameParameterValue")
    static String readStringTrimmed(RandomAccessFile file, int size) throws IOException {
        return readString(file, size).trim();
    }

    private static String readString(RandomAccessFile file, int size) throws IOException {
        byte[] result = new byte[size];
        file.read(result);
        return new String(result, CHARSET);
    }

    private static String getString(ByteBuffer buffer, int size) {
        byte[] result = new byte[size];
        buffer.get(result);
        return new String(result, CHARSET);
    }

    /**
     * Strings in the file are zero-terminated. Therefore, all provided strings like Id or PatientId are trimmed by default.
     * @param buffer Buffer to read from
     * @param size Number of chars to be read
     * @return Trimmed string
     */
    static String getStringTrimmed(ByteBuffer buffer, int size) {
        return getString(buffer, size).trim();
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

    @SuppressWarnings("SameParameterValue")
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
