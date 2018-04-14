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
