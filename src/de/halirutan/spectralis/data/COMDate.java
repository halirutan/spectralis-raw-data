package de.halirutan.spectralis.data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

/**
 * Bits of this implementation were stolen from this StackOverflow article
 * @see <a href="http://stackoverflow.com/a/28479647/1078614">OLEDate java implementation</a>
 * Created by patrick on 10.01.17.
 * (c) Patrick Scheibe 2017
 */
public class COMDate {

    private static final LocalDateTime ZERO_COM_TIME = LocalDateTime.of(1899, 12, 30, 0, 0);
    private static final BigDecimal MILLIS_PER_DAY = new BigDecimal(86400000);

    public static LocalDateTime toLocalDateTime(final double d) {
        return toLocalDateTime(BigDecimal.valueOf(d));
    }

    public static LocalDateTime toLocalDateTime(BigDecimal comTime) {
        BigDecimal daysAfterZero = comTime.setScale(0, RoundingMode.DOWN);
        BigDecimal fraction = comTime.subtract(daysAfterZero).abs(); //fraction always represents the time of that day
        BigDecimal fractionMillisAfterZero = fraction.multiply(MILLIS_PER_DAY).setScale(0, RoundingMode.HALF_DOWN);

        return ZERO_COM_TIME.plusDays(daysAfterZero.intValue()).plus(fractionMillisAfterZero.longValue(), ChronoUnit.MILLIS);
    }

}
