package de.halirutan.spectralis.data;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

/**
 * Created by patrick on 10.01.17.
 * (c) Patrick Scheibe 2017
 */
public class COMDateTest {
    @Test
    public void toLocalDateTime() throws Exception {
        assertEquals(COMDate.toLocalDateTime(0), LocalDateTime.of(1899, 12, 30, 0, 0));
        assertEquals(COMDate.toLocalDateTime(2), LocalDateTime.of(1900, 1, 1, 0, 0));
        assertEquals(COMDate.toLocalDateTime(5), LocalDateTime.of(1900, 1, 4, 0, 0));
        assertEquals(COMDate.toLocalDateTime(5.25), LocalDateTime.of(1900, 1, 4, 6, 0));
        assertEquals(COMDate.toLocalDateTime(5.5), LocalDateTime.of(1900, 1, 4, 12, 0));
        assertEquals(COMDate.toLocalDateTime(5.875), LocalDateTime.of(1900, 1, 4, 21, 0));


    }

}