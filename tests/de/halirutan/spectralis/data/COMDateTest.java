package de.halirutan.spectralis.data;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

/**
 * Created by patrick on 10.01.17.
 * (c) Patrick Scheibe 2017
 */
@SuppressWarnings("MagicNumber")
public class COMDateTest {
    @Test
    public final void toLocalDateTime() {
        assertEquals(LocalDateTime.of(1899, 12, 30, 0, 0), COMDate.toLocalDateTime(0));
        assertEquals(LocalDateTime.of(1900, 1, 1, 0, 0), COMDate.toLocalDateTime(2));
        assertEquals(LocalDateTime.of(1900, 1, 4, 0, 0), COMDate.toLocalDateTime(5));
        assertEquals(LocalDateTime.of(1900, 1, 4, 6, 0), COMDate.toLocalDateTime(5.25));
        assertEquals(LocalDateTime.of(1900, 1, 4, 12, 0), COMDate.toLocalDateTime(5.5));
        assertEquals(LocalDateTime.of(1900, 1, 4, 21, 0), COMDate.toLocalDateTime(5.875));
    }

}
