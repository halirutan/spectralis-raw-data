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

package de.halirutan.spectralis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by patrick on 10.01.17.
 * (c) Patrick Scheibe 2017
 */
public class Util {
    public static final String VALID = "valid.vol";
    public static final String INVALID = "invalid.vol";
    public static final String LAYERS = "layers.vol";
    public static final String WILLIS_712 = "Willis_B_712.vol";
    public static final String WILLIS_713 = "Willis_B_713.vol";
    public static final String WILLIS_714 = "Willis_B_714.vol";
    public static final String WILLIS_715 = "Willis_B_715.vol";
    public static final String WILLIS_716_0 = "Willis_B_716_0.vol";
    public static final String WILLIS_716_2 = "Willis_B_716_2.vol";
    public static final String WILLIS_716_4 = "Willis_B_716_4.vol";


    public static RandomAccessFile getVolRandomAccessFile(String  fileName) throws FileNotFoundException, URISyntaxException {
        File volFile = getVolFile(fileName);
        return new RandomAccessFile(volFile, "r");
    }

    public static File getVolFile(String fileName) throws URISyntaxException {
            URL validURL = ClassLoader.getSystemResource(fileName);
            return new File(validURL.toURI());
    }

}
