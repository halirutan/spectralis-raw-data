package de.halirutan.spectralis.data;

import java.time.LocalDateTime;

import de.halirutan.spectralis.filestructure.Util;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by patrick on 10.01.17.
 * (c) Patrick Scheibe 2017
 */
@SuppressWarnings("MagicNumber")
public class COMDateTest {
    @Test
    public final void toLocalDateTime() {
        assertEquals(LocalDateTime.of(1899, 12, 30, 0, 0), Util.toLocalDateTime(0));
        assertEquals(LocalDateTime.of(1900, 1, 1, 0, 0), Util.toLocalDateTime(2));
        assertEquals(LocalDateTime.of(1900, 1, 4, 0, 0), Util.toLocalDateTime(5));
        assertEquals(LocalDateTime.of(1900, 1, 4, 6, 0), Util.toLocalDateTime(5.25));
        assertEquals(LocalDateTime.of(1900, 1, 4, 12, 0), Util.toLocalDateTime(5.5));
        assertEquals(LocalDateTime.of(1900, 1, 4, 21, 0), Util.toLocalDateTime(5.875));
    }

}
